package view;

import entity.Task;

import interface_adapter.calendar.TaskClickListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

/**
 * A layered pane that:
 * - Normalizes midnight-end back one tick so tasks ending at 00:00 stay on the prior day,
 * - Splits tasks at true calendar-day boundaries,
 * - Slots overlapping segments side-by-side.
 */
public class TaskGridPane extends JLayeredPane {

    private static final int QUARTERS_PER_HOUR = 4;
    private static final int HOURS_PER_DAY = 24;
    private static final int TOTAL_QUARTERS = HOURS_PER_DAY * QUARTERS_PER_HOUR;
    private static final int CELL_HEIGHT = 20;
    private static final int DAYS_IN_WEEK = 7;
    private static final int DAY_COLUMN_WIDTH = 50;
    private static final float FONT_SIZE = 12f;
    private static final int MIN_QUARTER_SPAN = 1;
    private static final int MINUTES_IN_QUARTER = 15;
    private static final int MINUTES_ROUNDING_OFFSET = 7;

    private final List<LocalDate> weekDates;
    private final List<Segment> segments = new ArrayList<>();

    public TaskGridPane(final CalendarData data, final List<Task> tasks, final TaskClickListener onClick) {
        this.weekDates = data.getWeekDates();

        setLayout(null);
        setOpaque(true);
        setBackground(Color.WHITE);

        final int width = DAYS_IN_WEEK * DAY_COLUMN_WIDTH;
        final int height = TOTAL_QUARTERS * CELL_HEIGHT;
        setPreferredSize(new Dimension(width, height));

        for (final Task task : tasks) {
            final LocalDateTime start = task.getTaskInfo().getStartDateTime();
            LocalDateTime end = task.getTaskInfo().getEndDateTime();

            if (end.toLocalTime().equals(LocalTime.MIDNIGHT)) {
                end = end.minusNanos(1);
            }

            long daysSpan = 0L;
            if (end.toLocalDate().isAfter(start.toLocalDate())) {
                daysSpan = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());
                daysSpan = Math.min(daysSpan, weekDates.size() - 1);
            }

            for (int dayOffset = 0; dayOffset <= daysSpan; dayOffset++) {
                final LocalDateTime segmentStart;
                if (dayOffset == 0) {
                    segmentStart = start;
                }
                else {
                    segmentStart = start.toLocalDate().plusDays(dayOffset).atStartOfDay();
                }

                final LocalDateTime segmentEnd;
                if (dayOffset == daysSpan) {
                    segmentEnd = end;
                }
                else {
                    segmentEnd = start.toLocalDate()
                            .plusDays(dayOffset + 1)
                            .atStartOfDay()
                            .minusNanos(1);
                }

                final Segment segment = new Segment(task, segmentStart, segmentEnd, weekDates, onClick);
                segments.add(segment);
                add(segment.getButton(), JLayeredPane.PALETTE_LAYER);
            }
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final int width = getWidth();
        final int height = getHeight();

        final int cols = weekDates.size();
        final int rows = TOTAL_QUARTERS;

        final int cellWidth = width / cols;
        final int cellHeight = height / rows;

        drawGridLines(g, width, height, cols, rows, cellWidth, cellHeight);
        layoutAndPositionSegments(cols, cellWidth, cellHeight);
    }

    private void drawGridLines(final Graphics graphics, final int width, final int height,
                               final int cols, final int rows,
                               final int cellWidth, final int cellHeight) {
        graphics.setColor(Color.LIGHT_GRAY);
        for (int c = 0; c <= cols; c++) {
            final int x = c * cellWidth;
            graphics.drawLine(x, 0, x, height);
        }

        graphics.setColor(Color.BLACK);
        for (int q = 0; q <= rows; q += QUARTERS_PER_HOUR) {
            final int y = q * cellHeight;
            graphics.drawLine(0, y, width, y);
        }
    }

    private void layoutAndPositionSegments(final int cols, final int cellWidth, final int cellHeight) {
        for (int dayCol = 0; dayCol < cols; dayCol++) {
            final int dc = dayCol;
            final List<Segment> colSegments = segments.stream()
                    .filter(seg -> seg.getDayIndex() == dc)
                    .sorted(Comparator.comparingInt(Segment::getStartQ))
                    .toList();

            final List<List<Segment>> slots = new ArrayList<>();
            for (final Segment segment : colSegments) {
                boolean placed = false;
                for (int i = 0; i < slots.size(); i++) {
                    final List<Segment> slotList = slots.get(i);
                    final Segment last = slotList.get(slotList.size() - 1);
                    if (segment.getStartQ() >= last.getEndQ()) {
                        segment.setSlot(i);
                        slotList.add(segment);
                        placed = true;
                        break;
                    }
                }
                if (!placed) {
                    segment.setSlot(slots.size());
                    final List<Segment> newSlot = new ArrayList<>();
                    newSlot.add(segment);
                    slots.add(newSlot);
                }
            }

            final int slotCount = Math.max(1, slots.size());

            for (final Segment segment : colSegments) {
                final int startQ = segment.getStartQ();
                final int endQ = segment.getEndQ();
                final int slot = segment.getSlot();
                final int spanQ = Math.max(MIN_QUARTER_SPAN, endQ - startQ);

                final int x = dc * cellWidth + slot * (cellWidth / slotCount);
                final int y = startQ * cellHeight;
                final int w = cellWidth / slotCount;
                final int h = spanQ * cellHeight;

                segment.getButton().setBounds(x, y, w, h);
            }
        }
    }

    private static class Segment {

        private static final int MINUTES_PER_HOUR = 60;
        private static final int MINUTES_PER_QUARTER = 15;
        private static final int ROUNDING_OFFSET = 7;
        private static final int BUTTON_MARGIN_TOP = 2;
        private static final int BUTTON_MARGIN_LEFT = 4;
        private static final int BUTTON_MARGIN_BOTTOM = 2;
        private static final int BUTTON_MARGIN_RIGHT = 4;

        private final JButton button;
        private final int startQ;
        private final int endQ;
        private final int dayIndex;
        private int slot;

        Segment(final Task task, final LocalDateTime segmentStart, final LocalDateTime segmentEnd,
                final List<LocalDate> weekDates, final TaskClickListener listener) {

            this.startQ = segmentStart.getHour() * QUARTERS_PER_HOUR
                    + (segmentStart.getMinute() + ROUNDING_OFFSET) / MINUTES_PER_QUARTER;

            this.endQ = segmentEnd.getHour() * QUARTERS_PER_HOUR
                    + (segmentEnd.getMinute() + ROUNDING_OFFSET) / MINUTES_PER_QUARTER;

            final int idx = weekDates.indexOf(segmentStart.toLocalDate());
            if (idx >= 0) {
                this.dayIndex = idx;
            }
            else {
                this.dayIndex = 0;
            }

            String text = task.getTaskInfo().getTaskName();
            if (task.getTaskInfo().getTag() != null) {
                text += " " + task.getTaskInfo().getTag().getTagIcon();
            }

            this.button = new JButton(text);
            button.setMargin(new Insets(
                    BUTTON_MARGIN_TOP,
                    BUTTON_MARGIN_LEFT,
                    BUTTON_MARGIN_BOTTOM,
                    BUTTON_MARGIN_RIGHT
            ));
            button.setFont(button.getFont().deriveFont(FONT_SIZE));
            button.setOpaque(true);
            button.setBorderPainted(false);

            final boolean incomplete = "Incomplete".equals(task.getTaskInfo().getTaskStatus());
            if (incomplete) {
                switch (task.getTaskInfo().getPriority()) {
                    case HIGH -> button.setBackground(Color.RED);
                    case MEDIUM -> button.setBackground(Color.ORANGE);
                    case LOW -> button.setBackground(Color.YELLOW);
                    default -> button.setBackground(Color.LIGHT_GRAY);
                }
            }
            else {
                button.setBackground(Color.LIGHT_GRAY);
            }

            button.addActionListener(event -> listener.onTaskClick(task));
        }

        public JButton getButton() {
            return button;
        }

        public int getStartQ() {
            return startQ;
        }

        public int getEndQ() {
            return endQ;
        }

        public int getDayIndex() {
            return dayIndex;
        }

        public int getSlot() {
            return slot;
        }

        public void setSlot(final int slot) {
            this.slot = slot;
        }
    }
}

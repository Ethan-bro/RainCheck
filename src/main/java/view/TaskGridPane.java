package view;

import interface_adapter.calendar.TaskClickListener;
import entity.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A layered pane that:
 * - Normalizes midnight‐end back one tick so tasks ending at 00:00 stay on the prior day,
 * - Splits tasks at true calendar‐day boundaries,
 * - Slots overlapping segments side‐by‐side.
 */
public class TaskGridPane extends JLayeredPane {
    private static final int QUARTERS_PER_HOUR = 4;
    private static final int TOTAL_QUARTERS    = 24 * QUARTERS_PER_HOUR;

    private final List<LocalDate> weekDates;
    private final List<Segment> segments = new ArrayList<>();

    public TaskGridPane(CalendarData data,
                        List<Task> tasks,
                        TaskClickListener onClick) {
        this.weekDates = data.getWeekDates();
        setLayout(null);
        setOpaque(true);
        setBackground(Color.WHITE);

        int W = 7 * 50, H = TOTAL_QUARTERS * 20;
        setPreferredSize(new Dimension(W, H));

        for (Task t : tasks) {
            LocalDateTime start = t.getTaskInfo().getStartDateTime();
            LocalDateTime end   = t.getTaskInfo().getEndDateTime();

            // normalize exact‐midnight endings
            if (end.toLocalTime().equals(LocalTime.MIDNIGHT)) {
                end = end.minusNanos(1);
            }

            long daysSpan = 0;
            if (end.toLocalDate().isAfter(start.toLocalDate())) {
                daysSpan = ChronoUnit.DAYS.between(
                        start.toLocalDate(), end.toLocalDate()
                );
                daysSpan = Math.min(daysSpan, weekDates.size() - 1);
            }

            for (int d = 0; d <= daysSpan; d++) {
                LocalDateTime segStart = (d == 0)
                        ? start
                        : start.toLocalDate().plusDays(d).atStartOfDay();
                LocalDateTime segEnd = (d == daysSpan)
                        ? end
                        : start.toLocalDate()
                        .plusDays(d + 1)
                        .atStartOfDay()
                        .minusNanos(1);

                Segment seg = new Segment(t, segStart, segEnd, weekDates, onClick);
                segments.add(seg);
                add(seg.button, JLayeredPane.PALETTE_LAYER);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int W = getWidth(), H = getHeight();
        int cols = weekDates.size(), rows = TOTAL_QUARTERS;
        int cellW = W / cols, cellH = H / rows;

        // vertical lines
        g.setColor(Color.LIGHT_GRAY);
        for (int c = 0; c <= cols; c++) {
            int x = c * cellW;
            g.drawLine(x, 0, x, H);
        }

        // horizontal hour lines
        g.setColor(Color.BLACK);
        for (int q = 0; q <= rows; q += QUARTERS_PER_HOUR) {
            int y = q * cellH;
            g.drawLine(0, y, W, y);
        }

        // for each day‐column
        for (int dayCol = 0; dayCol < cols; dayCol++) {
            final int dc = dayCol;
            List<Segment> colSegs = segments.stream()
                    .filter(s -> s.dayIndex == dc)
                    .sorted(Comparator.comparingInt(s -> s.startQ))
                    .toList();

            // assign slots
            List<List<Segment>> slots = new ArrayList<>();
            for (Segment s : colSegs) {
                boolean placed = false;
                for (int i = 0; i < slots.size(); i++) {
                    Segment last = slots.get(i).get(slots.get(i).size() - 1);
                    if (s.startQ >= last.endQ) {
                        s.slot = i;
                        slots.get(i).add(s);
                        placed = true;
                        break;
                    }
                }
                if (!placed) {
                    s.slot = slots.size();
                    List<Segment> newSlot = new ArrayList<>();
                    newSlot.add(s);
                    slots.add(newSlot);
                }
            }

            final int slotCount = Math.max(1, slots.size());

            // position each segment
            for (Segment s : colSegs) {
                final int startQ = s.startQ;
                final int endQ   = s.endQ;
                final int slot   = s.slot;
                // <— force at least 1 quarter‐hour
                final int spanQ  = Math.max(1, endQ - startQ);

                int x = dc * cellW + slot * (cellW / slotCount);
                int y = startQ * cellH;
                int w = cellW / slotCount;
                int h = spanQ * cellH;

                s.button.setBounds(x, y, w, h);
            }
        }
    }

    private static class Segment {
        final JButton button;
        final int startQ, endQ, dayIndex;
        int slot = 0;

        Segment(Task t,
                LocalDateTime segStart,
                LocalDateTime segEnd,
                List<LocalDate> weekDates,
                TaskClickListener listener) {

            this.startQ = segStart.getHour() * QUARTERS_PER_HOUR
                    + (segStart.getMinute() + 7) / 15;
            this.endQ   = segEnd.getHour() * QUARTERS_PER_HOUR
                    + (segEnd.getMinute() + 7) / 15;

            int idx = weekDates.indexOf(segStart.toLocalDate());
            this.dayIndex = idx >= 0 ? idx : 0;

            // build label: name + tag emoji (no TaskID)
            String txt = t.getTaskInfo().getTaskName();
            if (t.getTaskInfo().getTag() != null) {
                txt += " " + t.getTaskInfo().getTag().getTagIcon();
            }

            button = new JButton(txt);
            button.setMargin(new Insets(2, 4, 2, 4));
            button.setFont(button.getFont().deriveFont(12f));
            button.setOpaque(true);
            button.setBorderPainted(false);

            boolean incomplete = "Incomplete".equals(t.getTaskInfo().getTaskStatus());
            if (incomplete) {
                switch (t.getTaskInfo().getPriority()) {
                    case HIGH   -> button.setBackground(Color.RED);
                    case MEDIUM -> button.setBackground(Color.ORANGE);
                    case LOW    -> button.setBackground(Color.YELLOW);
                }
            } else {
                button.setBackground(Color.LIGHT_GRAY);
            }

            button.addActionListener(e -> listener.onTaskClick(t));
        }
    }
}

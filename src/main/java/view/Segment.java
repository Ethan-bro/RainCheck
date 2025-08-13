package view;

import entity.Task;

import interface_adapter.calendar.TaskClickListener;

import java.awt.Color;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;

public class Segment {

    private static final int QUARTERS_PER_HOUR = 4;
    private static final int MINUTES_PER_QUARTER = 15;
    private static final int ROUNDING_OFFSET = 7;
    private static final int BUTTON_MARGIN_TOP = 2;
    private static final int BUTTON_MARGIN_LEFT = 4;
    private static final int BUTTON_MARGIN_BOTTOM = 2;
    private static final int BUTTON_MARGIN_RIGHT = 4;

    private static final float FONT_SIZE = 12f;

    private final JButton button;
    private final int startQ;
    private final int endQ;
    private final int dayIndex;
    private int slot;

    /**
     * Constructs a Segment representing a task's visual block in the calendar grid.
     * @param task the task represented by this segment
     * @param segmentStart the start time of the segment
     * @param segmentEnd the end time of the segment
     * @param weekDates the list of week dates for column calculation
     * @param listener the click listener for the segment button
     */
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
                case HIGH ->
                    button.setBackground(Color.RED);
                case MEDIUM ->
                    button.setBackground(Color.ORANGE);
                case LOW ->
                    button.setBackground(Color.YELLOW);
                default ->
                    button.setBackground(Color.LIGHT_GRAY);
            }
        }
        else {
            button.setBackground(Color.LIGHT_GRAY);
        }

        button.addActionListener(event -> listener.onTaskClick(task));
    }

    /**
     * Gets the button representing this task segment.
     *
     * @return the JButton for this segment
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Gets the starting quarter index for this segment.
     *
     * @return the start quarter
     */
    public int getStartQ() {
        return startQ;
    }

    /**
     * Gets the ending quarter index for this segment.
     *
     * @return the end quarter
     */
    public int getEndQ() {
        return endQ;
    }

    /**
     * Gets the day index (column) for this segment.
     *
     * @return the day index
     */
    public int getDayIndex() {
        return dayIndex;
    }

    /**
     * Gets the slot index for overlapping segments.
     *
     * @return the slot index
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Sets the slot index for this segment.
     * @param slot the slot index
     */
    public void setSlot(final int slot) {
        this.slot = slot;
    }
}

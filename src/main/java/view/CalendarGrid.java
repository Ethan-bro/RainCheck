package view;

import interface_adapter.calendar.TaskClickListener;
import entity.Task;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

public class CalendarGrid extends JPanel {

    private final ActionListener addTaskListener;
    private final ActionListener manageTagsListener;
    private final ActionListener logoutListener;

    public CalendarGrid(CalendarData data,
                        Map<LocalDate, Map<String, Object>> weatherMap,
                        List<Task> tasks,
                        CalendarListeners calendarListeners) {

        super(new GridBagLayout());
        this.addTaskListener = calendarListeners.getAddTaskListener();
        this.manageTagsListener = calendarListeners.getManageTagsListener();
        this.logoutListener = calendarListeners.getLogoutListener();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1,1,1,1);

        List<LocalDate> weekDates = data.getWeekDates();

        // compute which date/hour cells are covered by spanning tasks
        Set<String> covered = new HashSet<>();
        for (Task t : tasks) {
            LocalDateTime s = t.getTaskInfo().getStartDateTime();
            LocalDateTime e = t.getTaskInfo().getEndDateTime();
            LocalDate date = s.toLocalDate();
            int startHour = s.getHour();
            int endHour   = e.getHour();
            for (int h = startHour; h < endHour; h++) {
                covered.add(date.toString() + "#" + h);
            }
        }

        // --- Row 0: headers (empty corner + days/weather) ---
        for (int col = 0; col < 8; col++) {
            gbc.gridx = col;
            gbc.gridy = 0;
            gbc.gridwidth  = 1;
            gbc.gridheight = 1;
            gbc.weightx = (col == 0 ? 0.0 : 1.0);
            gbc.weighty = 0.0;
            gbc.fill    = GridBagConstraints.BOTH;

            if (col == 0) {
                add(new JLabel(""), gbc);
            } else {
                LocalDate date = weekDates.get(col - 1);
                JPanel header = new JPanel(new BorderLayout());
                header.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                String dayName = date.getDayOfWeek()
                        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                String fullDay = dayName + " the " + getOrdinal(date.getDayOfMonth());
                JLabel dayLabel = new JLabel(fullDay, SwingConstants.CENTER);
                dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD, 14f));
                header.add(dayLabel, BorderLayout.NORTH);

                Map<String, Object> weather = weatherMap.get(date);
                JLabel iconLabel = new JLabel();
                if (weather != null && weather.get("icon") != null) {
                    iconLabel.setIcon((ImageIcon) weather.get("icon"));
                }
                iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
                header.add(iconLabel, BorderLayout.CENTER);

                JPanel tempsPanel = new JPanel(new GridLayout(1,2));
                tempsPanel.setPreferredSize(new Dimension(0,20));
                if (weather != null) {
                    tempsPanel.add(new JLabel(
                            String.format("%.1f°C", (double)weather.get("tempmin")),
                            SwingConstants.CENTER));
                    tempsPanel.add(new JLabel(
                            String.format("%.1f°C", (double)weather.get("tempmax")),
                            SwingConstants.CENTER));
                }
                header.add(tempsPanel, BorderLayout.SOUTH);

                add(header, gbc);
            }
        }

        // --- Rows 1–24: first draw empty cells (to get borders) ---
        for (int hour = 0; hour < 24; hour++) {
            int row = hour + 1;
            for (int col = 0; col < 8; col++) {
                gbc.gridx      = col;
                gbc.gridy      = row;
                gbc.gridwidth  = 1;
                gbc.gridheight = 1;
                gbc.weightx    = (col == 0 ? 0.0 : 1.0);
                gbc.weighty    = 1.0;
                gbc.fill       = GridBagConstraints.BOTH;

                JPanel cell = new JPanel();
                cell.setOpaque(false);
                String key = (col > 0 ? weekDates.get(col-1).toString() + "#" + hour : null);
                if (col == 0 || (key != null && !covered.contains(key))) {
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }

                if (col == 0) {
                    // hour label column
                    cell.setLayout(new GridBagLayout());
                    JLabel hourLabel = new JLabel(
                            CalendarData.HOURS_OF_DAY[hour],
                            SwingConstants.CENTER);
                    cell.add(hourLabel);
                }
                // else: leave empty for now

                add(cell, gbc);
            }
        }

        // --- Then overlay each task button, spanning its rows ---
        for (Task t : tasks) {
            LocalDateTime start = t.getTaskInfo().getStartDateTime();
            LocalDateTime end   = t.getTaskInfo().getEndDateTime();

            int dayOfWeekVal = start.getDayOfWeek().getValue() % 7; // Sunday=0 .. Saturday=6
            int col = dayOfWeekVal + 1;                            // gridx=1..7
            int row = start.getHour() + 1;  // gridy=1..24

            long durationHrs = Math.max(1,
                    Duration.between(start, end).toHours());
            int span = (int)durationHrs;

            JButton btn = getJButton(calendarListeners.getTaskClickListener(), t);

            gbc.gridx      = col;
            gbc.gridy      = row;
            gbc.gridheight = span;
            gbc.weightx    = 1.0;
            gbc.weighty    = span;
            gbc.fill       = GridBagConstraints.BOTH;

            // Remove border on the blank cell underneath:
            // (optional) you might want btn.setBorder(null);
            add(btn, gbc);
        }
    }

    @NotNull
    private static JButton getJButton(TaskClickListener taskClickListener, Task t) {
        String text = t.getTaskInfo().getTaskName();
        if (t.getTaskInfo().getTag() != null && t.getTaskInfo().getTag().getTagEmoji() != null) {
            text += " " + t.getTaskInfo().getTag().getTagEmoji();
        }
        JButton button = new JButton(text);
        button.setMargin(new Insets(2, 4, 2, 4));
        button.setFont(button.getFont().deriveFont(12f));

        button.setOpaque(true);
        button.setBorderPainted(false);

        if (t.getTaskInfo().getTaskStatus().equals("Incomplete")) {
            switch (t.getTaskInfo().getPriority()) {
                case HIGH:
                    button.setBackground(Color.RED);
                    break;
                case MEDIUM:
                    button.setBackground(Color.ORANGE);
                    break;
                case LOW:
                    button.setBackground(Color.YELLOW);
                    break;
            }
        } else {
            button.setBackground(Color.LIGHT_GRAY);
        }

        button.addActionListener(e -> taskClickListener.onTaskClick(t));
        return button;
    }

    private void showSideMenu(Component invoker) {
        JPopupMenu sideMenu = new JPopupMenu();

        JMenuItem addTaskItem = new JMenuItem("Add Task");
        JMenuItem manageTagsItem = new JMenuItem("Manage Tags");
        JMenuItem logoutItem = new JMenuItem("Log Out");

        addTaskItem.addActionListener(addTaskListener);
        manageTagsItem.addActionListener(manageTagsListener);
        logoutItem.addActionListener(logoutListener);

        sideMenu.add(addTaskItem);
        sideMenu.add(manageTagsItem);
        sideMenu.add(logoutItem);

        sideMenu.show(invoker, 0, invoker.getHeight());
    }

    private String getOrdinal(int number) {
        if (number >= 11 && number <= 13) return number + "th";
        return switch (number % 10) {
            case 1 -> number + "st";
            case 2 -> number + "nd";
            case 3 -> number + "rd";
            default -> number + "th";
        };
    }
}

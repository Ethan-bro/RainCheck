package view;

import interface_adapter.calendar.TaskClickListener;
import entity.Task;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
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
                        TaskClickListener taskClickListener,
                        ActionListener addTaskListener,
                        ActionListener manageTagsListener,
                        ActionListener logoutListener) {

        this.addTaskListener = addTaskListener;
        this.manageTagsListener = manageTagsListener;
        this.logoutListener = logoutListener;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        List<LocalDate> weekDates = data.getWeekDates();

        Map<LocalDate, Map<Integer, List<Task>>> taskMap = new HashMap<>();
        for (Task t : tasks) {
            LocalDate date = t.getTaskInfo().getStartDateTime().toLocalDate();
            int hour = t.getTaskInfo().getStartDateTime().getHour();
            taskMap
                    .computeIfAbsent(date, d -> new HashMap<>())
                    .computeIfAbsent(hour, h -> new ArrayList<>())
                    .add(t);
        }

        for (int row = 0; row < 26; row++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new GridLayout(1, 8));
            int rowHeight = (row == 1) ? 80 : 40;
            rowPanel.setPreferredSize(new Dimension(800, rowHeight));

            for (int col = 0; col < 8; col++) {
                JPanel cell = new JPanel();

                if (row == 0) {
                    cell.setBorder(null);
                    if (col == 0) {
                        cell.add(new JLabel(""));
                    }
                } else if (row == 1) {
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    if (col == 0) {
                        JButton menuButton = new JButton("☰");
                        menuButton.addActionListener(e -> showSideMenu(menuButton));
                        cell.add(menuButton);
                    } else {
                        LocalDate date = weekDates.get(col - 1);
                        Map<String, Object> weather = weatherMap.get(date);
                        cell.setLayout(new BorderLayout());

                        // Updated: Top - Day name with number, like "Sun the 28th"
                        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                        int dayOfMonth = date.getDayOfMonth();
                        String fullDay = dayName + " the " + getOrdinal(dayOfMonth);
                        JLabel dayLabel = new JLabel(fullDay);
                        dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD, 14f));
                        cell.add(dayLabel, BorderLayout.NORTH);

                        // Center: Weather icon
                        JLabel iconLabel = new JLabel();
                        ImageIcon icon = (ImageIcon) weather.get("icon");
                        if (icon != null) {
                            iconLabel.setIcon((ImageIcon) weather.get("icon"));
                        }
                        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        cell.add(iconLabel, BorderLayout.CENTER);

                        // Bottom: Temps
                        JPanel tempsPanel = new JPanel(new GridLayout(1, 2));
                        tempsPanel.setPreferredSize(new Dimension(cell.getWidth(), 20));

                        double tempMin = (double) weather.get("tempmin");
                        double tempMax = (double) weather.get("tempmax");

                        JLabel lowLabel = new JLabel(String.format("%.1f°C", tempMin));
                        lowLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        lowLabel.setFont(lowLabel.getFont().deriveFont(Font.PLAIN, 12f));

                        JLabel highLabel = new JLabel(String.format("%.1f°C", tempMax));
                        highLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        highLabel.setFont(highLabel.getFont().deriveFont(Font.PLAIN, 12f));

                        tempsPanel.add(lowLabel);
                        tempsPanel.add(highLabel);

                        cell.add(tempsPanel, BorderLayout.SOUTH);
                    }
                } else if (col > 0){
                    LocalDate date = weekDates.get(col - 1);
                    int hour = row - 2;
                    List<Task> cellTasks =
                            taskMap.getOrDefault(date, Collections.emptyMap())
                            .getOrDefault(hour, Collections.emptyList());

                    if (!cellTasks.isEmpty()) {
                        cell.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
                        for (Task t : cellTasks) {
                            JButton button = getJButton(taskClickListener, t);
                            cell.add(button);
                        }
                    }
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                else {
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                    JLabel hourLabel = new JLabel(CalendarData.HOURS_OF_DAY[row - 2]);
                    hourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    cell.add(hourLabel);
                }

                rowPanel.add(cell);
            }

            add(rowPanel);
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

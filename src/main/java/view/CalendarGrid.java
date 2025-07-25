package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarGrid extends JPanel {

    public CalendarGrid(CalendarData data, Map<LocalDate, Map<String, Object>> weatherMap) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        List<LocalDate> weekDates = data.getWeekDates();

        for (int row = 0; row < 26; row++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new GridLayout(1, 8)); // Each row has 8 columns

            // Set custom height for the second row (index 1)
            int rowHeight = (row == 1) ? 80 : 40;
            rowPanel.setPreferredSize(new Dimension(800, rowHeight));

            for (int col = 0; col < 8; col++) {
                JPanel cell = new JPanel();

                if (row == 0) {
                    // Date numbers row
                    cell.setBorder(null); // no border
                    if (col == 0) {
                        cell.add(new JLabel(""));
                    } else {
                        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                        String currDate = String.valueOf(weekDates.get(col - 1).getDayOfMonth());
                        JLabel lbl = new JLabel(currDate);
                        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                        cell.add(lbl);
                    }

                } else if (row == 1) {
                    // Weather header row with icon + day name + temps

                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    if (col == 0) {
                        // Menu button on first column as before
                        cell.add(new JButton("☰"));
                    } else {
                        // For days 1-7, build a weather panel similar to your dayPanel in LoggedInView
                        LocalDate date = weekDates.get(col - 1);
                        Map<String, Object> weather = weatherMap.get(date);

                        cell.setLayout(new BorderLayout());

                        // Top: Day name
                        JLabel dayLabel = new JLabel(date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH));
                        dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD, 14f));
                        cell.add(dayLabel, BorderLayout.NORTH);

                        // Center: icon
                        JLabel iconLabel = new JLabel();
                        if (weather != null && weather.get("icon") instanceof ImageIcon) {
                            iconLabel.setIcon((ImageIcon) weather.get("icon"));
                        }
                        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        cell.add(iconLabel, BorderLayout.CENTER);

                        // Bottom: Low and High temps side by side
                        JPanel tempsPanel = new JPanel(new GridLayout(1, 2));
                        tempsPanel.setPreferredSize(new Dimension(cell.getWidth(), 20));

                        if (weather != null) {
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
                        }

                        cell.add(tempsPanel, BorderLayout.SOUTH);
                    }

                } else {
                    // Other rows: same as before
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    if (col == 0) {
                        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                        JLabel hourLabel = new JLabel(CalendarData.HOURS_OF_DAY[row - 2]);
                        hourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        cell.add(hourLabel);
                    }
                }

                rowPanel.add(cell);
            }

            add(rowPanel);
        }
    }
}

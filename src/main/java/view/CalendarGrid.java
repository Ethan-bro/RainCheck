package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarGrid extends JPanel {

    public CalendarGrid(CalendarData data, Map<LocalDate, Map<String, Object>> weatherMap) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        List<LocalDate> weekDates = data.getWeekDates();

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
                        cell.add(new JButton("☰"));
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
                } else {
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

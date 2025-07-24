package view;

import javax.swing.*;
import java.awt.*;

public class CalendarGrid extends JPanel {

    public CalendarGrid(CalendarData data) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (int row = 0; row < 26; row++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new GridLayout(1, 8)); // Each row has 8 columns

            // Set custom height for the second row
            int rowHeight = (row == 1) ? 80 : 40;
            rowPanel.setPreferredSize(new Dimension(800, rowHeight));

            for (int col = 0; col < 8; col++) {
                JPanel cell = new JPanel();

                // Make the top row borderless
                if (row == 0) {
                    cell.setBorder(null); // no border

                    if (col == 0) {
                        cell.add(new JLabel(""));
                    } else {
                        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                        String currDate = String.valueOf(data.getWeekDates().get(col - 1).getDayOfMonth());
                        JLabel lbl = new JLabel(currDate);
                        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                        cell.add(lbl);
                    }
                } else if (row == 1) {
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                     if (col == 0) {
                         cell.add(new JButton("☰"));
                     } else {
                         cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                         JLabel dayLabel = new JLabel(data.DAYS_OF_WEEK[col - 1]);
                         dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                         cell.add(dayLabel);
                     }
                } else {
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    if (col == 0) {
                        cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                        JLabel hourLabel = new JLabel(data.HOURS_OF_DAY[row - 2]);
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


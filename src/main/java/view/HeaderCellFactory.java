package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

/**
 * Utility to build one “day header” cell: name + date + icon + temps.
 */
public class HeaderCellFactory {
    public static JPanel makeHeader(LocalDate date, Map<String,Object> weather) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        String dayName = date.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        String title = dayName + " the " + getOrdinal(date.getDayOfMonth());
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
        header.add(lbl, BorderLayout.NORTH);

        JLabel icon = new JLabel();
        if (weather != null && weather.get("icon") != null) {
            icon.setIcon((ImageIcon) weather.get("icon"));
        }
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(icon, BorderLayout.CENTER);

        JPanel temps = new JPanel(new GridLayout(1,2));
        temps.setBackground(Color.WHITE);
        temps.setPreferredSize(new Dimension(0,20));
        if (weather != null) {
            temps.add(new JLabel(
                    String.format("%.1f°C",(double)weather.get("tempmin")),
                    SwingConstants.CENTER));
            temps.add(new JLabel(
                    String.format("%.1f°C",(double)weather.get("tempmax")),
                    SwingConstants.CENTER));
        }
        header.add(temps, BorderLayout.SOUTH);

        return header;
    }

    private static String getOrdinal(int n) {
        if (n>=11 && n<=13) return n+"th";
        return switch(n%10) {
            case 1->n+"st"; case 2->n+"nd"; case 3->n+"rd"; default->n+"th";
        };
    }
}
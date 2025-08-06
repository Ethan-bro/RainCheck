package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

/**
 * Utility to build one “day header” cell: day name + date + weather icon + temps,
 * styled with lighter borders, padding, and hover effect.
 */
public class HeaderCellFactory {

    public static JPanel makeHeader(LocalDate date, Map<String,Object> weather) {
        JPanel header = new JPanel(new BorderLayout(5, 5));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC)), // light gray border
                BorderFactory.createEmptyBorder(8, 8, 8, 8)          // padding inside
        ));

        // Day name + ordinal date label
        String dayName = date.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        String title = dayName + " the " + getOrdinal(date.getDayOfMonth());
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.add(lbl, BorderLayout.NORTH);

        // Weather icon
        JLabel icon = new JLabel();
        if (weather != null && weather.get("icon") instanceof ImageIcon) {
            icon.setIcon((ImageIcon) weather.get("icon"));
        }
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(icon, BorderLayout.CENTER);

        // Temperature panel
        JPanel temps = new JPanel(new GridLayout(1, 2, 5, 0));
        temps.setBackground(Color.WHITE);
        temps.setPreferredSize(new Dimension(0, 22));
        if (weather != null) {
            temps.add(makeTempLabel(weather.get("tempmin")));
            temps.add(makeTempLabel(weather.get("tempmax")));
        }
        header.add(temps, BorderLayout.SOUTH);

        // Add subtle hover effect to highlight header
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                header.setBackground(new Color(0xF0F8FF)); // light blue highlight
                header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                header.setBackground(Color.WHITE);
                header.setCursor(Cursor.getDefaultCursor());
            }
        });

        return header;
    }

    private static JLabel makeTempLabel(Object tempObj) {
        String text = tempObj instanceof Number
                ? String.format("%.1f°C", ((Number) tempObj).doubleValue())
                : "--";
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(0x555555));
        return lbl;
    }

    private static String getOrdinal(int n) {
        if (n >= 11 && n <= 13) return n + "th";
        return switch (n % 10) {
            case 1 -> n + "st";
            case 2 -> n + "nd";
            case 3 -> n + "rd";
            default -> n + "th";
        };
    }
}

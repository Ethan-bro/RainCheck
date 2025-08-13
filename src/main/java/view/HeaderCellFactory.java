package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Utility to build one “day header” cell: day name + date + weather icon + temps,
 * styled with lighter borders, padding, and hover effect.
 */
public final class HeaderCellFactory {

    private static final int BORDER_THICKNESS = 1;
    private static final int PADDING = 8;
    private static final int FONT_SIZE_TITLE = 14;
    private static final int FONT_SIZE_TEMP = 12;
    private static final int TEMP_PANEL_HEIGHT = 22;
    private static final int TEMP_PANEL_GAP = 5;
    private static final int GRID_ROWS = 1;
    private static final int GRID_COLS = 2;
    private static final int BORDER_ARGB_GRAY = 0xCCCCCC;
    private static final int COLOR_TEMP_TEXT = 0x555555;
    private static final int COLOR_HOVER_BG = 0xF0F8FF;

    private static final int ORDINAL_TEEN_START = 11;
    private static final int ORDINAL_TEEN_END = 13;
    private static final int ORDINAL_MODULO_BASE = 10;
    private static final int ORDINAL_ST = 1;
    private static final int ORDINAL_ND = 2;
    private static final int ORDINAL_RD = 3;

    /**
     * Private constructor to prevent instantiation.
     */
    private HeaderCellFactory() {
        // hiding the public empty constructor
    }
    
    /**
     * Creates a JPanel representing a header cell with date info and weather.
     *
     * @param date    the LocalDate to display
     * @param weather a map containing weather info: expects keys "icon" (ImageIcon),
     *                "tempmin" and "tempmax"
     * @return a JPanel configured with day name, date, weather icon, and temps
     */

    public static JPanel makeHeader(final LocalDate date, final Map<String, Object> weather) {
        final JPanel header = new JPanel(new BorderLayout(TEMP_PANEL_GAP, TEMP_PANEL_GAP));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(BORDER_ARGB_GRAY), BORDER_THICKNESS),
                BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
        ));

        final String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        final String title = dayName + " the " + getOrdinal(date.getDayOfMonth());
        final JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, FONT_SIZE_TITLE));
        header.add(lbl, BorderLayout.NORTH);

        final JLabel icon = new JLabel();
        if (weather != null) {
            final Object iconObj = weather.get("icon");
            if (iconObj instanceof ImageIcon) {
                icon.setIcon((ImageIcon) iconObj);
            }
        }
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(icon, BorderLayout.CENTER);

        final JPanel temps = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, TEMP_PANEL_GAP, 0));
        temps.setBackground(Color.WHITE);
        temps.setPreferredSize(new Dimension(0, TEMP_PANEL_HEIGHT));
        if (weather != null) {
            temps.add(makeTempLabel(weather.get("tempmin")));
            temps.add(makeTempLabel(weather.get("tempmax")));
        }
        header.add(temps, BorderLayout.SOUTH);

        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                header.setBackground(new Color(COLOR_HOVER_BG));
                header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                header.setBackground(Color.WHITE);
                header.setCursor(Cursor.getDefaultCursor());
            }
        });

        return header;
    }

    /**
     * Creates a JLabel for displaying a temperature value.
     * @param tempObj the temperature value (Number or null)
     * @return a JLabel with formatted temperature or placeholder
     */
    private static JLabel makeTempLabel(final Object tempObj) {
        final String text;
        if (tempObj instanceof Number) {
            text = String.format("%.1f°C", ((Number) tempObj).doubleValue());
        }
        else {
            text = "--";
        }
        final JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, FONT_SIZE_TEMP));
        lbl.setForeground(new Color(COLOR_TEMP_TEXT));
        return lbl;
    }

    /**
     * Returns the ordinal string for a given day of the month (e.g., 1st, 2nd, 3rd, 4th).
     * @param dayOfMonth the day of the month
     * @return the ordinal string
     */
    private static String getOrdinal(final int dayOfMonth) {
        final String suffix;
        if (dayOfMonth >= ORDINAL_TEEN_START && dayOfMonth <= ORDINAL_TEEN_END) {
            suffix = "th";
        }
        else {
            switch (dayOfMonth % ORDINAL_MODULO_BASE) {
                case ORDINAL_ST -> suffix = "st";
                case ORDINAL_ND -> suffix = "nd";
                case ORDINAL_RD -> suffix = "rd";
                default -> suffix = "th";
            }
        }
        return dayOfMonth + suffix;
    }
}

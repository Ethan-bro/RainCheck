package view;

import javax.swing.*;
import java.awt.*;

/**
 * Left-hand hour labels aligned to the same quarter-hour grid used by TaskGridPane,
 * with background colors shifting from sunrise to night.
 */
public class HourLabelPane extends JLayeredPane {
    private static final int QUARTERS_PER_HOUR = 4;
    private static final int TOTAL_QUARTERS = 24 * QUARTERS_PER_HOUR;

    // Define start and end colors for the gradient (sunrise yellow to night blue)
    private static final Color COLOR_START = new Color(0xFFF3B0); // pale yellow morning
    private static final Color COLOR_MID = new Color(0xFFFFFF);   // white midday
    private static final Color COLOR_END = new Color(0x1A237E);   // deep blue night

    public HourLabelPane(int labelColumnWidth) {
        setLayout(null);
        setPreferredSize(new Dimension(labelColumnWidth, TOTAL_QUARTERS * 20));

        for (int hr = 0; hr < 24; hr++) {
            JLabel lbl = new JLabel(CalendarData.HOURS_OF_DAY[hr], SwingConstants.CENTER);
            lbl.putClientProperty("hourIndex", hr);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setOpaque(true);
            add(lbl, JLayeredPane.DEFAULT_LAYER);
        }
    }

    @Override
    public void doLayout() {
        int H = getHeight();
        int cellH = H / TOTAL_QUARTERS;
        int w = getWidth();

        for (Component c : getComponents()) {
            if (!(c instanceof JLabel)) continue;

            int hr = (int) ((JLabel) c).getClientProperty("hourIndex");
            int y = hr * QUARTERS_PER_HOUR * cellH;
            c.setBounds(0, y, w, cellH);

            // Calculate color for hour hr:
            // Morning 6-12 = gradient from COLOR_START to COLOR_MID
            // Afternoon 12-18 = gradient from COLOR_MID to COLOR_START (lighter)
            // Night 18-6 = gradient from COLOR_START to COLOR_END (darkening)
            Color bg;
            if (hr >= 6 && hr < 12) {
                bg = interpolateColor(COLOR_START, COLOR_MID, (hr - 6) / 6f);
            } else if (hr >= 12 && hr < 18) {
                bg = interpolateColor(COLOR_MID, COLOR_START, (hr - 12) / 6f);
            } else if (hr >= 18) {
                bg = interpolateColor(COLOR_START, COLOR_END, (hr - 18) / 6f);
            } else { // hr < 6 (midnight to 6am)
                bg = interpolateColor(COLOR_END, COLOR_START, hr / 6f);
            }
            c.setBackground(bg);
            ((JLabel)c).setForeground(isDark(bg) ? Color.WHITE : Color.BLACK);
        }
    }

    // Linearly interpolate between two colors (t in [0,1])
    private static Color interpolateColor(Color c1, Color c2, float t) {
        int r = (int) (c1.getRed() + t * (c2.getRed() - c1.getRed()));
        int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
        return new Color(r, g, b);
    }

    // Determine if a color is "dark" for text contrast
    private static boolean isDark(Color c) {
        // Calculate luminance per ITU-R BT.709
        double luminance = (0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue()) / 255;
        return luminance < 0.5;
    }
}

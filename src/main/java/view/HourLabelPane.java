package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

/**
 * Left-hand hour labels aligned to the same quarter-hour grid used by TaskGridPane,
 * with background colors shifting from sunrise to night.
 */
public class HourLabelPane extends JLayeredPane {

    private static final int QUARTERS_PER_HOUR = 4;
    private static final int HOURS_PER_DAY = 24;
    private static final int TOTAL_QUARTERS = HOURS_PER_DAY * QUARTERS_PER_HOUR;
    private static final int LABEL_HEIGHT_PER_QUARTER = 20;
    private static final int FONT_SIZE = 12;

    private static final Color COLOR_START = new Color(0xFFF3B0);
    private static final Color COLOR_MID = new Color(0xFFFFFF);
    private static final Color COLOR_END = new Color(0x1A237E);

    private static final int MORNING_START_HOUR = 6;
    private static final int MIDDAY_HOUR = 12;
    private static final int AFTERNOON_END_HOUR = 18;
    private static final float GRADIENT_DIVISOR = 6f;

    private static final double LUMINANCE_RED_COEFF = 0.2126;
    private static final double LUMINANCE_GREEN_COEFF = 0.7152;
    private static final double LUMINANCE_BLUE_COEFF = 0.0722;
    private static final double LUMINANCE_DIVISOR = 255.0;
    private static final double LUMINANCE_THRESHOLD = 0.5;

    public HourLabelPane(final int labelColumnWidth) {
        setLayout(null);
        setPreferredSize(new Dimension(labelColumnWidth, TOTAL_QUARTERS * LABEL_HEIGHT_PER_QUARTER));

        for (int hr = 0; hr < HOURS_PER_DAY; hr++) {
            final JLabel label = new JLabel(CalendarData.HOURS_OF_DAY[hr], SwingConstants.CENTER);
            label.putClientProperty("hourIndex", hr);
            label.setFont(new Font("Segoe UI", Font.PLAIN, FONT_SIZE));
            label.setOpaque(true);
            add(label, JLayeredPane.DEFAULT_LAYER);
        }
    }

    @Override
    public void doLayout() {
        final int height = getHeight();
        final int cellHeight = height / TOTAL_QUARTERS;
        final int width = getWidth();

        for (final Component component : getComponents()) {
            if (!(component instanceof JLabel)) {
                continue;
            }
            final JLabel label = (JLabel) component;
            final int hour = (int) label.getClientProperty("hourIndex");
            final int yPosition = hour * QUARTERS_PER_HOUR * cellHeight;
            label.setBounds(0, yPosition, width, cellHeight);

            final Color backgroundColor;
            if (hour >= MORNING_START_HOUR && hour < MIDDAY_HOUR) {
                backgroundColor = interpolateColor(
                        COLOR_START,
                        COLOR_MID,
                        (hour - MORNING_START_HOUR) / GRADIENT_DIVISOR
                );
            }
            else if (hour >= MIDDAY_HOUR && hour < AFTERNOON_END_HOUR) {
                backgroundColor = interpolateColor(
                        COLOR_MID,
                        COLOR_START,
                        (hour - MIDDAY_HOUR) / GRADIENT_DIVISOR
                );
            }
            else if (hour >= AFTERNOON_END_HOUR) {
                backgroundColor = interpolateColor(
                        COLOR_START,
                        COLOR_END,
                        (hour - AFTERNOON_END_HOUR) / GRADIENT_DIVISOR
                );
            }
            else {
                backgroundColor = interpolateColor(COLOR_END, COLOR_START, hour / GRADIENT_DIVISOR);
            }
            label.setBackground(backgroundColor);

            if (isDark(backgroundColor)) {
                label.setForeground(Color.WHITE);
            }
            else {
                label.setForeground(Color.BLACK);
            }
        }
    }

    /**
     * Linearly interpolate between two colors (t in [0,1]).
     *
     * @param startColor starting color
     * @param endColor   ending color
     * @param fraction   interpolation fraction
     * @return interpolated Color
     */
    private static Color interpolateColor(final Color startColor, final Color endColor, final float fraction) {
        final int red = (int) (startColor.getRed() + fraction * (endColor.getRed() - startColor.getRed()));
        final int green = (int) (startColor.getGreen() + fraction * (endColor.getGreen() - startColor.getGreen()));
        final int blue = (int) (startColor.getBlue() + fraction * (endColor.getBlue() - startColor.getBlue()));
        return new Color(red, green, blue);
    }

    /**
     * Determine if a color is "dark" for text contrast.
     *
     * @param color color to test
     * @return true if dark, false otherwise
     */
    private static boolean isDark(final Color color) {
        final double luminance = (LUMINANCE_RED_COEFF * color.getRed()
                + LUMINANCE_GREEN_COEFF * color.getGreen()
                + LUMINANCE_BLUE_COEFF * color.getBlue()) / LUMINANCE_DIVISOR;
        return luminance < LUMINANCE_THRESHOLD;
    }
}

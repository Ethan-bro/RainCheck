package view;

import javax.swing.*;
import java.awt.*;

/**
 * Left‐hand hour labels, aligned to the same quarter‐hour grid
 * used by TaskGridPane.
 */
public class HourLabelPane extends JLayeredPane {
    private static final int QUARTERS_PER_HOUR = 4;
    private static final int TOTAL_QUARTERS     = 24 * QUARTERS_PER_HOUR;

    public HourLabelPane(int labelColumnWidth) {
        setLayout(null);
        setPreferredSize(new Dimension(labelColumnWidth, TOTAL_QUARTERS * 20));

        // Create & add 24 labels
        for (int hr = 0; hr < 24; hr++) {
            JLabel lbl = new JLabel(CalendarData.HOURS_OF_DAY[hr], SwingConstants.CENTER);
            lbl.putClientProperty("hourIndex", hr);
            add(lbl, JLayeredPane.DEFAULT_LAYER);
        }
    }

    @Override
    public void doLayout() {
        // Called whenever pane is resized
        int H = getHeight();
        int cellH = H / TOTAL_QUARTERS;
        int w = getWidth();
        for (Component c : getComponents()) {
            if (!(c instanceof JLabel)) continue;
            int hr = (int)((JLabel)c).getClientProperty("hourIndex");
            int y  = hr * QUARTERS_PER_HOUR * cellH;
            c.setBounds(0, y, w, cellH);
        }
    }
}
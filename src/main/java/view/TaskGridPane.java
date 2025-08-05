package view;

import interface_adapter.calendar.TaskClickListener;
import entity.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The “body” of the calendar: draws hourly grid lines in paintComponent,
 * then absolutely positions task‐buttons snapped to quarter‐hour rows.
 */
public class TaskGridPane extends JLayeredPane {
    private static final int QUARTERS_PER_HOUR = 4;
    private static final int TOTAL_QUARTERS     = 24 * QUARTERS_PER_HOUR;

    private final List<Task> tasks;
    private final TaskClickListener listener;

    public TaskGridPane(
            Dimension preferredSize,
            List<Task> tasks,
            TaskClickListener listener
    ) {
        this.tasks    = tasks;
        this.listener = listener;
        setLayout(null);
        setPreferredSize(preferredSize);

        // create buttons up front
        for (Task t : tasks) {
            JButton b = makeTaskButton(t);
            add(b, JLayeredPane.PALETTE_LAYER);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int W = getWidth(), H = getHeight();
        int cols = 7, rows = TOTAL_QUARTERS;
        int cellW = W / cols;
        int cellH = H / rows;

        g.setColor(Color.LIGHT_GRAY);
        // vertical lines
        for (int c = 0; c <= cols; c++) {
            int x = c * cellW;
            g.drawLine(x, 0, x, H);
        }
        // horizontal at each hour
        g.setColor(Color.BLACK);
        for (int q = 0; q <= rows; q += QUARTERS_PER_HOUR) {
            int y = q * cellH;
            g.drawLine(0, y, W, y);
        }

        // now position buttons
        Component[] comps = getComponentsInLayer(JLayeredPane.PALETTE_LAYER);
        for (Component comp : comps) {
            if (!(comp instanceof JButton)) continue;
            JButton b = (JButton) comp;
            Task t = (Task) b.getClientProperty("task");
            LocalDateTime s = t.getTaskInfo().getStartDateTime();
            LocalDateTime e = t.getTaskInfo().getEndDateTime();
            int startQ = s.getHour() * QUARTERS_PER_HOUR + (s.getMinute() + 7) / 15;
            int endQ   = e.getHour() * QUARTERS_PER_HOUR + (e.getMinute() + 7) / 15;
            int span   = Math.max(1, endQ - startQ);

            int col = (s.getDayOfWeek().getValue() % 7);
            int row = startQ;

            int x = col * cellW;
            int y = row * cellH;
            int w = cellW;
            int h = span * cellH;

            b.setBounds(x, y, w, h);
        }
    }

    private JButton makeTaskButton(Task t) {
        String txt = t.getTaskInfo().getTaskName();
        if (t.getTaskInfo().getTag() != null) {
            txt += " " + t.getTaskInfo().getTag().getTagEmoji();
        }
        JButton b = new JButton(txt);
        b.putClientProperty("task", t);
        b.setMargin(new Insets(2,4,2,4));
        b.setFont(b.getFont().deriveFont(12f));
        b.setOpaque(true);
        b.setBorderPainted(false);
        if ("Incomplete".equals(t.getTaskInfo().getTaskStatus())) {
            switch (t.getTaskInfo().getPriority()) {
                case HIGH   -> b.setBackground(Color.RED);
                case MEDIUM -> b.setBackground(Color.ORANGE);
                case LOW    -> b.setBackground(Color.YELLOW);
            }
        } else {
            b.setBackground(Color.LIGHT_GRAY);
        }
        b.addActionListener(e -> listener.onTaskClick(t));
        return b;
    }
}

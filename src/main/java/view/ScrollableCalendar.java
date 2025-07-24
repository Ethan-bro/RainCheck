package view;

import javax.swing.*;
import java.awt.*;

public class ScrollableCalendar extends JPanel {

    private final JScrollPane scrollPane;

    public ScrollableCalendar(CalendarGrid calendar) {
        setLayout(new BorderLayout());
        scrollPane = new JScrollPane(calendar);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        add(scrollPane, BorderLayout.CENTER);
    }
}


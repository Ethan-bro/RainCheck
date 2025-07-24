package view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    public StyledButton(String text, String icon) {
        super(icon + " " + text);
        setupStyling();
        addHoverEffect();
    }

    private void setupStyling() {
        setFont(new Font("Arial", Font.PLAIN, 11));
        setForeground(Color.WHITE);
        setBackground(new Color(0, 0, 0, 100));
        setFocusPainted(false);
        setBorderPainted(false);
    }

    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(255, 255, 255, 50));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(0, 0, 0, 100));
            }
        });
    }
}
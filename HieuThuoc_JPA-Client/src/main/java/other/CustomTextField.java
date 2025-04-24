package other;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CustomTextField extends JTextField implements FocusListener {
    private Color borderColor;
    private Color colorTransparent = new Color(0, 0, 0, 0);

    public CustomTextField(int columns) {
        super(columns);
        setUp();
    }

    public CustomTextField() {
        super();
        setUp();
    }

    private void setUp() {
        setOpaque(false);
        setBorder(null);
        borderColor = colorTransparent;
        setBorder(new EmptyBorder(0, 5, 0, 5));
        setSelectionColor(Color.BLUE);
        addFocusListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        if(isEnabled()) {
//            g2d.setColor(borderColor);
//            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
//
//            g2d.setColor(Color.WHITE);
//            g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
//        } else {
//            g2d.setColor(Color.LIGHT_GRAY);
//            g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
//        }
        g2d.setColor(borderColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);

        super.paintComponent(g);
    }

    @Override
    public void focusGained(FocusEvent e) {
        borderColor = getSelectionColor();
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        borderColor = colorTransparent;
        repaint();
    }
}

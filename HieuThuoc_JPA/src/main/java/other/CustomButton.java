package other;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomButton extends JButton implements MouseListener {
    private Color backgroundColor;
    private Color originalBackgroundColor;
    private Color selectedBackgroundColor;

    public CustomButton(String text, Icon icon) {
        super(text, icon);
        setUp();
    }

    public CustomButton(Action a) {
        super(a);
        setUp();
    }

    public CustomButton(String text) {
        super(text);
        setUp();
    }

    public CustomButton(Icon icon) {
        super(icon);
        setUp();
    }

    private void setUp() {
        setOpaque(false);
        setBorder(null);
        setBackground(new Color(153, 204, 255, 100));
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
    }

    @Override
    public void setBackground(Color bg) {
        if (bg == null)
            this.backgroundColor = UIManager.getColor("Button.background");
        else {
            this.backgroundColor = bg;
        }
    }

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        originalBackgroundColor = backgroundColor;
        setBackground(selectedBackgroundColor == null ? originalBackgroundColor.darker() : selectedBackgroundColor);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setBackground(originalBackgroundColor);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

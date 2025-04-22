package other;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusListener;
import java.util.Vector;

public class CustomComboBox<T> extends JComboBox<T> {
    public CustomComboBox(ComboBoxModel<T> aModel) {
        super(aModel);
        setUp();
    }

    public CustomComboBox() {
        setUp();
    }

    public CustomComboBox(Vector<T> items) {
        super(items);
        setUp();
    }

    public CustomComboBox(T[] items) {
        super(items);
        setUp();
    }

    private void setUp() {
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                CustomButton c = new CustomButton("▼");
                c.setBackground(Color.BLUE);
                c.setForeground(Color.WHITE);
                return c;
            }
        });
        setBackground(Color.WHITE);
        setOpaque(false);
        setBorder(new EmptyBorder(0, 10, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g);
    }
}

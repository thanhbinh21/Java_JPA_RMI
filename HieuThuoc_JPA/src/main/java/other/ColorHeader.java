package other;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ColorHeader extends DefaultTableCellRenderer {
    private TextAreaForTableHeader textArea;
    private int headerHeight = 0;

    public void setColCount(int colCount) {
        textArea.setColCount(colCount);
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
        textArea.setHeaderHeight(headerHeight);
    }

    public void setHeaderBackground(Color color) {
        textArea.setBackground(color);
    }

    public ColorHeader() {
        textArea = new TextAreaForTableHeader();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String[] values = ((String) value).split("\n");
        textArea.setText(null);
        textArea.setCol(column);
        for(int i = 0; i < values.length; i++)
            textArea.append("  " + values[i] + "\n");
        if(headerHeight < textArea.getFont().getSize() * values.length + (values.length) * 4 + 2)
            headerHeight = textArea.getFont().getSize() * values.length + (values.length) * 4 + 2;
        textArea.setPreferredSize(new Dimension(0 , headerHeight));//Mỗi font + thêm 4  (values.length) * 4 + 2
        return textArea;
    }
}

class TextAreaForTableHeader extends JTextArea {
    private int col = 0;
    private int colCount = 1;

    public void setCol(int col) {
        this.col = col;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public void setHeaderHeight(int headerHeight) {
        setBorder(BorderFactory.createEmptyBorder((headerHeight + 5) / 2 - getFont().getSize(), 0, 0, 0));
    }

    public TextAreaForTableHeader() {
        setUp();
    }

    private void setUp() {
        setBackground(new Color(153, 204, 255, 200));
        setBorder(BorderFactory.createEmptyBorder(25 / 2 - getFont().getSize(), 0, 0, 0));
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintTextArea(g);
        super.paintComponent(g);
    }

    private void paintTextArea(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        double borderRadius = 15;
        Area area = new Area();

        if(colCount == 1) {
            area.add(new Area(new Arc2D.Double(0, 0, borderRadius * 2, borderRadius * 2, 90, 90, Arc2D.PIE)));
            area.add(new Area(new Rectangle2D.Double(0, borderRadius, getWidth(), getHeight() - borderRadius)));
            area.add(new Area(new Arc2D.Double(getWidth() - borderRadius * 2, 0, borderRadius * 2, borderRadius * 2, 0, 90, Arc2D.PIE)));
            area.add(new Area(new Rectangle2D.Double(borderRadius, 0, getWidth() - borderRadius * 2, getHeight())));
            g2d.fill(area);
            return;
        }
        if(col == 0) {
            area.add(new Area(new Arc2D.Double(0, 0, borderRadius * 2, borderRadius * 2, 90, 90, Arc2D.PIE)));
            area.add(new Area(new Rectangle2D.Double(0, borderRadius, getWidth(), getHeight() - borderRadius)));
            area.add(new Area(new Rectangle2D.Double(borderRadius, 0, getWidth() - borderRadius, getHeight())));
            g2d.fill(area);
            return;
        }
        if(col == colCount - 1) {
            area.add(new Area(new Arc2D.Double(getWidth() - borderRadius * 2, 0, borderRadius * 2, borderRadius * 2, 0, 90, Arc2D.PIE)));
            area.add(new Area(new Rectangle2D.Double(0, borderRadius, getWidth(), getHeight() - borderRadius)));
            area.add(new Area(new Rectangle2D.Double(0, 0, getWidth() - borderRadius, getHeight())));
            g2d.fill(area);
            return;
        }
        if(col > 0 && col < colCount - 1) {
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

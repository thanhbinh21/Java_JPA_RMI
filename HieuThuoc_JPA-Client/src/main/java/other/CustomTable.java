package other;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

public class CustomTable extends JTable {
    private ColorHeader colorHeader;

    public CustomTable() {
        setUp();
    }

    public CustomTable(TableModel dm) {
        super(dm);
        setUp();
        if(colorHeader != null)
            colorHeader.setColCount(dataModel.getColumnCount());
    }

    public CustomTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        setUp();
        if(colorHeader != null)
            colorHeader.setColCount(dataModel.getColumnCount());
    }

    public CustomTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        setUp();
        if(colorHeader != null)
            colorHeader.setColCount(dataModel.getColumnCount());
    }

    public CustomTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        setUp();
    }

    public CustomTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
        setUp();
    }

    public CustomTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        setUp();
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        if(colorHeader != null)
            colorHeader.setColCount(dataModel.getColumnCount());
    }

    private void setUp() {
        colorHeader = new ColorHeader();
        getTableHeader().setDefaultRenderer(colorHeader);
        setShowGrid(false);
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) c).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
                return c;
            }
        });
    }

    public void setHeaderBackground(Color color) {
        colorHeader.setHeaderBackground(color);
    }

    public void setHeaderHeight(int height) {
        colorHeader.setHeaderHeight(height);
    }
}

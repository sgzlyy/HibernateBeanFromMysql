package cn.com.spbun.nddd.gui.common.table;

import javax.swing.table.DefaultTableModel;

public class TableModelExt extends DefaultTableModel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5119478314723209936L;

    private boolean isCellEditable = true;

    public TableModelExt(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public void setCellEditable(boolean flag) {
        this.isCellEditable = flag;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return isCellEditable;
    }
}

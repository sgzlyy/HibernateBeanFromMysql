package cn.com.spbun.nddd.gui.common.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7808156776882397663L;

    public TableCellTextAreaRenderer() {
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        int maxPreferredHeight = 0;

        for (int i = 0; i < table.getColumnCount(); i++) {
            setText("" + table.getValueAt(row, i));
            setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
            maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
        }

        if (table.getRowHeight(row) != maxPreferredHeight) {
            table.setRowHeight(row, maxPreferredHeight);
        }

        setText(value == null ? "" : value.toString());

        if (isSelected) {
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setBackground(Color.WHITE);
        }

        return this;
    }

}

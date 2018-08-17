package cn.com.sby.common.ui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import cn.com.sby.common.SwingUtil;
import cn.com.sby.common.ui.UIConstants;

/**
 * 表格的工具类
 * 
 *
 *
 */
public class TableUtil {

    public static void setTableHeaderStyle(JTable table) {

        JTableHeader tableHeader = table.getTableHeader();

        int columnCount = tableHeader.getColumnModel().getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            setTableHeaderColor(table, i, UIConstants.TABLE_TITLE_BACKGROUND);
        }

        Dimension d = tableHeader.getPreferredSize();
        d.height = 30;
        tableHeader.setPreferredSize(d);
    }

    private static void setTableHeaderColor(JTable table, int columnIndex, final Color c) {

        TableColumn column = table.getTableHeader().getColumnModel().getColumn(columnIndex);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {

            /**
             * serialVersionUID
             */
            private static final long serialVersionUID = -3649068911233089236L;

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);

                comp.setFont(SwingUtil.FONT_SIMSUN_B);

                comp.setHorizontalAlignment(JLabel.CENTER);
                comp.setBorder(BorderFactory.createRaisedBevelBorder());
                comp.setBackground(c);
                return comp;
            }
        };

        column.setHeaderRenderer(cellRenderer);
    }
}

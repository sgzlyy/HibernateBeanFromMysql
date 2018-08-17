package cn.com.sby.common.ui.ext;

import java.awt.Color;

import javax.swing.JTable;

public class JTableExt extends JTable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 9172069527581776239L;

    public JTableExt() {
        super();
        doCustom();
    }

    private void doCustom() {

        this.getTableHeader();

        this.setBackground(new Color(57, 66, 100));// 设置背景颜色
        this.setForeground(Color.WHITE);// 设置字体颜色，但标题不会改变
        this.setSelectionBackground(new Color(80, 89, 123));// 设置选择行的颜色
        this.setSelectionForeground(Color.WHITE);

        // this.addMouseMotionListener(this);
    }

    // @Override
    // public void mouseDragged(MouseEvent e) {
    //
    // }
    //
    // @Override
    // public void mouseMoved(MouseEvent e) {
    //
    // int rowUnderMouse = -1;
    //
    // Point p = this.getMousePosition();
    //
    // if (p != null) {
    //
    // rowUnderMouse = rowAtPoint(p);
    //
    // this.setRowSelectionInterval(rowUnderMouse, rowUnderMouse);
    // }
    // }

}

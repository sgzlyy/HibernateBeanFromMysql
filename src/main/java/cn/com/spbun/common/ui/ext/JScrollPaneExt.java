package cn.com.spbun.common.ui.ext;

import java.awt.Color;

import javax.swing.JScrollPane;

public class JScrollPaneExt extends JScrollPane {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7508288935420203690L;

    public JScrollPaneExt() {
        super();
        doCustom();
    }

    private void doCustom() {
        this.getViewport().setBackground(new Color(57, 66, 100));// 设置背景颜色
    }
}

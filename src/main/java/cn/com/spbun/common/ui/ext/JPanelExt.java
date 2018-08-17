package cn.com.spbun.common.ui.ext;

import java.awt.Color;

import javax.swing.JPanel;

public class JPanelExt extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2763111643974401517L;

    public JPanelExt() {
        super();

        doCustom();
    }

    private void doCustom() {
        // this.setBackground(new Color(31, 37, 61));
        this.setBackground(new Color(57, 66, 100));
    }
}

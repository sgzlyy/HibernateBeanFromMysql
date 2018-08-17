package cn.com.spbun.common.ui.ext;

import java.awt.Color;

import javax.swing.JLabel;

public class JLabelExt extends JLabel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2124253861346470610L;

    public JLabelExt() {
        this(null);
    }

    public JLabelExt(String text) {
        super(text);
        doCustom();
    }

    private void doCustom() {
        this.setForeground(Color.WHITE);
    }

}

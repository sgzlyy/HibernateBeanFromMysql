package cn.com.spbun.common.ui.ext;

import java.awt.Color;

import javax.swing.JFrame;

public class JFrameExt extends JFrame {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7473412717388086629L;

    public JFrameExt() {
        this(null);
    }

    public JFrameExt(String windowTitle) {
        super(windowTitle);
        doCustom();
    }

    private void doCustom() {
        this.getContentPane().setBackground(new Color(31, 37, 61));
    }
}

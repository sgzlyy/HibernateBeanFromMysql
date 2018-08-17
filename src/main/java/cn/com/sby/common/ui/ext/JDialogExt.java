package cn.com.sby.common.ui.ext;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class JDialogExt extends JDialog {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7077170255213465773L;

    public JDialogExt(JFrame frame, boolean isLock) {
        super(frame, isLock);
        doCustom();
    }

    private void doCustom() {

        this.getContentPane().setBackground(new Color(57, 66, 100));
    }
}

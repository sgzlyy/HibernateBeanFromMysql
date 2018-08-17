package cn.com.sby.common.ui.ext;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class JTextFieldExt extends JTextField implements FocusListener {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 592833485026130899L;

    public JTextFieldExt() {
        this(null);
    }

    public JTextFieldExt(String text) {
        super(text);
        doCustom();
    }

    private void doCustom() {
        this.addFocusListener(this);
        this.setBackground(new Color(80, 89, 123));
        this.setForeground(Color.WHITE);
        this.focusLost(null);
    }

    @Override
    public void focusGained(FocusEvent e) {
        this.setBorder(BorderFactory.createLineBorder(new Color(17, 168, 171)));
    }

    @Override
    public void focusLost(FocusEvent e) {
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}

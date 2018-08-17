package cn.com.sby.common.ui.ext;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class JButtonExt extends JButton implements MouseListener {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5457193462351814053L;

    private static final Color SELECT_COLOR = new Color(15, 146, 149);
    private static final Color UNSELECT_COLOR = new Color(17, 168, 171);

    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(5, 20, 5, 20);

    public JButtonExt() {
        this(null);
    }

    public JButtonExt(String text) {
        super(text);
        doCustom();
    }

    private void doCustom() {
        this.setBorder(EMPTY_BORDER);
        this.setForeground(Color.WHITE);
        this.setOpaque(true);
        this.setFocusable(false);
        this.addMouseListener(this);
        this.setMouseOut();
    }

    private void setMouseIn() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setBackground(SELECT_COLOR);
    }

    private void setMouseOut() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.setBackground(UNSELECT_COLOR);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setMouseIn();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setMouseOut();
    }

}

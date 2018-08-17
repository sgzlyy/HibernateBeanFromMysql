package cn.com.spbun.nddd;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LayerUI;

public class TestPaint extends JPanel {

    private static final int width = 400;

    private static final int height = 400;

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -103504569745772879L;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(null);

                frame.setSize(300, 300);

                TestPaint paint = new TestPaint();
                // frame.add(paint);

                JLayer<JPanel> l = paint.getLayer();
                l.setBounds(0, 0, width, height);

                frame.add(l);

                frame.setVisible(true);
            }
        });
    }

    public TestPaint() {

        super();

        initGUI();
    }

    private void initGUI() {

        // this.setSize(new Dimension(400, 400));
        // this.setLocation(0, 0);
        // this.setLayout(null);

        addReg("123131", 0, 0, 100, 30);
        addReg("我是中国人", 30, 30, 100, 20);

        addReg("123131", 270, 0, 100, 30);
        addReg("我是中国人", 270, 30, 100, 20);

        this.setBorder(BorderFactory.createLineBorder(Color.RED));

        this.setPreferredSize(new Dimension(width, height));

        MouseAdapter ma = new MouseAdapter() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println(e);
                TestPaint.this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println(e);
                TestPaint.this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            }
        };

        this.addMouseListener(ma);
    }

    private void addReg(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        this.add(label);
    }

    private float zoom = 0.5f;

    public JLayer<JPanel> getLayer() {

        LayerUI<JComponent> layerUI = new LayerUI<JComponent>() {

            @Override
            public void paint(Graphics g, JComponent c) {

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.scale(zoom, zoom);
                c.paint(g);

                TestPaint.this.setPreferredSize(new Dimension(200, 200));
            }

            @Override
            public void installUI(JComponent c) {
                super.installUI(c);
                ((JLayer) c).setLayerEventMask(AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
            }

            @Override
            public void uninstallUI(JComponent c) {
                super.uninstallUI(c);
                ((JLayer) c).setLayerEventMask(0);
            }

            // public void eventDispatched(AWTEvent e5, JLayer<? extends
            // JComponent> l) {
            //
            // if (e5 instanceof MouseEvent) {
            //
            // MouseEvent e = (MouseEvent) e5;
            //
            // MouseEvent event = new MouseEvent((Component) e.getSource(),
            // e.getID(), e.getWhen(),
            // e.getModifiers(), e.getX(), e.getY(), e.getXOnScreen(),
            // e.getYOnScreen(),
            // e.getClickCount(), e.isPopupTrigger(), e.getButton());
            //
            // e.consume();
            //
            // super.processMouseEvent(event, l);
            // super.processMouseMotionEvent(event, l);
            //
            // // e.translatePoint((int) (e.getX() / 0.5) - e.getX(), (int)
            // // (e.getY() / 0.5) - e.getY());
            // //
            // // super.eventDispatched(e, l);
            //
            // }
            //
            // }
        };

        JLayer llll = new JLayer(this, layerUI);

        return llll;
    }
}

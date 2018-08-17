package cn.com.spbun.nddd;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.plaf.LayerUI;

public class TestJLayerZoom {

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                final JPanel panel = new JPanel();
                final ZoomUI layerUI = new ZoomUI();

                final JLayer<JComponent> jLayer = new JLayer<JComponent>(panel, layerUI);

                JButton zoomIn = new JButton("Zoom In");
                zoomIn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        layerUI.zoom += 0.1;
                        jLayer.repaint();
                    }
                });
                zoomIn.setBorder(BorderFactory.createEtchedBorder());
                panel.add(zoomIn);

                JButton zoomOut = new JButton("Zoom Out");
                zoomOut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        layerUI.zoom -= 0.1;
                        jLayer.repaint();
                    }
                });
                zoomOut.setBorder(BorderFactory.createEtchedBorder());
                panel.add(zoomOut);

                frame.setPreferredSize(new Dimension(400, 200));
                frame.add(jLayer);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private static class ZoomUI extends LayerUI<JComponent> {

        public double zoom = 1; // Changing this value seems to have no effect

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(zoom, zoom);
            super.paint(g2, c);
            g2.dispose();
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            JLayer jlayer = (JLayer) c;
            jlayer.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.ACTION_EVENT_MASK
                    | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        }

        @Override
        public void uninstallUI(JComponent c) {
            JLayer jlayer = (JLayer) c;
            jlayer.setLayerEventMask(0);
            super.uninstallUI(c);
        }

        @Override
        protected void processMouseEvent(MouseEvent e, JLayer<? extends JComponent> l) {
            super.processMouseEvent(e, l);
            if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                System.out.println("Mouse click on layer: " + e.getPoint());
            }
        }
    }
}
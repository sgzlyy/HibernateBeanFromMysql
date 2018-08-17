package cn.com.sby.hbfm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class JavaApplication1 extends JFrame {

    public static void main(String[] args) {
        new JavaApplication1();
    }

    private MyComponent myComponent = new MyComponent();

    public JavaApplication1() throws HeadlessException {
        this.setSize(400, 400);
        this.add(myComponent);
        this.setVisible(true);
    }

    class MyComponent extends JComponent {

        private int x, y;
        private double scale = 1;
        private MouseAdapter mouseAdapter = new MouseAdapter();
        private AffineTransform transform = new AffineTransform();

        public MyComponent() {
            this.addMouseListener(mouseAdapter);
            this.addMouseWheelListener(mouseAdapter);
            this.addMouseMotionListener(mouseAdapter);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, 400, 400);
            g2.setColor(Color.RED);
            g2.setTransform(transform);

            transform.scale(scale, scale);
            g2.drawString("My String!", x, y);
        }

        private class MouseAdapter implements MouseWheelListener, MouseListener, MouseMotionListener {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() == 1) {
                    scale += 0.1;
                } else {
                    scale -= 0.1;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        }
    }
}
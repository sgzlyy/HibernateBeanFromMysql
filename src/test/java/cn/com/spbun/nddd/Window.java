package cn.com.spbun.nddd;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.plaf.LayerUI;

public class Window extends JFrame {

    private static boolean blockInput = true;
    private CustomCanvas canvas = new CustomCanvas();

    public static void main(String[] args) {
        new Window().setVisible(true);
    }

    public Window() {
        canvas.addMouseListener(canvas);
        LayerUI<Canvas> layerUI = new CanvasLayerUI();
        JLayer<Canvas> canvasLayer = new JLayer<Canvas>(canvas, layerUI);
        add(canvasLayer);
        setSize(800, 600);
    }

    private class CanvasLayerUI extends LayerUI<Canvas> {

        @Override
        public void eventDispatched(AWTEvent e, JLayer<? extends Canvas> l) {
            if (blockInput) {
                if (e instanceof InputEvent) {
                    ((InputEvent) e).consume();
                }
            } else {
                super.eventDispatched(e, l);
            }
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            if (c instanceof JLayer) {
                JLayer<?> layer = (JLayer<?>) c;
                layer.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK);
            }
        }

        @Override
        protected void processMouseEvent(MouseEvent e, JLayer<? extends Canvas> l) {
            super.processMouseEvent(e, l);
            if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                System.out.println("Mouse click on layer: " + e.getPoint());
            }
        }
    }

    private class CustomCanvas extends Canvas implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("Mouse click on canvas: " + e.getPoint());
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
    }
}

package cn.com.sby.hbfm;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Testxxxx {

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                ExamplePanel panel = new ExamplePanel();

                frame.add(panel);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    // I could not get this to with when it extended JLayeredPane
    private static class ExamplePanel extends JPanel {
        private static final int maxX = 500;
        private static final int maxY = 500;
        private static double zoom = 1;
        private static final Circle circle = new Circle(100, 100);

        public ExamplePanel() {
            this.setPreferredSize(new Dimension(maxX, maxY));
            this.setFocusable(true);

            Button zoomIn = new Button("Zoom In");
            zoomIn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoom += 0.1;
                    repaint();
                }
            });
            add(zoomIn);

            Button zoomOut = new Button("Zoom Out");
            zoomOut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoom -= 0.1;
                    repaint();
                }
            });
            add(zoomOut);

            // add(circle); // Comment back in if using JLayeredPane
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.scale(zoom, zoom);
            super.paintComponent(g);

            circle.paint(g); // Comment out if using JLayeredPane
        }

    }

    static class Circle extends JPanel {
        private Color color = Color.RED;
        private final int x;
        private final int y;
        private static final int DIMENSION = 100;

        public Circle(int x, int y) {
            // setBounds(x, y, DIMENSION, DIMENSION);
            this.x = x;
            this.y = y;
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    color = Color.BLUE;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(color);
            g2.fillOval(x, y, DIMENSION, DIMENSION);
        }

        // I had some trouble getting this to work with JLayeredPane even when
        // setting the bounds
        // In the constructor
        // @Override
        // public void paintComponent(Graphics g) {
        // Graphics2D g2 = (Graphics2D) g;
        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        // g2.setPaint(color);
        // g2.fillOval(x, y, DIMENSION, DIMENSION);
        // }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(DIMENSION, DIMENSION);
        }
    }
}

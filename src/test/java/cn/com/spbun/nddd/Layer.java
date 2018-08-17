package cn.com.spbun.nddd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Layer extends JPanel {

    private static final long serialVersionUID = 1L;
    
    ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().createImage("D:/rose.jpg"));

    // 以下两个参数描述图层的位置
    private int x = 0;//
    private int y = 0;
    // 以下两个参数描述图层的大小
    private int width = 300;
    private int height = 300;

    // 以下两个参数描述图层的每次放大或缩小的尺寸
    private int dx = 50;
    private int dy = 50;

    public Layer() {
        this.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    zoom();
                } else {
                    reduce();
                }

            }
        });

        MouseAdapter ma = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                zoom();
            }

            boolean moveEnable = false;
            Point point1 = null;
            Point point2 = null;

            @Override
            public void mousePressed(MouseEvent e) {
                moveEnable = true;
                point1 = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                moveEnable = false;
                point1 = null;
                point2 = null;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // System.out.println("move");
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("dragged");
                point2 = e.getPoint();
                if (moveEnable) {
                    if (point1 != null && point2 != null) {
                        int dx = point2.x - point1.x;
                        int dy = point2.y - point1.y;
                        x = x + dx;
                        y = y + dy;
                        // Layer.this.setLocation(_x, _y);
                        point1 = point2;
                        repaint();
                    }
                }
            }
        };

        this.addMouseMotionListener(ma);
        this.addMouseListener(ma);
    }

    @Override
    public void paint(Graphics g) {
        // 所有的图层变更都在此方法内响应
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getBounds().width, getBounds().height);
        g2.drawImage(img.getImage(), x, y, width, height, null);

    }

    /**
     * 缩小
     */
    public void reduce() {
        if (width > 2 * dx && height > 2 * dy) {
            x += dx;
            y += dy;
            width -= 2 * dx;
            height -= 2 * dy;
            super.repaint();
        }

    }

    /**
     * 放大
     */
    public void zoom() {
        x -= dx;
        y -= dy;
        width += 2 * dx;
        height += 2 * dy;
        super.repaint();
    }

    /**
     * 测试方法
     * 
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        f.setSize(500, 500);

        final Layer layer = new Layer();
        layer.setBorder(new EmptyBorder(2, 2, 2, 2));
        layer.setOpaque(true);
        layer.setBackground(Color.BLUE);
        layer.setSize(400, 400);
        f.getContentPane().add(layer);

        JButton btn1 = new JButton("放大");
        btn1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                layer.zoom();
            }
        });

        JButton btn2 = new JButton("缩小");
        btn2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                layer.reduce();
            }
        });
        f.add(btn1, BorderLayout.NORTH);
        f.add(btn2, BorderLayout.SOUTH);

        f.setVisible(true);
    }

}
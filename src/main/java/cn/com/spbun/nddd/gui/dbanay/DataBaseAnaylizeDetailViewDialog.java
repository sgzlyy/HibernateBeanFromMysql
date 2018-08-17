package cn.com.spbun.nddd.gui.dbanay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import cn.com.spbun.nddd.model.DBConfigItem;

public class DataBaseAnaylizeDetailViewDialog extends JDialog {

    public static void main(String[] args) {
        new DataBaseAnaylizeDetailViewDialog(null,
                new DBConfigItem("back_log", "65535",
                        "指定MySQL可能的连接数量。当MySQL主线程在很短的时间内得到非常多的连接请求，该参数就起作用，之后主线程花些时间(尽管很短)检查连接并且启动一个新线程。\r\nback_log参数的值指出在MySQL暂时停止响应新请求之前的短时间内多少个请求可以被存在堆栈中。如果系统在一个短时间内有很多连接，则需要增大该参数的值，该参数值指定到来的TCP/IP连接的侦听队列的大小。不同的操作系统在这个队列大小上有它自己的限制。 试图设定back_log高于你的操作系统的限制将是无效的。\r\n当观察MySQL进程列表，发现大量 264084 | unauthenticated user | xxx.xxx.xxx.xxx | NULL | Connect | NULL | login | NULL 的待连接进程时，就要加大 back_log 的值。back_log默认值为50。"))
                                .setVisible(true);
    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8295226233221692277L;

    private DBConfigItem item = null;

    public DataBaseAnaylizeDetailViewDialog(JFrame frame, DBConfigItem item) {

        super(frame, true);
        this.item = item;
        initGui();
    }

    private void initGui() {

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel centerPanel = new JPanel();
        JPanel footPanel = new JPanel();
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(footPanel, BorderLayout.SOUTH);

        JLabel label11 = new JLabel("参数名：");
        JLabel label12 = new JLabel(item.getKey());

        JLabel label21 = new JLabel("参数值：");
        JLabel label22 = new JLabel(item.getValue());

        JLabel label31 = new JLabel("说明：");
        JEditorPane jep = new JEditorPane();
        jep.setText(item.getDescribe());
        JScrollPane label32 = new JScrollPane(jep);

        JButton button = new JButton("我知道了");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DataBaseAnaylizeDetailViewDialog.this.dispose();
            }
        });
        footPanel.add(button);

        // 为指定的 Container 创建 GroupLayout
        GroupLayout layout = new GroupLayout(centerPanel);
        centerPanel.setLayout(layout);
        // 创建GroupLayout的水平连续组，，越先加入的ParallelGroup，优先级级别越高。
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGap(5);// 添加间隔
        hGroup.addGroup(layout.createParallelGroup().addComponent(label11).addComponent(label21).addComponent(label31));
        hGroup.addGap(5);
        hGroup.addGroup(layout.createParallelGroup().addComponent(label12).addComponent(label22).addComponent(label32));
        hGroup.addGap(5);
        layout.setHorizontalGroup(hGroup);
        // 创建GroupLayout的垂直连续组，，越先加入的ParallelGroup，优先级级别越高。
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGap(10);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label11).addComponent(label12));
        vGroup.addGap(15);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label21).addComponent(label22));
        vGroup.addGap(15);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label31).addComponent(label32));

        // 设置垂直组
        layout.setVerticalGroup(vGroup);

        this.setSize(new Dimension(900, 400));
        this.setLocationRelativeTo(null);
    }

}

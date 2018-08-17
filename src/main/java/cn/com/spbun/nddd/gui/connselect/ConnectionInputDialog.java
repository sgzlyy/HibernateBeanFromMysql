package cn.com.spbun.nddd.gui.connselect;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import cn.com.spbun.common.ui.ext.JButtonExt;
import cn.com.spbun.common.ui.ext.JDialogExt;
import cn.com.spbun.common.ui.ext.JLabelExt;
import cn.com.spbun.common.ui.ext.JPanelExt;
import cn.com.spbun.common.ui.ext.JTextFieldExt;
import cn.com.spbun.nddd.model.ConnectionInfo;

/**
 * 连接信息输入对话框<br>
 * 用来新增连接或者连接信息的编辑
 * 
 * @author NOLY DAKE
 *
 */
public class ConnectionInputDialog extends JDialogExt {

    /**
     * 日志对象
     */
    private static final Logger LOG = Logger.getLogger(ConnectionInputDialog.class);

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2880655999125006086L;

    private JButtonExt bt1;
    private JButtonExt bt2;
    private JButtonExt bt3;
    private ConnectionSelectFrame frame = null;
    private JLabelExt label1;
    private JLabelExt label2;
    private JLabelExt label3;
    private JLabelExt label4;
    private JLabelExt label5;
    private ConnectionInfo resultInfo = null;

    private JTextFieldExt tf1;
    private JTextFieldExt tf2;
    private JTextFieldExt tf3;
    private JTextFieldExt tf4;
    private JTextFieldExt tf5;

    public static void main(String[] args) {
        new ConnectionInputDialog(null).setVisible(true);
    }

    /**
     * 获得选择的按钮
     */
    private int selectedButton = BTN_CANCEL_CLICKED;

    public int getSelectedButton() {
        return selectedButton;
    }

    public static final int BTN_OK_CLICKED = 1;
    public static final int BTN_CANCEL_CLICKED = 2;

    public ConnectionInputDialog(ConnectionSelectFrame frame) {
        this(frame, null);
    }

    public ConnectionInputDialog(ConnectionSelectFrame frame, ConnectionInfo connInfo) {
        super(frame, true);
        this.frame = frame;
        this.resultInfo = connInfo;
        initGui();
        bindListener();
    }

    private void assembleConnectionInfo(ConnectionInfo info) {

        info.setName(tf1.getText());
        info.setJdbcDriverName(tf2.getText());
        info.setJdbcConnString(tf3.getText());
        info.setUsername(tf4.getText());
        info.setPassword(tf5.getText());

        LOG.info(info);
    }

    private void bindListener() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("绑定事件开始...");
        }

        bt1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btn1Click();
            }
        });

        bt3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btn3Click();
            }
        });

        bt2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btn2Click();
            }
        });

        if (LOG.isDebugEnabled()) {
            LOG.debug("绑定事件结束");
        }
    }

    private void btn1Click() {

        if (resultInfo == null) {
            resultInfo = new ConnectionInfo();
        }

        assembleConnectionInfo(resultInfo);

        selectedButton = BTN_OK_CLICKED;

        this.dispose();
    }

    private void btn2Click() {
        selectedButton = BTN_CANCEL_CLICKED;

        ConnectionInputDialog.this.dispose();
    }

    private void btn3Click() {

        ConnectionInfo connectionInfo = new ConnectionInfo();

        assembleConnectionInfo(connectionInfo);

        try {
            Class.forName(connectionInfo.getJdbcDriverName());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "无法加载数据库驱动:" + connectionInfo.getJdbcDriverName());
            LOG.error(e.getMessage(), e);
            return;
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionInfo.getJdbcConnString(), connectionInfo.getUsername(),
                    connectionInfo.getPassword());

            if (connection != null) {
                JOptionPane.showMessageDialog(this, "测试成功");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "连接数据库出现错误：" + e.getMessage());
            LOG.error(e.getMessage(), e);
            return;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    public ConnectionInfo getResultInfo() {
        return resultInfo;
    }

    private void initGui() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("初始化界面开始...");
        }

        this.setTitle("连接管理");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        label1 = new JLabelExt("连接名：");
        label2 = new JLabelExt("JDBC驱动名称：");
        label3 = new JLabelExt("连接字符串：");
        label4 = new JLabelExt("用户名：");
        label5 = new JLabelExt("密码：");

        if (resultInfo == null) {
            tf1 = new JTextFieldExt("连接");
            tf2 = new JTextFieldExt("com.mysql.jdbc.Driver");
            tf3 = new JTextFieldExt("jdbc:mysql://192.168.2.xx:3306/sechma");
            tf4 = new JTextFieldExt("root");
            tf5 = new JTextFieldExt("root");
        } else {
            tf1 = new JTextFieldExt(resultInfo.getName());
            tf2 = new JTextFieldExt(resultInfo.getJdbcDriverName());
            tf3 = new JTextFieldExt(resultInfo.getJdbcConnString());
            tf4 = new JTextFieldExt(resultInfo.getUsername());
            tf5 = new JTextFieldExt(resultInfo.getPassword());
        }

        bt1 = new JButtonExt("确认");
        bt2 = new JButtonExt("取消");
        bt3 = new JButtonExt("测试");
        JPanelExt panel = new JPanelExt();
        panel.add(bt3);
        panel.add(bt1);
        panel.add(bt2);

        // 为指定的 Container 创建 GroupLayout
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        // 创建GroupLayout的水平连续组，，越先加入的ParallelGroup，优先级级别越高。
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGap(5);// 添加间隔
        hGroup.addGroup(layout.createParallelGroup().addComponent(label1).addComponent(label2).addComponent(label3)
                .addComponent(label4).addComponent(label5));
        hGroup.addGap(5);
        hGroup.addGroup(layout.createParallelGroup().addComponent(tf1).addComponent(tf2).addComponent(tf3)
                .addComponent(tf4).addComponent(tf5).addComponent(panel));
        hGroup.addGap(5);
        layout.setHorizontalGroup(hGroup);
        // 创建GroupLayout的垂直连续组，，越先加入的ParallelGroup，优先级级别越高。
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGap(10);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label1).addComponent(tf1));
        vGroup.addGap(5);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label2).addComponent(tf2));
        vGroup.addGap(5);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label3).addComponent(tf3));
        vGroup.addGap(5);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label4).addComponent(tf4));
        vGroup.addGap(5);
        vGroup.addGroup(layout.createParallelGroup().addComponent(label5).addComponent(tf5));
        vGroup.addGap(5);
        vGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(panel));
        vGroup.addGap(10);

        // 设置垂直组
        layout.setVerticalGroup(vGroup);
        this.setSize(new Dimension(500, 300));
        this.setMinimumSize(new Dimension(500, 300));
        this.setLocationRelativeTo(frame);

        if (LOG.isDebugEnabled()) {
            LOG.debug("初始化界面结束");
        }
    }
}

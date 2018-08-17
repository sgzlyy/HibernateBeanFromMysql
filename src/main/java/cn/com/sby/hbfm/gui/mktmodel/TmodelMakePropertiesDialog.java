package cn.com.sby.hbfm.gui.mktmodel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import cn.com.sby.common.FileUtil;
import cn.com.sby.common.ui.ext.JButtonExt;
import cn.com.sby.common.ui.ext.JDialogExt;
import cn.com.sby.common.ui.ext.JLabelExt;
import cn.com.sby.common.ui.ext.JPanelExt;
import cn.com.sby.common.ui.ext.JTextFieldExt;
import cn.com.sby.hbfm.HBFMApplication;
import cn.com.sby.hbfm.gui.console.TmodelMakerConsoleDialog;
import cn.com.sby.hbfm.model.ConnectionInfo;

/**
 * 设置生成Tmodel属性的对话框
 * 
 *
 *
 */
public class TmodelMakePropertiesDialog extends JDialogExt {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3424893536509001265L;

    /**
     * TODO:测试
     * 
     * @param args
     */
    public static void main(String[] args) {
        new TmodelMakePropertiesDialog(null, null).setVisible(true);
    }

    /** 数据库连接信息 */
    private ConnectionInfo connectionInfo = null;

    /** 用来选择的文件标签 */
    private JLabelExt selectedForder = null;

    /** 数据表的前缀 */
    private JList<String> lstTablePrefix = null;

    private JFrame frame = null;

    public TmodelMakePropertiesDialog(JFrame frame, ConnectionInfo connectionInfo) {
        super(frame, true);
        this.frame = frame;
        this.connectionInfo = connectionInfo;
        // 界面初始化
        initGui();
    }

    protected void prefixAddBtnClick() {

        String msg = JOptionPane.showInputDialog("请输入前缀！");
        if (msg == null || msg.trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "没有内容，不进行处理");
            return;
        }
        DefaultListModel<String> model = (DefaultListModel<String>) lstTablePrefix.getModel();
        model.addElement(msg);
    }

    protected void btbCancelClick() {
        int result = JOptionPane.showConfirmDialog(this, "您确认要放弃吗？");
        if (result == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }

    protected void btnFileSelectClick() {

        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = jfc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = jfc.getSelectedFile().getAbsolutePath();
            selectedForder.setText(filePath);
            selectedForder.setToolTipText(filePath);
        }
    }

    /**
     * 按下开始生成按钮的时候，开始执行
     */
    protected void btnOKClick() {

        // 获取前缀
        DefaultListModel<String> model = (DefaultListModel<String>) lstTablePrefix.getModel();

        List<String> tablePrefix = new ArrayList<String>();
        String tablePrefixString = "";
        for (int i = 0; i < model.getSize(); i++) {
            String value = model.getElementAt(i);
            tablePrefix.add(value);
            if (i == 0) {
                tablePrefixString = value;
            } else {
                tablePrefixString = tablePrefixString + "," + value;
            }
        }

        connectionInfo.setTablePrefixString(tablePrefixString);

        // 包名
        connectionInfo.setPackageNameString(lblPackage.getText());
        connectionInfo.setSaveModelPathString(selectedForder.getText());

        // 保存信息->文件
        HBFMApplication.getInstance().flushInfo2File();

        // 弹出执行对话框
        new TmodelMakerConsoleDialog(this, connectionInfo, new File(selectedForder.getText()), tablePrefix,
                lblPackage.getText()).setVisible(true);

    }

    protected void delBtnClick() {

        int index = lstTablePrefix.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "请选择一行后在进行删除操作！");
            return;
        }

        DefaultListModel<String> model = (DefaultListModel<String>) lstTablePrefix.getModel();
        model.remove(index);
    }

    /** 标签：包路径 */
    private JTextFieldExt lblPackage = null;

    private void initGui() {

        JPanelExt centerPanel = new JPanelExt();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        this.add(centerPanel, BorderLayout.CENTER);

        {
            // Tmodel的输出包，ADE301默认地址为：cn.com.infcn.core.model
            JPanelExt panelPackage = new JPanelExt();
            panelPackage.setLayout(new BorderLayout());
            panelPackage.setBorder(BorderFactory.createTitledBorder(null, "请输入包名：", TitledBorder.LEADING,
                    TitledBorder.DEFAULT_POSITION, null, Color.WHITE));

            String packageName = connectionInfo.getPackageNameString();
            if (packageName == null || packageName.trim().length() == 0) {
                packageName = "cn.com.infcn.core.model";
            }
            lblPackage = new JTextFieldExt(packageName);
            panelPackage.add(lblPackage, BorderLayout.CENTER);
            centerPanel.add(panelPackage);
        }

        {
            JPanelExt panel = new JPanelExt();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder(null, "请选择保存目录", TitledBorder.LEADING,
                    TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
            File file = new File(FileUtil.getInstallPath());
            String path = connectionInfo.getSaveModelPathString();
            if (path == null || path.trim().length() == 0) {
                path = file.getAbsolutePath();
            }
            selectedForder = new JLabelExt(path);
            panel.add(selectedForder, BorderLayout.CENTER);
            JButtonExt btnFileSelect = new JButtonExt("选择路径");
            panel.add(btnFileSelect, BorderLayout.EAST);
            centerPanel.add(panel);
            btnFileSelect.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    btnFileSelectClick();
                }
            });
        }
        {
            JPanelExt panel1 = new JPanelExt();
            panel1.setBorder(BorderFactory.createTitledBorder(null, "请设置数据表的前缀", TitledBorder.LEADING,
                    TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
            centerPanel.add(panel1);

            panel1.setLayout(new BorderLayout());

            DefaultListModel<String> model = new DefaultListModel<String>();
            String tablePrefix = connectionInfo.getTablePrefixString();
            if (tablePrefix != null && tablePrefix.trim().length() != 0) {
                String[] tablePrefixL = tablePrefix.split(",");
                for (String s : tablePrefixL) {
                    if (s == null || s.trim().length() == 0) {
                        continue;
                    }
                    model.addElement(s);
                }
            }
            lstTablePrefix = new JList<String>(model);
            JScrollPane jsp = new JScrollPane(lstTablePrefix);
            panel1.add(jsp, BorderLayout.CENTER);

            JPanelExt rightBtnPanel = new JPanelExt();
            rightBtnPanel.setLayout(new BoxLayout(rightBtnPanel, BoxLayout.Y_AXIS));
            // 增加按钮
            JButtonExt prefixAddBtn = new JButtonExt("增加");
            rightBtnPanel.add(prefixAddBtn);
            rightBtnPanel.add(Box.createVerticalStrut(5));
            prefixAddBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    prefixAddBtnClick();
                }
            });
            JButtonExt updBtn = new JButtonExt("修改");
            rightBtnPanel.add(updBtn);
            rightBtnPanel.add(Box.createVerticalStrut(5));
            updBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    updBtnClick();
                }
            });

            JButtonExt delBtn = new JButtonExt("删除");
            rightBtnPanel.add(delBtn);
            delBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    delBtnClick();
                }
            });

            panel1.add(rightBtnPanel, BorderLayout.EAST);

        }
        JPanelExt footPanel = new JPanelExt();

        JButtonExt btnOK = new JButtonExt("开始生成...");
        footPanel.add(btnOK);
        btnOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnOKClick();
            }
        });

        JButtonExt btnCancel = new JButtonExt("放弃");
        footPanel.add(btnCancel);
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btbCancelClick();
            }
        });

        this.add(footPanel, BorderLayout.SOUTH);
        this.setSize(600, 400);
        this.setLocationRelativeTo(frame);
    }

    protected void updBtnClick() {

        int index = lstTablePrefix.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "请选择一行后在进行编辑操作！");
            return;
        }

        String msg = JOptionPane.showInputDialog("请输入前缀！");
        if (msg == null || msg.trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "没有内容，不进行处理");
            return;
        }

        DefaultListModel<String> model = (DefaultListModel<String>) lstTablePrefix.getModel();
        model.set(index, msg);
    }
}

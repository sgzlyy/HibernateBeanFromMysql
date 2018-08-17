package cn.com.spbun.nddd.gui.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;

import cn.com.spbun.common.DateUtil;
import cn.com.spbun.nddd.gui.major.MainFrame;

public class ConsoleDialog extends JDialog {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2238542444996492927L;

    public ConsoleDialog(MainFrame frame) {
        super(frame, true);
        this.frame = frame;
        initGui();
    }

    public ConsoleDialog(JDialog dialog) {
        super(dialog, true);
        this.dialog = dialog;
        initGui();
    }

    private JDialog dialog = null;

    protected JButton btnOk = null;

    /**
     * 主界面
     */
    private MainFrame frame = null;

    /**
     * 滚动面板
     */
    private JScrollPane jsp = null;

    /**
     * 日志信息
     */
    private JList<String> consoleList = null;

    private DefaultListModel<String> listModel = null;

    public void console(String log) {

        listModel.addElement(DateUtil.currentTime() + log);

        // 设置显示最后一条
        int last = consoleList.getLastVisibleIndex();
        Rectangle rect = consoleList.getCellBounds(last, last);
        jsp.getViewport().scrollRectToVisible(rect);
    }

    private void initGui() {

        jsp = new JScrollPane();

        listModel = new DefaultListModel<String>();
        consoleList = new JList<String>(listModel);

        jsp.setViewportView(consoleList);

        this.add(jsp, BorderLayout.CENTER);

        btnOk = new JButton("完成");
        btnOk.setEnabled(false);
        btnOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ConsoleDialog.this.dispose();
            }
        });
        this.add(btnOk, BorderLayout.SOUTH);

        this.setSize(new Dimension(600, 400));

        if (frame != null) {
            this.setLocationRelativeTo(frame);
        }
        if (dialog != null) {
            this.setLocationRelativeTo(dialog);
        }
    }
}

package cn.com.sby.hbfm.gui.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

import cn.com.sby.common.DateUtil;
import cn.com.sby.hbfm.gui.mktmodel.TmodelMakePropertiesDialog;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.TableInfo;
import cn.com.sby.hbfm.service.TmodelMakeService;
import cn.com.sby.hbfm.service.database.mysql.Mysql55AnaylizerService;
import cn.com.sby.hbfm.service.database.mysql.MysqlAnaylizerServiceI;

/**
 * 导出的界面
 * 
 *
 *
 */
public class TmodelMakerConsoleDialog extends ConsoleDialog {

    /**
     * 内部线程：用来处理数据库分析以及导出操作
     * 
     *
     *
     */
    class TmodelMakerConsoleThread implements Runnable {

        @Override
        public void run() {

            try {

                // 首先进行数据库分析
                MysqlAnaylizerServiceI service = new Mysql55AnaylizerService();
                service.setConsole(TmodelMakerConsoleDialog.this);

                // 加载数据库中的所有表信息
                List<TableInfo> tableInfos = service.loadTableInfos(connectionInfo);

                // 导出服务
                TmodelMakeService tmodelMakeService = new TmodelMakeService(connectionInfo, file, tablePrefix,
                        tableInfos, packageName);
                tmodelMakeService.setConsole(TmodelMakerConsoleDialog.this);

                tmodelMakeService.makeTmodels();

                btnOk.setEnabled(true);

            } catch (SQLException e) {

                console(e.getMessage());
            }
        }
    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2372320549860918164L;

    private JButton btnOk = null;

    /** 数据库连接信息，根据这个信息我们才能进行数据处理 */
    private ConnectionInfo connectionInfo = null;

    /**
     * 主界面
     */
    private TmodelMakePropertiesDialog dialog = null;

    /**
     * 滚动面板
     */
    private JScrollPane jsp = null;

    /**
     * 日志信息
     */
    private JList<String> consoleList = null;
    private File file = null;
    private List<String> tablePrefix = null;

    private DefaultListModel<String> listModel = null;

    private String packageName = null;

    /**
     * 构造方法
     * 
     * @param frame
     *            用来锁定的面板
     * @param connectionInfo
     *            数据库连接信息
     * @param tablePrefix
     * @param file
     */
    public TmodelMakerConsoleDialog(TmodelMakePropertiesDialog dialog, ConnectionInfo connectionInfo, File file,
            List<String> tablePrefix, String packageName) {

        super(dialog);
        this.dialog = dialog;
        this.connectionInfo = connectionInfo;
        this.file = file;
        this.tablePrefix = tablePrefix;
        this.packageName = packageName;

        initGui();

        new Thread(new TmodelMakerConsoleThread()).start();
    }

    @Override
    public void console(String log) {

        listModel.addElement(DateUtil.currentTime() + log);

        // 设置显示最后一条
        int last = consoleList.getLastVisibleIndex();
        Rectangle rect = consoleList.getCellBounds(last, last);

        if (rect == null) {
            return;
        }

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
                TmodelMakerConsoleDialog.this.dispose();
            }
        });
        this.add(btnOk, BorderLayout.SOUTH);

        this.setSize(new Dimension(600, 400));
        this.setLocationRelativeTo(dialog);
    }
}

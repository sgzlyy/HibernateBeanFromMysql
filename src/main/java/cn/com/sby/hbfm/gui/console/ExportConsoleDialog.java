package cn.com.sby.hbfm.gui.console;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import cn.com.sby.hbfm.gui.major.MainFrame;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.TableInfo;
import cn.com.sby.hbfm.service.Export2CHMService;
import cn.com.sby.hbfm.service.database.mysql.Mysql55AnaylizerService;
import cn.com.sby.hbfm.service.database.mysql.MysqlAnaylizerServiceI;

/**
 * 导出的界面
 * 
 *
 *
 */
public class ExportConsoleDialog extends ConsoleDialog {

    /**
     * 内部线程：用来处理数据库分析以及导出操作
     * 
     *
     *
     */
    class ExportConsoleDialogThread implements Runnable {

        @Override
        public void run() {

            try {

                // 首先进行数据库分析
                MysqlAnaylizerServiceI service = new Mysql55AnaylizerService();
                service.setConsole(ExportConsoleDialog.this);

                //
                List<TableInfo> tableInfos = service.loadTableInfos(connectionInfo);

                Export2CHMService export2CHMService = new Export2CHMService(new File("D:/delete/hbfm/html"),
                        tableInfos);
                export2CHMService.setConsole(ExportConsoleDialog.this);

                export2CHMService.export2CHM();

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

    /** 数据库连接信息，根据这个信息我们才能进行数据处理 */
    private ConnectionInfo connectionInfo = null;

    /**
     * 构造方法
     * 
     * @param frame
     *            用来锁定的面板
     * @param connectionInfo
     *            数据库连接信息
     */
    public ExportConsoleDialog(MainFrame frame, ConnectionInfo connectionInfo) {

        super(frame);
        this.connectionInfo = connectionInfo;

        new Thread(new ExportConsoleDialogThread()).start();
    }

}

package cn.com.sby.hbfm;

import cn.com.sby.common.FileUtil;
import cn.com.sby.common.SwingUtil;
import cn.com.sby.hbfm.gui.connselect.ConnectionSelectFrame;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.PersistenceObject;
import cn.com.sby.hbfm.service.SaveFileLoadService;
import cn.com.sby.hbfm.service.SaveFileStoreService;
import org.apache.log4j.Logger;

import java.io.File;

/**
 *
 * 系统的全局环境，负责项目的启动以及全局参数<br>
 * 
 *
 * @date 2017年4月27日
 * @version 1.0
 *
 */
public class HBFMApplication {

    /** 日志对象 */
    private static final Logger LOG = Logger.getLogger(HBFMApplication.class);

    private static HBFMApplication instance = null;

    /**
     * 单例模式
     * 
     * @return
     */
    public static HBFMApplication getInstance() {

        if (instance == null) {
            instance = new HBFMApplication();
        }

        return instance;
    }

    /**
     * 如果使用调试模式的话，需要设置这个值
     */
    public String forder4Develop = null;

    private PersistenceObject persistenceObject = null;

    /**
     * 当前数据库连接的信息
     */
    private ConnectionInfo currentConnectionInfo = null;

    /**
     * 默认的构造方法
     */
    private HBFMApplication() {
        // NOTHINT
    }

    public void flushInfo2File() {
        SaveFileStoreService sfss = new SaveFileStoreService();
        sfss.save(persistenceObject, getStoreXml());
    }

    public ConnectionInfo getCurrentConnectionInfo() {
        return currentConnectionInfo;
    }

    public PersistenceObject getPersistenceObject() {
        return persistenceObject;
    }

    private File getStoreXml() {

        File resourcesForder = new File(FileUtil.getInstallPath(), "resources");

        File forder = new File(resourcesForder, "config");

        File file = new File(forder, "store.xml");

        if (LOG.isDebugEnabled()) {
            LOG.debug("存储信息文件为" + file);
        }

        return file;
    }

    public void setCurrentConnectionInfo(ConnectionInfo currentConnectionInfo) {
        this.currentConnectionInfo = currentConnectionInfo;
    }

    public void startApp() {

        // 确认是否启用调试信息
        forder4Develop = System.getProperty("test.SystemStartForder");

        persistenceObject = new SaveFileLoadService().load(getStoreXml());

        // 美化界面用
        SwingUtil.updateUIFont(SwingUtil.FONT_SIMSUN);

        new ConnectionSelectFrame().setVisible(true);
    }
}

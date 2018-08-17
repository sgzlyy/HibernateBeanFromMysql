package cn.com.spbun.nddd;

import java.io.File;

import org.apache.log4j.Logger;

import cn.com.spbun.common.FileUtil;
import cn.com.spbun.common.SwingUtil;
import cn.com.spbun.nddd.gui.connselect.ConnectionSelectFrame;
import cn.com.spbun.nddd.model.ConnectionInfo;
import cn.com.spbun.nddd.model.PersistenceObject;
import cn.com.spbun.nddd.service.SaveFileLoadService;
import cn.com.spbun.nddd.service.SaveFileStoreService;

/**
 *
 * 系统的全局环境，负责项目的启动以及全局参数<br>
 * 
 * @author 杨彪
 * @date 2017年4月27日
 * @version 1.0
 *
 */
public class NdddApplication {

    /** 日志对象 */
    private static final Logger LOG = Logger.getLogger(NdddApplication.class);

    private static NdddApplication instance = null;

    /**
     * 单例模式
     * 
     * @return
     */
    public static NdddApplication getInstance() {

        if (instance == null) {
            instance = new NdddApplication();
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
    private NdddApplication() {
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

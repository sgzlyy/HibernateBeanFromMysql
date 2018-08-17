package cn.com.spbun.common;

import java.io.Closeable;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * IO工具类
 * 
 * @author 杨彪
 *
 */
public class IOUtil {

    /** 日志对象 */
    private static final Logger LOG = Logger.getLogger(IOUtil.class);

    /**
     * 安静的关闭
     * 
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {

        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}

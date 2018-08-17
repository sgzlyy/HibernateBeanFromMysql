package cn.com.sby.hbfm.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.sby.common.FileUtil;
import cn.com.sby.hbfm.gui.console.ExportConsoleDialog;
import cn.com.sby.hbfm.model.TableInfo;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Export2CHMService {

    private File outputForder = null;
    private List<TableInfo> tableInfos = null;

    private ExportConsoleDialog console = null;

    public void setConsole(ExportConsoleDialog console) {
        this.console = console;
    }

    public Export2CHMService(File outputForder, List<TableInfo> tableInfos) {
        this.outputForder = outputForder;
        this.tableInfos = tableInfos;
    }

    /** 日志对象 */
    private static final Logger LOG = Logger.getLogger(Export2CHMService.class);

    /**
     * 将信息导出到文件中
     */
    public void export2CHM() {

        if (console != null) {
            console.console("加载配置文件....");
        }

        // 加载模板
        Template template = loadTableTemplete();

        File tempForder = new File(outputForder.getParentFile(), "temp");
        tempForder.mkdirs();

        for (TableInfo tableInfo : tableInfos) {
            writeTableInfo(tableInfo, tempForder, template);
        }
    }

    private void writeTableInfo(TableInfo tableInfo, File tempForder, Template template) {

        Writer out = null;

        try {
            File outputFile = new File(tempForder, tableInfo.getTableName() + ".html");

            // 将模板和数据模型合并生成文件
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));

            // 生成文件
            template.process(tableInfo, out);

            // 关闭流
            out.flush();
        } catch (TemplateException e) {
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 加载模板
     * 
     * @return
     */
    private Template loadTableTemplete() {

        // 创建配置实例
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        // 设置编码
        configuration.setDefaultEncoding("UTF-8");

        File file = new File(FileUtil.getInstallPath(), "templete");

        Template template = null;

        try {
            TemplateLoader templateLoader = new FileTemplateLoader(file);

            // ftl模板文件统一放至 com.lun.template 包下面
            configuration.setTemplateLoader(templateLoader);

            // 获取模板
            template = configuration.getTemplate("table.html");

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        return template;
    }
}

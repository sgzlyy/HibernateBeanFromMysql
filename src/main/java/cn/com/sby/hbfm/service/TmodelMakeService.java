package cn.com.sby.hbfm.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import cn.com.sby.common.FileUtil;
import cn.com.sby.common.IOUtil;
import cn.com.sby.common.Inflector;
import cn.com.sby.common.TmodelUtil;
import cn.com.sby.hbfm.gui.console.ConsoleDialog;
import cn.com.sby.hbfm.model.ColumnInfo;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.FKInfo;
import cn.com.sby.hbfm.model.TableInfo;
import cn.com.sby.hbfm.model.TmodelClassDetail;
import cn.com.sby.hbfm.model.TmodelFieldDetail;
import cn.com.sby.hbfm.service.database.mysql.AbstractMysqlAnaylizerService;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 生成Tmodel<br>
 * 外部主要调用makeTmodels方法
 * 
 *
 *
 */
public class TmodelMakeService {

    /** 日志对象 */
    private static final Logger LOG = Logger.getLogger(TmodelMakeService.class);

    /** 输出目录 */
    private File outputForder = null;
    /** 数据库表信息 */
    private List<TableInfo> tableInfos = null;
    /** 表的前缀 */
    private List<String> tablePrefix = null;
    /** 包名 */
    private String packageName = null;

    /**
     * 命令输出<br>
     * 对象为空的时候，不输出
     */
    private ConsoleDialog console = null;

    public void setConsole(ConsoleDialog console) {
        this.console = console;
    }

    /**
     * 默认的构造方法
     * 
     * @param connectionInfo
     * @param file
     * @param tablePrefix
     * @param tableInfos
     * @param packageName
     */
    public TmodelMakeService(ConnectionInfo connectionInfo, File file, List<String> tablePrefix,
            List<TableInfo> tableInfos, String packageName) {
        this.outputForder = file;
        this.tablePrefix = tablePrefix;
        this.tableInfos = tableInfos;
        this.packageName = packageName;

    }

    /**
     * 生成Tmodels
     */
    public void makeTmodels() {

        if (console != null) {
            console.console("在输出目录创建model目录开始...");
        }

        File tempForder = new File(outputForder, "model");
        tempForder.mkdirs();

        if (console != null) {
            console.console("开始生成Tmodel...");
        }

        Template template = loadTableTemplete();
        if (template == null) {
            return;
        }

        // 循环生成Tmodel
        for (int i = 0; i < tableInfos.size(); i++) {

            TableInfo tableInfo = tableInfos.get(i);

            if (tableInfo.isMiddleTable()) {// 中间表不生成Tmodel

                if (console != null) {
                    console.console("[" + (i + 1) + "/" + tableInfos.size() + "]为中间表，不生成Tmodel");
                }
                continue;
            }

            makeTmodel(template, tableInfo, tempForder, i);
        }
    }

    private void makeTmodel(Template template, TableInfo tableInfo, File tempForder, int off) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("开始为表" + (off + 1) + "[" + tableInfo.getTableName() + "]生成Java实体类.");
        }

        TmodelClassDetail detail = conventTableInfo2TmodelInfo(tableInfo, packageName);

        File outputFile = new File(tempForder, detail.getClassName() + ".java");

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        OutputStreamWriter osw = null;

        try {

            fos = new FileOutputStream(outputFile, false);
            bos = new BufferedOutputStream(fos);
            osw = new OutputStreamWriter(bos, "UTF-8");

            if (console != null) {
                console.console("[" + (off + 1) + "/" + tableInfos.size() + "]生成Tmodel：" + detail.getClassName());
            }
            template.process(detail, osw);
        } catch (TemplateException e) {
            LOG.error(e.getMessage(), e);
            if (console != null) {
                console.console("处理过程出现异常，异常信息：" + e.getMessage());
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            if (console != null) {
                console.console("处理过程出现异常，异常信息：" + e.getMessage());
            }
        } finally {
            IOUtil.closeQuietly(osw);
            IOUtil.closeQuietly(bos);
            IOUtil.closeQuietly(fos);
        }
    }

    /**
     * 将一条数据库表信息转换为Tmodel模型
     * <p>
     * 首先设置表的信息
     * </p>
     * 
     * <p>
     * 循环字段，判断字段是否被其他表引用。如果被引用，则使用对象类型，否则使用基本数据类型。
     * </p>
     * 
     * @param currentTable
     *            数据库表信息
     * @param tables
     *            数据库中的所有表信息
     * @param pkgName
     *            要输出的包名
     * @return
     */
    private TmodelClassDetail conventTableInfo2TmodelInfo(TableInfo currentTable, String pkgName) {

        TmodelClassDetail clazz = new TmodelClassDetail();

        clazz.setPkgName(pkgName);// 包名称
        clazz.setTableName(currentTable.getTableName());
        clazz.setTableDescribe(currentTable.getTableDescribe().replaceAll("\r\n", ""));

        // 获得不包含前缀的数据库表名称
        String tableName = TmodelUtil.removePrefix(currentTable.getTableName(), tablePrefix);// 移除表中的前缀信息

        clazz.setClassName(TmodelUtil.convent2ClassName(tableName));// 类的名称
        clazz.setInstanceName(TmodelUtil.convent2InstanceName(tableName));// 实例的名称

        List<TmodelFieldDetail> fieldDetails = new ArrayList<TmodelFieldDetail>();
        clazz.setFieldDetails(fieldDetails);

        // 将数据库表中的的字段（Column）转换为Java对象中的属性（Field）
        // 下面的处理结束后，基本完成了所有字段到属性的映射关系
        mappingCurrentTableFk(currentTable, clazz);
        // 设置被引用信息
        // 其他表引用了当前的表，那么当前表要设置Set类型的对象
        mappingOtherTableFk(currentTable, clazz);

        return clazz;

    }

    private void mappingOtherTableFk(TableInfo currentTable, TmodelClassDetail clazz) {

        List<ColumnInfo> currentTableColumns = currentTable.getColumnInfos();// 当前表的字段信息

        List<TmodelFieldDetail> fieldDetails = clazz.getFieldDetails();

        String tableName = TmodelUtil.removePrefix(currentTable.getTableName(), tablePrefix);// 移除表中的前缀信息

        if (tableName.equals("ade_user")) {
            System.out.println();
        }

        for (TableInfo refTableInfo : tableInfos) {

            // 如果是当前表的话，也要处理，存在自关联的情况，因此不能跳过当前表。
            // 如果是自关联的话，这里要生成Set集合类

            List<FKInfo> refFKInfos = refTableInfo.getForeignKeyInfos();// 其他表的外键
            if (refFKInfos == null || refFKInfos.size() == 0) {
                continue;
            }

            for (FKInfo refFKinfo : refFKInfos) {

                // 如果这个外键引用的表不是当前表的话，这个外键对当前处理没有意义，因此跳过
                if (!refFKinfo.getReferenceTableName().equals(currentTable.getTableName())) {
                    continue;
                }

                TmodelFieldDetail fieldDetail = new TmodelFieldDetail();
                fieldDetails.add(fieldDetail);

                String refFKTableName = null;

                // 普通的外键引用以及中间表的处理是不一样的
                if (refTableInfo.isMiddleTable()) {

                    clazz.setMDK(true);
                    fieldDetail.setMDK(true);

                    // 如果是中间表的话，当前为表A，第二张表为C，B为A与C的中间表，那么在判断A的关联条件的时候，应该让A中存储一个C
                    // 因此应该取另外一个字段对应的表作为要处理的表。
                    // 比如数据表module、role、role_module这三张表，在处理role表的时候，找到了role_module中间表中的外键role_id。
                    //
                    for (FKInfo temp : refFKInfos) {
                        if (!temp.equals(refFKinfo)) {
                            refFKTableName = TmodelUtil.removePrefix(temp.getReferenceTableName(), tablePrefix);
                            fieldDetail.setMdkColumnName(temp.getColumnName());
                            break;
                        }
                    }

                    fieldDetail.setMdkTableName(refFKinfo.getTableName());
                } else {
                    clazz.setHasRK(true);
                    fieldDetail.setRK(true);
                    refFKTableName = TmodelUtil.removePrefix(refFKinfo.getTableName(), tablePrefix);
                }

                String fkinstances = Inflector.getInstance().pluralize(TmodelUtil.convent2InstanceName(refFKTableName));
                // String fkObjectType =
                // TmodelUtil.convent2ClassName(tableName); //

                String replaceInstanceName = fkinstances;// 名称转换为复数

                boolean isExist = false;
                for (TmodelFieldDetail currentTableFieldDetail : fieldDetails) {// 循环当前表的

                    String currentInstanceType = currentTableFieldDetail.getInstanceName();

                    if (fkinstances.equals(currentInstanceType)) {

                        currentTableFieldDetail.setInstanceName(currentInstanceType + "By"
                                + TmodelUtil.column2ClazzForUpper(currentTableFieldDetail.getColumnEnName()));
                        currentTableFieldDetail.setRkInstanceName(currentTableFieldDetail.getRkInstanceName() + "By"
                                + TmodelUtil.column2ClazzForUpper(currentTableFieldDetail.getColumnEnName()));

                        isExist = true;
                    }
                }

                if (isExist) {
                    replaceInstanceName = replaceInstanceName + "By"
                            + TmodelUtil.column2ClazzForUpper(refFKinfo.getColumnName());

                    fieldDetail.setRkInstanceName(TmodelUtil.convent2InstanceName(
                            TmodelUtil.removePrefix(refFKinfo.getReferenceTableName(), tablePrefix)) + "By"
                            + TmodelUtil.column2ClazzForUpper(refFKinfo.getColumnName()));

                } else {
                    fieldDetail.setRkInstanceName(clazz.getInstanceName());
                }

                String description = "";
                for (ColumnInfo columnInfo : currentTableColumns) {

                    // 字段数据库名称
                    String columnName = columnInfo.getColumnNameEn();
                    if (columnName.equals(fieldDetail.getColumnEnName())) {
                        description = columnInfo.getDescription();
                        break;
                    }
                }

                fieldDetail.setColumnEnName(refFKinfo.getColumnName());
                fieldDetail.setDescription(description);
                fieldDetail.setInstanceName(replaceInstanceName);
                fieldDetail.setClazzName("Set<" + TmodelUtil.convent2ClassName(refFKTableName) + ">");
            }
        }

    }

    private void mappingCurrentTableFk(TableInfo currentTable, TmodelClassDetail clazz) {

        List<TmodelFieldDetail> fieldDetails = clazz.getFieldDetails();

        List<ColumnInfo> currentTableColumns = currentTable.getColumnInfos();// 当前表的字段信息

        // 获取当前表的外键信息，就是当前表的字段引用了其他表的信息
        List<FKInfo> foreignKeyInfoList = currentTable.getForeignKeyInfos();

        for (ColumnInfo columnInfo : currentTableColumns) {// 循环当前表的字段嘻嘻

            TmodelFieldDetail fieldDetail = new TmodelFieldDetail();
            fieldDetails.add(fieldDetail);

            fieldDetail.setCanNull(columnInfo.getIsCanNull());
            fieldDetail.setColumnLength(columnInfo.getColumnLength());
            fieldDetail.setDescription(columnInfo.getDescription());
            fieldDetail.setColumnEnName(columnInfo.getColumnNameEn());

            // 查找当前字段是否为外键
            FKInfo fkInfo = findFKInfo(fieldDetail.getColumnEnName(), foreignKeyInfoList);

            if (fkInfo != null) {// 如果外键信息不为空的话，使用对象字段，否则使用基本数据类型字段

                clazz.setHasFK(true);
                String refTableName = TmodelUtil.removePrefix(fkInfo.getReferenceTableName(), tablePrefix);

                String refInstance = TmodelUtil.convent2InstanceName(refTableName);// 引用的表名称->实例名称
                String refObjectType = TmodelUtil.convent2ClassName(refTableName);// 引用的表名称->对象名称

                // 如果一个字段被多次引用的话，需要在属性的后面增加By引用的
                boolean isEixct = false;

                if (fieldDetails.size() > 0) {
                    for (TmodelFieldDetail temp : fieldDetails) {
                        String instanceBefore = temp.getInstanceName();
                        String objectTypeBefore = temp.getClazzName();

                        if (!refObjectType.equals(objectTypeBefore)) {
                            continue;
                        }

                        if (refInstance.equals(instanceBefore)) {
                            temp.setInstanceName(
                                    instanceBefore + "By" + TmodelUtil.column2ClazzForUpper(temp.getColumnEnName()));
                        }
                        isEixct = true;
                    }
                }

                if (isEixct) {
                    refInstance = refInstance + "By" + TmodelUtil.column2ClazzForUpper(fieldDetail.getColumnEnName());
                }

                fieldDetail.setInstanceName(refInstance);
                fieldDetail.setClazzName(TmodelUtil.convent2ClassName(refTableName));
                fieldDetail.setPK(false);
                fieldDetail.setFK(true);

            } else {// 基本数据类型

                String fieldJavaType = AbstractMysqlAnaylizerService.COLUMNTYPE_JAVATYPE
                        .get(columnInfo.getColumnType());

                if ("Date".equals(fieldJavaType)) {
                    clazz.setHasDateField(true);
                    fieldDetail.setHasDateField(true);
                } else if ("BigDecimal".equals(fieldJavaType)) {
                    clazz.setHasDecimalField(true);
                } else if ("Integer".equals(fieldJavaType)) {
                    if (!fieldDetail.getCanNull()) {
                        fieldJavaType = "int";
                    }
                } else if ("Double".equals(fieldJavaType)) {
                    if (!fieldDetail.getCanNull()) {
                        fieldJavaType = "double";
                    }
                }

                fieldDetail.setClazzName(fieldJavaType);
                fieldDetail.setInstanceName(TmodelUtil.column2ClazzForLower(fieldDetail.getColumnEnName()));
                fieldDetail.setPK(columnInfo.getIsPrimaryKey());
                fieldDetail.setFK(false);
            }
        }

    }

    private FKInfo findFKInfo(String columnName, List<FKInfo> foreignKeyInfoList) {

        for (FKInfo finfo : foreignKeyInfoList) {
            String f_col_name = finfo.getColumnName();
            if (f_col_name.equals(columnName)) {
                return finfo;
            }
        }

        return null;
    }

    private Template loadTableTemplete() {

        // 创建配置实例
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        // 设置编码
        configuration.setDefaultEncoding("UTF-8");

        File forder = new File(FileUtil.getInstallPath(), "resources");
        File file = new File(forder, "templete");

        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "找不到模板文件，路径：" + file.getAbsolutePath());
            return null;
        }

        Template template = null;

        try {
            TemplateLoader templateLoader = new FileTemplateLoader(file);

            // ftl模板文件统一放至 com.lun.template 包下面
            configuration.setTemplateLoader(templateLoader);

            // 获取模板
            template = configuration.getTemplate("tmodel.tlp");

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        return template;
    }
}

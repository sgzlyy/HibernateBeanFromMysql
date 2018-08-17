package cn.com.sby.hbfm.service.database.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import cn.com.sby.hbfm.model.ColumnInfo;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.FKInfo;
import cn.com.sby.hbfm.model.TableInfo;

public class Mysql55AnaylizerService extends AbstractMysqlAnaylizerService {

    /** 日志对象 */
    private static final Logger LOG = Logger.getLogger(Mysql55AnaylizerService.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.com.sby.hbfm.service.mysql.A#loadTableInfos(java.sql.Connection)
     */
    @Override
    public List<TableInfo> loadTableInfos(ConnectionInfo connectionInfo) throws SQLException {

        // 定义返回值
        List<TableInfo> tableInfos = new ArrayList<TableInfo>();

        Connection connection = null;

        try {
            connection = getConnection(connectionInfo);

            tableInfos = loadTableNames(connection);

            loadColumns(connection, tableInfos);// 加载字段信息
            if (LOG.isDebugEnabled()) {
                LOG.debug("获取所有表字段完成");
            }
            console("获取所有表字段完成");

            loadFKInfo(connection, tableInfos);// 加载外键信息
            if (LOG.isDebugEnabled()) {
                LOG.debug("获取所有表外键信息完成");
            }
            console("获取所有表外键信息完成");

            loadTableCreateSQL(connection, tableInfos);// 加载创建SQL
            if (LOG.isDebugEnabled()) {
                LOG.debug("获取所有表加载创建SQL信息完成");
            }
            console("获取所有表加载创建SQL信息完成");

            // 设置每个表是否为中间表
            for (TableInfo tableInfo : tableInfos) {
                tableInfo.setMiddleTable(isMiddleTable(tableInfo));
            }

        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return null;

        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return null;

            // 不管出现什么错误，都要处理异常
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return null;

        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return tableInfos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.com.sby.hbfm.service.mysql.A#loadColumns(java.sql.Connection,
     * java.util.List)
     */
    @Override
    public void loadColumns(Connection connection, List<TableInfo> tableInfos) throws SQLException {

        PreparedStatement stmtTableInfo = null;
        ResultSet rsTableInfo = null;

        // 循环每一张表进行处理
        for (TableInfo tableInfo : tableInfos) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("获取表[" + tableInfo.getTableName() + "]字段开始...");
            }
            console("获取表[" + tableInfo.getTableName() + "]字段开始...");

            try {

                stmtTableInfo = connection.prepareStatement("show full fields from " + tableInfo.getTableName());

                rsTableInfo = stmtTableInfo.executeQuery();

                List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
                tableInfo.setColumnInfos(columnInfos);

                while (rsTableInfo.next()) {

                    ColumnInfo columnInfo = new ColumnInfo();
                    columnInfos.add(columnInfo);

                    columnInfo.setTableInfo(tableInfo);
                    columnInfo.setColumnNameEn(rsTableInfo.getString("FIELD"));

                    String columnType = rsTableInfo.getString("TYPE");
                    if (columnType.indexOf("(") > 0) {
                        String[] columnTypeList = columnType.split("\\(|\\)");
                        columnInfo.setColumnType(columnTypeList[0]);
                        String length = columnTypeList[1];
                        if (length != null && length != "")
                            length = length.split(",")[0];
                        columnInfo.setColumnLength(length);
                    } else {
                        columnInfo.setColumnType(columnType);
                    }
                    columnInfo.setIsCanNull("YES".equalsIgnoreCase(rsTableInfo.getString("NULL")));

                    columnInfo.setDescription(rsTableInfo.getString("COMMENT"));
                    columnInfo.setIsPrimaryKey("PRI".equalsIgnoreCase(rsTableInfo.getString("KEY")));
                }

            } finally {
                if (rsTableInfo != null) {
                    rsTableInfo.close();
                }
                if (stmtTableInfo != null) {
                    stmtTableInfo.close();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.com.sby.hbfm.service.mysql.A#loadFKInfo(java.sql.Connection,
     * cn.com.sby.hbfm.model.TableInfo, java.util.List)
     */
    @Override
    public void loadFKInfo(Connection connection, List<TableInfo> allTableInfos) throws SQLException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("加载外键信息开始...");
        }
        console("加载外键信息开始...");

        StringBuffer sql4ForeignKey = new StringBuffer();
        sql4ForeignKey.append(" SELECT");
        sql4ForeignKey.append(" CONSTRAINT_SCHEMA,");
        sql4ForeignKey.append(" TABLE_NAME,");
        sql4ForeignKey.append(" COLUMN_NAME,");
        sql4ForeignKey.append(" CONSTRAINT_NAME,");
        sql4ForeignKey.append(" REFERENCED_TABLE_NAME,");
        sql4ForeignKey.append(" REFERENCED_COLUMN_NAME");
        sql4ForeignKey.append(" FROM");
        sql4ForeignKey.append(" INFORMATION_SCHEMA.KEY_COLUMN_USAGE");
        sql4ForeignKey.append(" WHERE");
        sql4ForeignKey.append(" TABLE_NAME = ?");

        // ConnectionInfo info =
        // NdddApplication.getInstance().getCurrentConnectionInfo();
        String sechma = null;

        // 首先计算当前使用的sechma
        String sechmaSQL = "select database()";

        PreparedStatement stmtSechma = null;
        ResultSet rsSechma = null;
        try {

            stmtSechma = connection.prepareStatement(sechmaSQL);
            rsSechma = stmtSechma.executeQuery();
            if (rsSechma.next()) {
                sechma = rsSechma.getString(1);
            }

        } finally {
            if (rsSechma != null) {
                rsSechma.close();
            }
            if (stmtSechma != null) {
                stmtSechma.close();
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("当前连接数据库名称为：" + sechma);
        }
        console("当前连接数据库名称为：" + sechma);

        PreparedStatement stmtFKInfo = null;
        ResultSet rsFKInfo = null;

        // 循环每一张表进行处理
        for (TableInfo tableInfo : allTableInfos) {

            try {

                stmtFKInfo = connection.prepareStatement(sql4ForeignKey.toString());
                stmtFKInfo.setString(1, tableInfo.getTableName());

                rsFKInfo = stmtFKInfo.executeQuery();

                List<FKInfo> foreignKeyInfos = new ArrayList<FKInfo>();
                tableInfo.setForeignKeyInfos(foreignKeyInfos);

                // 循环结果集中的每一条结果
                while (rsFKInfo.next()) {

                    // 如果不是我们要处理的SECHMA
                    if (!sechma.equals(rsFKInfo.getString("CONSTRAINT_SCHEMA"))) {
                        continue;
                    }

                    if ("PRIMARY".equals(rsFKInfo.getString("CONSTRAINT_NAME"))) {
                        continue;
                    }

                    // 设置外键关联的数据库表名称
                    String referencedTableName = rsFKInfo.getString("REFERENCED_TABLE_NAME");
                    if (referencedTableName == null) {// 找不到表可能是索引，不是外键。
                        continue;
                    }

                    FKInfo foreignKeyInfo = new FKInfo();
                    foreignKeyInfos.add(foreignKeyInfo);

                    // 设置外键的名称
                    foreignKeyInfo.setForeignKeyName(rsFKInfo.getString("CONSTRAINT_NAME"));

                    foreignKeyInfo.setTableName(tableInfo.getTableName());

                    // 当前字段的名称
                    String columnName = rsFKInfo.getString("COLUMN_NAME");

                    // 当前表的全部字段
                    List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();

                    // 判断当前表的全部字段中是否包含有外键的字段
                    for (ColumnInfo columnInfo : columnInfos) {
                        if (columnInfo.getColumnNameEn().equals(columnName)) {
                            foreignKeyInfo.setColumnInfo(columnInfo);
                            break;
                        }
                    }

                    // 设置外键字段的名称
                    foreignKeyInfo.setColumnName(rsFKInfo.getString("COLUMN_NAME"));

                    // 处理要被关联的字段信息
                    for (TableInfo referencedTableInfo : allTableInfos) {

                        // 判断被关联表的名称是否存在
                        if (referencedTableInfo.getTableName().equals(referencedTableName)) {

                            foreignKeyInfo.setReferenceTableName(referencedTableName);

                            String referenceColumnName = rsFKInfo.getString("REFERENCED_COLUMN_NAME");
                            List<ColumnInfo> referenceColumnInfos = referencedTableInfo.getColumnInfos();

                            for (ColumnInfo referenceColumnInfo : referenceColumnInfos) {
                                if (referenceColumnName.equals(referenceColumnInfo.getColumnNameEn())) {
                                    foreignKeyInfo.setReferenceColumnInfo(referenceColumnInfo);
                                    foreignKeyInfo.setReferenceColumnName(referenceColumnName);

                                    break;
                                }
                            }

                            break;
                        }
                    }

                    // 如果循环完成后，还是没有找到外键字段，则抛出异常
                    if (foreignKeyInfo.getReferenceColumnInfo() == null) {
                        throw new NullPointerException("处理过程中，发现结果错误!外键[" + foreignKeyInfo.getForeignKeyName()
                                + "]对应的表[" + referencedTableName + "]找不到。");
                    }

                }

            } finally {
                if (rsFKInfo != null) {
                    rsFKInfo.close();
                }
                if (stmtFKInfo != null) {
                    stmtFKInfo.close();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.com.sby.hbfm.service.mysql.A#loadTableCreateSQL(java.sql.Connection,
     * cn.com.sby.hbfm.model.TableInfo)
     */
    @Override
    public void loadTableCreateSQL(Connection connection, List<TableInfo> tableInfos) throws SQLException {

        PreparedStatement stmtTableInfo = null;
        ResultSet rsTableInfo = null;

        // 循环每一张表进行处理
        for (TableInfo tableInfo : tableInfos) {

            try {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("加载表[" + tableInfo.getTableName() + "]SQL信息开始...");
                }
                console("加载表[" + tableInfo.getTableName() + "]SQL信息开始...");

                stmtTableInfo = connection.prepareStatement("show create table " + tableInfo.getTableName());

                rsTableInfo = stmtTableInfo.executeQuery();

                while (rsTableInfo.next()) {
                    tableInfo.setSql(rsTableInfo.getString(2));
                }

            } finally {
                if (rsTableInfo != null) {
                    rsTableInfo.close();
                }
                if (stmtTableInfo != null) {
                    stmtTableInfo.close();
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.com.sby.hbfm.service.mysql.A#loadTableNames(java.sql.Connection)
     */
    @Override
    public List<TableInfo> loadTableNames(Connection connection) throws SQLException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("获取表名称开始...");
        }
        console("获取表名称开始...");

        // 定义返回值
        List<TableInfo> resultTableInfos = new ArrayList<TableInfo>();

        PreparedStatement stmtTableNames = null;
        ResultSet rsTableNames = null;

        try {

            stmtTableNames = connection.prepareStatement("SHOW TABLE STATUS");
            rsTableNames = stmtTableNames.executeQuery();

            while (rsTableNames.next()) {

                // 如果字段为视图的话，不进行处理，我们现在只处理表
                if ("VIEW".equals(rsTableNames.getString(18))) {
                    continue;
                }

                TableInfo tableInfo = new TableInfo();
                resultTableInfos.add(tableInfo);

                tableInfo.setTableName(rsTableNames.getString(1));
                tableInfo.setTableDescribe(rsTableNames.getString("Comment"));
            }

        } finally {

            if (rsTableNames != null) {
                try {
                    rsTableNames.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            if (stmtTableNames != null) {
                try {
                    stmtTableNames.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

        return resultTableInfos;
    }

    /**
     * 校验表，校验不通过的表不能持久化到文件
     * 
     * @param tableInfo
     * @return
     */
    private boolean isMiddleTable(TableInfo tableInfo) {
        if (tableInfo == null) {
            return false;
        }

        // 两个字段均是主键则认为是中间表
        // 校验表是否是中间表，中间表不生成
        List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();
        if (columnInfos != null && columnInfos.size() == 2) {

            for (ColumnInfo columnInfo : columnInfos) {

                if (!columnInfo.getIsPrimaryKey()) {
                    return false;
                }
            }
            // 存在两个字段，并且两个字段都是主键。
            return true;
        }
        return false;
    }

}

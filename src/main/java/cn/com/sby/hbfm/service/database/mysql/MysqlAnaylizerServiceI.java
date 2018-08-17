package cn.com.sby.hbfm.service.database.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.com.sby.hbfm.gui.console.ConsoleDialog;
import cn.com.sby.hbfm.model.ConnectionInfo;
import cn.com.sby.hbfm.model.DBConfigItem;
import cn.com.sby.hbfm.model.TableInfo;

/**
 * 抽象类
 * 
 *
 *
 */
public interface MysqlAnaylizerServiceI {

    /**
     * 设置消息提示框
     * 
     * @param console
     */
    public void setConsole(ConsoleDialog console);

    public List<DBConfigItem> getVariables(ConnectionInfo info);

    /**
     * 加载数据库中的表信息
     * 
     * @param connection
     * @return
     */
    public List<TableInfo> loadTableInfos(ConnectionInfo connectionInfo) throws SQLException;

    /**
     * 加载数据库中的表名称以及描述信息
     * 
     * @param connection
     * @return
     */
    public List<TableInfo> loadTableNames(Connection connection) throws SQLException;

    /**
     * 加载数据库中的字段信息
     * 
     * @param connection
     * @return
     */
    public void loadColumns(Connection connection, List<TableInfo> tableInfos) throws SQLException;

    /**
     * 加载数据库中的外键信息
     * 
     * @param connection
     * @return
     */
    public void loadFKInfo(Connection connection, List<TableInfo> allTableInfos) throws SQLException;

    /**
     * 加载一个表的建表语句
     * 
     * @param connection
     * @return
     */
    public void loadTableCreateSQL(Connection connection, List<TableInfo> tableInfos) throws SQLException;

}

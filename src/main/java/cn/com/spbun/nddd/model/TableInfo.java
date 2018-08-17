package cn.com.spbun.nddd.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 数据库表的描述类<br>
 * xml相关注解的字段为将对象写入到xml中的写入规则
 * 
 * @author 杨彪
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tableInfos")
@XmlType(name = "tableInfo", propOrder = { "tableName", "tableDescribe", "columnInfos", "foreignKeyInfos", "sql" })
public class TableInfo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 9021622450652857140L;

    @XmlElementWrapper(name = "columnInfos")
    @XmlElement(name = "columnInfo")
    private List<ColumnInfo> columnInfos = null;

    @XmlElementWrapper(name = "foreignKeyInfos")
    @XmlElement(name = "foreignKeyInfo")
    private List<FKInfo> foreignKeyInfos = null;

    @XmlElement(name = "sql", required = true)
    private String sql = null;

    @XmlElement(name = "tableDescribe", required = true)
    private String tableDescribe = null;

    @XmlElement(name = "tableName", required = true)
    private String tableName = null;

    /** 标识该表是否为中间表，比如存在两张表，用户表，角色表。那么用户角色表就为中间表。特点：只有两个字段，并且两个字段同时为主键 */
    private boolean isMiddleTable = false;

    public boolean isMiddleTable() {
        return isMiddleTable;
    }

    public void setMiddleTable(boolean isMiddleTable) {
        this.isMiddleTable = isMiddleTable;
    }

    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    public List<FKInfo> getForeignKeyInfos() {
        return foreignKeyInfos;
    }

    public String getSql() {
        return sql;
    }

    public String getTableDescribe() {
        return tableDescribe;
    }

    public String getTableName() {
        return tableName;
    }

    public void setColumnInfos(List<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }

    public void setForeignKeyInfos(List<FKInfo> foreignKeyInfos) {
        this.foreignKeyInfos = foreignKeyInfos;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setTableDescribe(String tableDescribe) {
        this.tableDescribe = tableDescribe;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

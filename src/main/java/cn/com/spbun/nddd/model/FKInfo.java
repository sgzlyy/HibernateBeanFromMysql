package cn.com.spbun.nddd.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * 外键信息，用来标识一个表的字段与另外一个表的字段的关联关系<br>
 * 从ForeignKeyInfo->FKInfo
 * 
 * @author NOLY DAKE
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "foreignKeyInfos")
@XmlType(name = "foreignKeyInfo", propOrder = { "foreignKeyName", "tableName", "columnName", "referenceTableName",
        "referenceColumnName" })
public class FKInfo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4212233642533752207L;

    /**
     * 外键名称
     */
    private String foreignKeyName = null;

    /**
     * 当前字段信息
     */
    @XmlTransient
    private ColumnInfo columnInfo = null;

    @XmlElement(name = "tableName", required = true)
    private String tableName = null;

    @XmlElement(name = "columnName", required = true)
    private String columnName = null;

    /**
     * 引用的字段信息
     */
    @XmlTransient
    private ColumnInfo referenceColumnInfo = null;

    @XmlElement(name = "referenceTableName", required = true)
    private String referenceTableName = null;

    @XmlElement(name = "referenceColumnName", required = true)
    private String referenceColumnName = null;

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public ColumnInfo getColumnInfo() {
        return columnInfo;
    }

    public void setColumnInfo(ColumnInfo columnInfo) {
        this.columnInfo = columnInfo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ColumnInfo getReferenceColumnInfo() {
        return referenceColumnInfo;
    }

    public void setReferenceColumnInfo(ColumnInfo referenceColumnInfo) {
        this.referenceColumnInfo = referenceColumnInfo;
    }

    public String getReferenceTableName() {
        return referenceTableName;
    }

    public void setReferenceTableName(String referenceTableName) {
        this.referenceTableName = referenceTableName;
    }

    public String getReferenceColumnName() {
        return referenceColumnName;
    }

    public void setReferenceColumnName(String referenceColumnName) {
        this.referenceColumnName = referenceColumnName;
    }

}

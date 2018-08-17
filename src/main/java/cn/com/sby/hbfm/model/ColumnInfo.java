package cn.com.sby.hbfm.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "columnInfos")
@XmlType(name = "columnInfo", propOrder = { "sequence", "columnNameEn", "columnType", "columnLength",
        "columnDecimalsLength", "isCanNull", "isPrimaryKey", "defaultValue", "description" })
public class ColumnInfo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8963383714769257336L;

    /**
     * 字段序号
     */
    @XmlElement(name = "sequence", required = true)
    private Integer sequence = null;

    /**
     * 字段名称英文
     */
    @XmlElement(name = "columnNameEn", required = true)
    private String columnNameEn = null;

    /**
     * 字段类型
     */
    @XmlElement(name = "columnType", required = true)
    private String columnType = null;

    /**
     * 字段长度
     */
    @XmlElement(name = "columnLength", required = true)
    private String columnLength = null;

    /**
     * 字段小数长度
     */
    @XmlElement(name = "columnDecimalsLength", required = true)
    private String columnDecimalsLength = null;

    /**
     * 是否允许空值
     */
    @XmlElement(name = "isCanNull", required = true)
    private Boolean isCanNull = null;

    /**
     * 是否为主键
     */
    @XmlElement(name = "isPrimaryKey", required = true)
    private Boolean isPrimaryKey = null;

    /**
     * 默认值
     */
    @XmlElement(name = "defaultValue", required = true)
    private String defaultValue = null;

    /**
     * 说明
     */
    @XmlElement(name = "description", required = true)
    private String description = null;

    @XmlTransient
    private TableInfo tableInfo = null;

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getColumnDecimalsLength() {
        return columnDecimalsLength;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public String getColumnNameEn() {
        return columnNameEn;
    }

    public String getColumnType() {
        return columnType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsCanNull() {
        return isCanNull;
    }

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setColumnDecimalsLength(String columnDecimalsLength) {
        this.columnDecimalsLength = columnDecimalsLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public void setColumnNameEn(String columnNameEn) {
        this.columnNameEn = columnNameEn;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsCanNull(Boolean isCanNull) {
        this.isCanNull = isCanNull;
    }

    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}

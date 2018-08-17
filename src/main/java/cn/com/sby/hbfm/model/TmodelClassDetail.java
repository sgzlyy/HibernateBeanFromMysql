package cn.com.sby.hbfm.model;

import java.util.List;

/**
 * Tmodel对象的描述类
 * 
 *
 *
 */
public class TmodelClassDetail {

    /**
     * 类名称
     */
    private String className = null;

    private String tableDescribe = null;

    /**
     * 是否有日期类型
     */
    private boolean hasDateField = false;

    /**
     * 是否有decimal类型BigDecimal
     */
    private boolean hasDecimalField = false;

    /**
     * 是否有外键
     */
    private boolean hasFK = false;

    /**
     * 是否中间表字段
     */
    private boolean isMDK = false;

    /**
     * 是否有被其他表引用的字段
     */
    private boolean hasRK = false;

    private List<TmodelFieldDetail> fieldDetails = null;

    private String tableName = null;

    /**
     * 实例名称
     */
    private String instanceName = null;

    /**
     * 包名
     */
    private String pkgName = null;

    public String getClassName() {
        return className;
    }

    public List<TmodelFieldDetail> getFieldDetails() {
        return fieldDetails;
    }

    public boolean getHasDateField() {
        return hasDateField;
    }

    public boolean getHasFK() {
        return hasFK;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public boolean getIsMDK() {
        return isMDK;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getTableDescribe() {
        return tableDescribe;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isHasDecimalField() {
        return hasDecimalField;
    }

    public boolean isHasRK() {
        return hasRK;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setFieldDetails(List<TmodelFieldDetail> fieldDetails) {
        this.fieldDetails = fieldDetails;
    }

    public void setHasDateField(boolean hasDateField) {
        this.hasDateField = hasDateField;
    }

    public void setHasDecimalField(boolean hasDecimalField) {
        this.hasDecimalField = hasDecimalField;
    }

    public void setHasFK(boolean hasFK) {
        this.hasFK = hasFK;
    }

    public void setHasRK(boolean hasRK) {
        this.hasRK = hasRK;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setMDK(boolean isMDK) {
        this.isMDK = isMDK;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setTableDescribe(String tableDescribe) {
        this.tableDescribe = tableDescribe;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

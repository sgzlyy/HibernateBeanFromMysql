package cn.com.spbun.nddd.model;

/**
 * Tmodel对象里面的成员变量的描述类
 * 
 * @author NOLY DAKE
 *
 */
public class TmodelFieldDetail {

    /** 字段是否可以为空 */
    private boolean canNull = false;

    /** 字段名称 */
    private String columnEnName = null;

    /** 字段的长度 */
    private String columnLength = null;

    /** 是否有日期类型 */
    private boolean hasDateField = false;

    /** 表示当前字段是否引用了其他表的字段 */
    private boolean isFK = false;

    /**
     * 是否包含中间表字段<br>
     * mdk->Middle Key<br>
     * 如果包含的话，模板生成的时候会引用 ManyToMany;JoinTable;
     */
    private boolean isMDK = false;

    /** 是否主键 */
    private boolean isPK = false;

    /** 是否被其他表引用 */
    private boolean isRK = false;

    /** 字段注释 */
    private String description = null;

    /**
     * 字段的实例名称<br>
     * 比如数据库表为jump_higher，那么“对象”为JumpHigher、“实例”名称为jumpHigher
     */
    private String instanceName = null;
    /**
     * 字段的Java对象类型<br>
     * 比如数据库表为jump_higher，那么“对象”为JumpHigher、“实例”名称为jumpHigher
     */
    private String clazzName = null;

    /** 有中间表时，表示对方在中间表的字段名称 */
    private String mdkColumnName = null;
    /**
     * 有中间表时，表示对方在中间表名称
     */
    private String mdkTableName = null;

    /**
     * 字段的实例名称<br>
     * 用来生成OneToMany:mappedBy使用。因为OneToMany的时候，instanceName会被转换成复数，因此需要单独一个变量存储单数的名称
     * 
     */
    private String rkInstanceName = null;

    public boolean getCanNull() {
        return canNull;
    }

    public String getClazzName() {
        return clazzName;
    }

    public String getColumnEnName() {
        return columnEnName;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public String getDescription() {
        return description;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public boolean getIsFK() {
        return isFK;
    }

    public boolean getIsMDK() {
        return isMDK;
    }

    public boolean getIsPK() {
        return isPK;
    }

    public boolean getIsRK() {
        return isRK;
    }

    public String getMdkColumnName() {
        return mdkColumnName;
    }

    public String getMdkTableName() {
        return mdkTableName;
    }

    public String getRkInstanceName() {
        return rkInstanceName;
    }

    public boolean isHasDateField() {
        return hasDateField;
    }

    public void setCanNull(boolean canNull) {
        this.canNull = canNull;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public void setColumnEnName(String columnEnName) {
        this.columnEnName = columnEnName;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFK(boolean isFK) {
        this.isFK = isFK;
    }

    public void setHasDateField(boolean hasDateField) {
        this.hasDateField = hasDateField;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setMDK(boolean isMDK) {
        this.isMDK = isMDK;
    }

    public void setMdkColumnName(String mdkColumnName) {
        this.mdkColumnName = mdkColumnName;
    }

    public void setMdkTableName(String mdkTableName) {
        this.mdkTableName = mdkTableName;
    }

    public void setPK(boolean isPK) {
        this.isPK = isPK;
    }

    public void setRK(boolean isRK) {
        this.isRK = isRK;
    }

    public void setRkInstanceName(String rkInstanceName) {
        this.rkInstanceName = rkInstanceName;
    }

}

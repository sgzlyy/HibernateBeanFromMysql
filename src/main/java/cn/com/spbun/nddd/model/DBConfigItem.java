package cn.com.spbun.nddd.model;

public class DBConfigItem {

    public DBConfigItem(String key, String value) {
        this(key, value, null);
    }

    public DBConfigItem(String key, String value, String describe) {
        this.key = key;
        this.value = value;
        this.describe = describe;
    }

    private String key = null;

    private String value = null;

    private String describe = null;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

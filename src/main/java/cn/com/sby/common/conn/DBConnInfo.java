package cn.com.sby.common.conn;

/**
 * 数据库连接信息
 * 
 *
 * 
 */
public class DBConnInfo {

    /**
     * MYSQL5.5的数据库类型
     */
    public static final String SERVER_TYPE_MYSQL_5_5 = "SERVER_TYPE_MYSQL_5_5";

    /** 数据库类型 */
    private String serverType = null;

    /** 主机名或IP */
    private String serverIP = null;

    /** 主机端口 */
    private String serverPort = null;

    /** 用户名 */
    private String userName = null;

    /** 密码 */
    private String password = null;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /** 数据库名称 */
    private String schemaName = null;

    public DBConnInfo(String serverType) {
        super();
        this.serverType = serverType;
    }

    public String getPassword() {
        return password;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getServerType() {
        return serverType;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

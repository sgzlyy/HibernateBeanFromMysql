package cn.com.sby.common.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mysql5x5ConnectionHelper {

    public static Connection getConnection(DBConnInfo dbConnInfo) throws ClassNotFoundException, SQLException {

        // 连续数据库
        Connection conn = null;

        // 加载驱动程序
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + dbConnInfo.getServerIP() + ":" + dbConnInfo.getServerPort() + "/"
                    + dbConnInfo.getSchemaName();

            // 连续数据库
            conn = DriverManager.getConnection(url, dbConnInfo.getUserName(), dbConnInfo.getPassword());

            return conn;

        } catch (ClassNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        }
    }
}

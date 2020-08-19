package org.xingze.etl.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author mysqltutorial.org
 */
public class MySQLJDBCUtil {

    /**
     * Get database connection
     *
     * @return a Connection object
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = null;

        // assign db parameters
        String url = "jdbc:mysql://10.8.8.61:3306/xingze";
        String user = "root";
        String password = "123456";

        // create a connection to the database
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

}

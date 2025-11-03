package com.igor.jsfgraph.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for handling JDBC connections.
 */
public class JDBCUtils {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            Properties info = new Properties();
            info.load(JDBCUtils.class.getClassLoader().getResourceAsStream("/db.cfg"));
            driver = info.getProperty("javax.persistence.jdbc.driver");
            url = info.getProperty("javax.persistence.jdbc.url");
            user = info.getProperty("javax.persistence.jdbc.user");
            password = info.getProperty("javax.persistence.jdbc.password");
            Class.forName(driver);
        } catch (Throwable ex) {
            System.err.println("Something went wrong during initializing JDBC: " + ex);
            throw new ExceptionInInitializerError();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

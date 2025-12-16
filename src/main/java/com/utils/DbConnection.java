package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static Connection connection;

    public static Connection getConnection(
            String host,
            String port,
            String database,
            String user,
            String password
    ) throws SQLException {

        String url = String.format(
                "jdbc:postgresql://%s:%s/%s",
                host, port, database
        );

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}

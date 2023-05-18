package com.stigglespatch.main.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String HOST = "na01-sql.pebblehost.com";
    private final int PORT = 3306;
    private final String DATABASE = "customer_491430_stiggles";
    private final String USERNAME = "customer_491430_stiggles";
    private final String PASSWORD = "f33V#gt@N-b4wUAyyZhD";

    private Connection connection;

    public void connect() throws SQLException {
        connection= DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false",
                USERNAME,
                PASSWORD
        );
    }
    public boolean isConnected(){ return connection != null; }

    public Connection getConnection() { return connection; }

    public void disconnect() {
        if (isConnected()){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

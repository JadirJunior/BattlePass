package com.battle.battlepass.battlepass.database;

import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static String host;
    public static String port;
    public static String username;
    public static String password;
    public static String database;
    private Connection connection;

    private final String connectionString = "jdbc:mysql://" + DatabaseConnection.host + ":"
            + DatabaseConnection.port + "/" + DatabaseConnection.database;

    
    public DatabaseConnection() {
        try {
            connect();
        } catch(Exception err) {
            Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&4Erro ao conectar ao database."));
        }
    }

    private void connect() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection(connectionString, DatabaseConnection.username, DatabaseConnection.password);
    }

    public Connection getConnection() { return connection; }

    public void disconnect() {
        try {
            if (!connection.isClosed()) {
                getConnection().close();
            }
        } catch (Exception err) {
            Bukkit.getConsoleSender().sendMessage(ChatUtil.format("&4Ocorreu um erro ao desconectar do database."));
        }
    }



}

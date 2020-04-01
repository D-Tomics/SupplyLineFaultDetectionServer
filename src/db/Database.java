package db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private String usrname = "root";
    private String password = "root";
    private String url = "jdbc:mysql://localhost/usersDB";

    private String name;
    private String host = "localhost";

    private Connection connection;
    private boolean connected;
    private Statement statement;

    public static Database instance ;
    public static Database getDatabase(String name) {
        if(instance == null)
            instance = new Database();
        instance.name = name;
        instance.url = "jdbc:mysql://" + instance.host + "/" + name;
        return instance;
    }

    public static Database getDatabase(String dbName, String userName, String password, String host, String url) {
        if(instance == null) 
            instance = new Database();
        instance.name = dbName;
        instance.host = host;
        instance.url = url;
        instance.usrname = userName;
        instance.password = password;
        return instance;
    }

    public static void setHost(String host) {
        instance.host = host;
    }

    private Database() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
             e.printStackTrace();
        }
    }

    public void connect() throws SQLException {
        if (!connected) {
            connection = DriverManager.getConnection(url, usrname, password);
            statement = connection.createStatement();    
            connected = true;
        }
    }

    public PreparedStatement createPreparedStatement(String sql) {
        try {
            if(!connected) connect();
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
       return null;
    }

    public boolean execute(String sql) {
        try {
            if(!connected) connect();
            return statement.execute(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public ResultSet executeQuery(String sql) {
        try {
            if(!connected) connect();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public int executeUpdate(String sql) {
        try {
            if(!connected) connect();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }
    
    public boolean execute(PreparedStatement statement) {
        try {
            if(!connected) connect();
            return statement.execute();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public ResultSet executeQuery(PreparedStatement statement) {
        try {
            if(!connected) connect();
            return statement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public int executeUpdate(PreparedStatement statement) {
        try {
            if(!connected) connect();
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    } 

    public void close() {
        try {
            if(!connected) return;
            connected = false;
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getName() { return name; }

    public Table getTable(String name) {
        return new Table(name, this);
    }
}
package pl.arozenek.ticketbooking;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.LinkedList;
//import java.util.List;


@Component
public class Database {

    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:src/main/resources/database.db";

    private Connection conn;
    private Statement stat;

    public Database() {
        try {
            Class.forName(Database.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("No JDBC driver");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Cannot make connection with database");
            e.printStackTrace();
        }
    }
}

package pl.arozenek.ticketbooking;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


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

        //Tests, to remove later
        List<Screening> li = selectScreening(61520000000000L, 71519975200000L);
        for (Screening i: li) {
            System.err.println(i);
        }
    }


    //Select all screenings from database
    public List<Screening> selectScreening() {

        List<Screening> screenings = new LinkedList<Screening>();
        try {
            ResultSet result = stat.executeQuery("SELECT IdScreening, IdRoom, screening.IdMovie, Time, Title " +
                    "FROM screening LEFT JOIN movies ON screening.IdMovie = movies.IdMovie;");
            int idScreening, idRoom, idMovie;
            long time;
            String title;

            while (result.next()) {
                idScreening = result.getInt("IdScreening");
                idRoom = result.getInt("IdRoom");
                time = result.getLong("Time");
                idMovie = result.getInt("IdMovie");
                title = result.getString("Title");
                screenings.add(new Screening(idScreening, idRoom, time, idMovie, title));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return screenings;
    }


    //Select from database all films between @beginPeriod and @endPeriod
    public List<Screening> selectScreening(long beginPeriod, long endPeriod) {
        List<Screening> screenings = new LinkedList<Screening>();
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "SELECT IdScreening, IdRoom, screening.IdMovie, Time, Title " +
                    "FROM screening LEFT JOIN movies ON screening.IdMovie = movies.IdMovie " +
                    "WHERE Time >= ? AND Time <= ?;");

            prepStmt.setLong(1,beginPeriod);
            prepStmt.setLong(2,endPeriod);

            ResultSet result = prepStmt.executeQuery();

            int idScreening, idRoom, idMovie;
            long time;

            String title;
            while (result.next()) {
                idScreening = result.getInt("IdScreening");
                idRoom = result.getInt("IdRoom");
                time = result.getLong("Time");
                idMovie = result.getInt("IdMovie");
                title = result.getString("Title");
                screenings.add(new Screening(idScreening, idRoom, time, idMovie, title));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return screenings;
    }


    //Give from database title of the movie
    public String selectTitleMovie(int idMovie) {
        String nameMovie = null;
        try {
            PreparedStatement prepStmt = conn.prepareStatement("select title from movies where IdMovie=?");
            prepStmt.setInt(1, idMovie);
            ResultSet result = prepStmt.executeQuery();
            if (result.next()) {
                nameMovie = result.getString("Title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return nameMovie;
    }
}

package pl.arozenek.ticketbooking;

import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


@Repository
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
        /*Reservation res = new Reservation("Adamk", "Rozenek",
                1, 61520000000000L, 2.50, 4,3);
        insertReservation(res);

        List<Seat> li = selectReservedSeat(1);
        for (Seat i : li) {
            System.err.println(i);
        }*/
    }


    //Select all screenings from database
    public List<Screening> selectScreening() {

        List<Screening> screenings = new LinkedList<Screening>();
        try {
            ResultSet result = stat.executeQuery("SELECT IdScreening, IdRoom, screening.IdMovie, Time, Title " +
                    "FROM screening LEFT JOIN movies ON screening.IdMovie = movies.IdMovie ORDER BY Title DESC, Time ASC;");
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
                     "WHERE Time >= ? AND Time <= ? ORDER BY Title ASC, Time ASC;;");

            prepStmt.setLong(1, beginPeriod);
            prepStmt.setLong(2, endPeriod);

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
            PreparedStatement prepStmt = conn.prepareStatement("SELECT title FROM movies WHERE IdMovie=?");
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


    //Insert reservation to database. Reservation @res is suppose to be valid
    public boolean insertReservation(Reservation res) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "INSERT INTO reservation VALUES (?, ?, ?, ?, ?, ?, ?)");
            prepStmt.setString(1, res.getClientName());
            prepStmt.setString(2, res.getClientSurname());
            prepStmt.setInt(3, res.getIdScreening());
            prepStmt.setLong(4, res.getTimeReservation());
            prepStmt.setDouble(5, res.getPrice());
            prepStmt.setInt(6, res.getRow());
            prepStmt.setInt(7, res.getSeat());
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    //Select all seats reserved for specific screening
    public List<Seat> selectReservedSeat(int idScreening) {
        List<Seat> seats = new LinkedList<>();
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "SELECT Row, Seat FROM reservation WHERE IdScreening=? ORDER BY Row, Seat ASC;");
            prepStmt.setInt(1, idScreening);

            ResultSet resuS = prepStmt.executeQuery();

            int row, seat;

            while (resuS.next()) {
                row = resuS.getInt("Row");
                seat = resuS.getInt("Seat");
                seats.add(new Seat(row, seat));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return seats;
    }

}
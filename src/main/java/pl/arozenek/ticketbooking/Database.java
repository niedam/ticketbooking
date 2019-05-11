package pl.arozenek.ticketbooking;

import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;



@Repository
public class Database {

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database.db";

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


    //Select from database all films between @beginPeriod and @endPeriod
    protected List<Screening> selectScreening(long beginPeriod, long endPeriod) {
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


    //Select all screenings from database
    protected List<Screening> selectScreening() {
        return selectScreening(0L, Long.MAX_VALUE);
    }


    //Give from database title of the movie
    //@idMovie - id of movie in database
    protected String selectTitleMovie(int idMovie) {
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


    //Return screening's idRoom
    //@idScreening - id of particular screening
    protected int selectIdRoom(int idScreening) {
        int selectedResult = -1;
        try {
            PreparedStatement prepStmt = conn.prepareStatement("SELECT IdRoom FROM screening WHERE IdScreening=?");
            prepStmt.setInt(1, idScreening);
            ResultSet result = prepStmt.executeQuery();
            if (result.next()) {
                selectedResult = result.getInt("IdRoom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return selectedResult;
    }


    //Insert reservation to database. Reservation @res is suppose to be valid
    protected boolean insertReservation(Reservation res) {
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
    //@idScreening - id of particular screening
    protected List<Seat> selectReservedSeat(int idScreening) {
        List<Seat> seats = new LinkedList<Seat>();
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


    //Create list containing number of rows in room and number of seats in every row
    //@idRoom - id of room in database
    protected List<Object> selectRowsRoom(int idRoom) {
        List<Object> listResult = new LinkedList<Object>();

        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "SELECT Rows, RowsLength FROM rooms WHERE IdRoom=?;");
            prepStmt.setInt(1, idRoom);

            ResultSet resuS = prepStmt.executeQuery();

            int rows;
            String rowsLength;

            if (resuS.next()) {
                rows = resuS.getInt("Rows");
                rowsLength = resuS.getString("RowsLength");
                listResult.add(rows);
                listResult.add(rowsLength);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return listResult;

    }


    //Return time of particular screening
    //@idScreening - id of particular screening
    protected long selectScreeningTime(int idScreening) {
        long result = -1;
        try {
            PreparedStatement prepStmt = conn.prepareStatement("SELECT Time FROM screening WHERE IdScreening=?");
            prepStmt.setInt(1, idScreening);
            ResultSet resultS = prepStmt.executeQuery();
            if (resultS.next()) {
                result = resultS.getLong("Time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return result;
    }


    //Remove all reservation
    protected boolean deleteAllReservation() {
        try {
            stat.execute("DELETE FROM reservation;");

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }


    //Try undo inserted reservations
    //@name, @surname - infomation about client
    //@timeRes - time of reservation created
    protected void deleteReservation(String name, String surname, long timeRes) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement("DELETE FROM reservation WHERE name=? AND " +
                    "surname=? AND TimeReservation=?");

            prepStmt.setString(1, name);
            prepStmt.setString(2, surname);
            prepStmt.setLong(3, timeRes);

            prepStmt.execute();
        } catch (SQLException e) {
            return;
        }
    }
}
package pl.arozenek.ticketbooking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


public class ReservationSystem {

    protected static List<Seat> availableSeats(int rows, String seats,
                                               LinkedList<Seat> occupiedSeats) {

        String[] seatsInRows = seats.split("/");
        LinkedList <Seat> result = new LinkedList<Seat>();

        int numOfSeats;

        occupiedSeats.add(new Seat(rows + 1, 0));

        for (int i = 1; i <= rows; i++) {
            numOfSeats = Integer.parseInt(seatsInRows[i - 1]);
            for (int j = 1; j <= numOfSeats; j++) {
                Seat front = occupiedSeats.getFirst();
                if (front.getRow() != i || front.getSeat() != j) {
                    result.add(new Seat(i, j));
                } else {
                    occupiedSeats.pop();
                }
            }

        }

        return result;
    }

    protected static List<Seat> freeSeatOnScreening(Database db, int idScreening) {

        int idRoom = db.selectIdRoom(idScreening);
        return freeSeatOnScreening(db, idScreening, idRoom);

    }

    protected static List<Seat> freeSeatOnScreening(Database db, int idScreening, int idRoom) {

        List<Object> roomInfo = db.selectRowsRoom(idRoom);

        int rows = (int) roomInfo.get(0);
        String rowsLength = (String) roomInfo.get(1);

        List<Seat> occupiedSeats = db.selectReservedSeat(idScreening);

        List<Seat> freeSeats = ReservationSystem.availableSeats(rows, rowsLength,
                (LinkedList<Seat>) occupiedSeats);

        return freeSeats;
    }
}
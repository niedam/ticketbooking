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
}

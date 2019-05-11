package pl.arozenek.ticketbooking;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.util.*;


@RestController
public class Router {

    @Autowired
    private Database database;


    @PostMapping(path = "/date", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> getDate(@RequestBody DateForm date) {
        return ResponseEntity.ok(date.toLong() + "\n");
    }


    @PostMapping(path = "/screening", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> screeningsString(@RequestBody TimePeriod p) {
        List <Screening> screeningList = database.selectScreening(p.begin, p.end);

        if (screeningList == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Screening cannot be found.\n");

        if (screeningList.isEmpty())
            return ResponseEntity.ok("There's no screening in this interval\n");

        StringBuilder result = new StringBuilder();
        for (Screening i: screeningList) {
            result.append(i);
            result.append('\n');
        }
        return ResponseEntity.ok(result.toString());
    }


    @GetMapping(path = "/available_seat/", produces = "text/plain")
    public ResponseEntity<String> available_seat(@RequestParam(name = "id") int idScreening) {

        int idRoom = database.selectIdRoom(idScreening);

        if (idRoom == -1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Screening not found.\n");

        List<Seat> freeSeats = ReservationSystem.freeSeatOnScreening(database, idScreening, idRoom);

        if (freeSeats.isEmpty())
            return ResponseEntity.ok("There is no place available.\n");

        if (freeSeats == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Seats cannot be found.\n");

        StringBuilder result = new StringBuilder("Available seats in room "+
                idRoom +":\n");


        for (Seat i: freeSeats) {
            result.append(i);
            result.append('\n');
        }

        return ResponseEntity.ok(result.toString());
    }


    @PostMapping(path = "/reservation", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> makeReservation(@RequestBody SimpleReservation reservation) {

        long currentTime = System.currentTimeMillis();

        String name = reservation.name;
        String surname = reservation.surname;
        int idScreening = reservation.idScreening;
        long screeningTime = database.selectScreeningTime(idScreening);

        double price = 0.0;

        if (screeningTime - 900000L < currentTime)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seats can be booked at least"
                    + " 15 minutes before the screenings begins.\n");


        SeatRes[] seats = reservation.seats;
        Arrays.sort(seats, SeatRes.compare);

        for (int i = 1; i < seats.length; i++) {
            if (seats[i].row - seats[i - 1].row > 1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot be a single place left over in"
                        + " row between two already reserved places\n");
        }

        List <Seat> listFreeSeat = ReservationSystem.freeSeatOnScreening(database,idScreening);
        Seat[] freeSeat = listFreeSeat.toArray(new Seat[listFreeSeat.size()]);

        Arrays.sort(freeSeat, Seat.compare);

        for (SeatRes i: seats) {

            if (Arrays.binarySearch(freeSeat, i, Seat.compare) < 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Place " + i + " is not available.\n");
            }

            if (i.getTicketType() != 'A' && i.getTicketType() != 'S' && i.getTicketType() != 'C')
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad ticket type for place " + i + "\n");

        }

        double finalPrice = 0.00;

        for (SeatRes i: seats) {
            if (i.getTicketType() == 'A')
                price = 25.00;
            else if (i.getTicketType() == 'S')
                price = 18.00;
            else if (i.getTicketType() == 'C')
                price = 12.50;

            finalPrice += price;

            if (!database.insertReservation(new Reservation(name, surname, idScreening,
                    currentTime, price, i.getRow(), i.getSeat()))) {
                database.deleteReservation(name, surname, currentTime);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Reservation has not been made properly.");
            }


        }

        Date expirationTime = new Date(screeningTime - 900000L);

        return ResponseEntity.status(HttpStatus.CREATED).body(name + " " + surname + ", you have made reservation." +
                                                                " Total price: " + finalPrice +
                                                                " PLN, your reservation expire on " + expirationTime + "\n");
    }


    @DeleteMapping(path = "/reset_reservations", produces = "text/plain")
    public ResponseEntity<String> resetReservations() {

        if (!database.deleteAllReservation())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Reservations could not be deleted.\n");

        return ResponseEntity.ok("All stored reservations has been deleted.\n");
    }

}
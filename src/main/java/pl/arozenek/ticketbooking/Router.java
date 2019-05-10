package pl.arozenek.ticketbooking;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.util.*;


@RestController
public class Router {

    @Autowired
    private Database database;

    @PostMapping(path = "/screening", consumes = "application/json", produces = "text/plain")
    public String screeningsString(@RequestBody TimePeriod p) {
        List <Screening> screeningList = database.selectScreening(p.begin, p.end);
        StringBuilder result = new StringBuilder();
        for (Screening i: screeningList) {
            result.append(i);
            result.append('\n');
        }
        return result.toString();
    }


    @PostMapping(path = "/screening/json", consumes = "application/json", produces = "application/json")
    public List <Screening> screeningsList(@RequestBody TimePeriod p) {
        return database.selectScreening(p.begin, p.end);
    }


    @GetMapping(path = "/available_seat/", produces = "text/plain")
    public ResponseEntity<String> available_seat(@RequestParam(name = "id") int idScreening) {

        int idRoom = database.selectIdRoom(idScreening);

        if (idRoom == -1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Screening not found.\n");

        List<Seat> freeSeats = ReservationSystem.freeSeatOnScreening(database, idScreening, idRoom);

        StringBuilder result = new StringBuilder("Available seats in room "+
                database.selectIdRoom(idScreening) +":\n");


        for (Seat i: freeSeats) {
            result.append(i);
            result.append('\n');
        }

        return ResponseEntity.ok(result.toString());
    }


    @GetMapping(path = "/available_seat/json/", produces = "application/json")
    public ResponseEntity<List<Seat>> available_seatList(@RequestParam(name = "id") int idScreening) {

        int idRoom = database.selectIdRoom(idScreening);

        if (idRoom == -1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LinkedList<>());

        List<Seat> freeSeats = ReservationSystem.freeSeatOnScreening(database, idScreening, idRoom);

        return ResponseEntity.ok(freeSeats);
    }


    @PostMapping(path = "/reservation", consumes = "application/json", produces = "application/json")
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

        double finalPrice = 0.0;

        for (SeatRes i: seats) {
            if (i.getTicketType() == 'A')
                price = 25.0;
            else if (i.getTicketType() == 'S')
                price = 18.0;
            else if (i.getTicketType() == 'C')
                price = 12.5;

            finalPrice += price;

            database.insertReservation(new Reservation(name, surname, idScreening,
                    currentTime, price, i.getRow(), i.getSeat()));


        }

        Date expirationTime = new Date(screeningTime - 900000L);

        return ResponseEntity.status(HttpStatus.CREATED).body("You have made reservation. Total price: " + finalPrice +
                                                                "PLN, your reservation expire on " + expirationTime + "\n");
    }

}
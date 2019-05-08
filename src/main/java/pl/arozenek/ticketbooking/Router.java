package pl.arozenek.ticketbooking;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

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


    @GetMapping(path = "/available_seat/", produces = "application/json")
    public String available_seat(@RequestParam(name = "id") int idScreening) {

        List<Seat> occupiedSeats = database.selectReservedSeat(idScreening);
        List<Seat> freeSeats = ReservationSystem.availableSeats(4,"4/5/5/6",
                (LinkedList<Seat>) occupiedSeats);

        StringBuilder result = new StringBuilder("Available seats:\n");


        for (Seat i: freeSeats) {
            result.append(i);
            result.append('\n');
        }

        return result.toString();
    }


    @GetMapping(path = "/available_seat/json", produces = "application/json")
    public List<Seat> available_seatList(@RequestParam(name = "id") int idScreening) {

        List<Seat> occupiedSeats = database.selectReservedSeat(idScreening);
        return ReservationSystem.availableSeats(4,"4/5/5/6",
                (LinkedList<Seat>) occupiedSeats);
    }


}
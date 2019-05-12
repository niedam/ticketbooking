package pl.arozenek.ticketbooking;

/* Class for /reservation endpoint */

public class SeatRes extends Seat {
    private char ticketType;

    public SeatRes(int row, int seat, char ticketType) {
        super(row, seat);
        this.ticketType = ticketType;
    }

    protected char getTicketType() {return ticketType;}


}

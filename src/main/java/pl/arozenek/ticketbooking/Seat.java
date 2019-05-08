package pl.arozenek.ticketbooking;

public class Seat {
    private int row;
    private int seat;

    public Seat(int row, int seat) {
        this.row = row;
        this.seat = seat;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    @Override
    public String toString() {
        return ("Row: " + row + ", Seat: " + seat);
    }
}

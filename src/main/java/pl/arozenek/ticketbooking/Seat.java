package pl.arozenek.ticketbooking;

import java.util.Comparator;

public class Seat {
    public int row;
    public int seat;

    protected static Comparator<Seat> compare =
            new Comparator<Seat>() {
                @Override
                public int compare(Seat seat, Seat t1) {
                    if (seat.row < t1.row)
                        return -1;
                    else if (seat.row > t1.row)
                        return 1;
                    else if (seat.seat < t1.seat)
                        return -1;
                    else if (seat.seat > t1.seat)
                        return 1;
                    return 0;
                }
            };


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

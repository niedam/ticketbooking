package pl.arozenek.ticketbooking;

/* Class for /reservation endpoint */

public class Reservation {
    private String clientName;
    private String clientSurname;
    private int idScreening;
    private long timeReservation;
    private double price;
    private int row;
    private int seat;

    public Reservation(String clientName, String clientSurname, int idScreening,
                       long timeReservation, double price, int row, int seat) {
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.idScreening = idScreening;
        this.timeReservation = timeReservation;
        this.price = price;
        this.row = row;
        this.seat = seat;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public int getIdScreening() {
        return idScreening;
    }

    public long getTimeReservation() {
        return timeReservation;
    }

    public double getPrice() {
        return price;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }
}

package pl.arozenek.ticketbooking;

public class Screening {
    private int idScreening;
    private int idRoom;
    private int idMovie;
    private long time;
    private String title;

    public Screening(int idScreening, int idRoom, long time, int idMovie, String title) {
        this.idScreening = idScreening;
        this.idRoom = idRoom;
        this.time = time;
        this.idMovie = idMovie;
        this.title = title;
    }

    public int getIdScreening() {
        return this.idScreening;
    }

    public int getIdRoom() {
        return this.idRoom;
    }

    public int getIdMovie() {
        return this.idMovie;
    }

    public long getTime() {
        return this.time;
    }

    public String getTitle() {
        return  this.title;
    }

    @Override
    public String toString() {
        return ("idScreening: " + idScreening + ", title: " + title);
    }
}

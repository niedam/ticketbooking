package pl.arozenek.ticketbooking;

import java.util.Date;

public class Screening {
    private int idScreening;
    private int room;
    private int idMovie;
    private long time;
    private String title;

    public Screening(int idScreening, int idRoom, long time, int idMovie, String title) {
        this.idScreening = idScreening;
        this.room = idRoom;
        this.time = time;
        this.idMovie = idMovie;
        this.title = title;
    }

    public int getIdScreening() {
        return this.idScreening;
    }

    public int getRoom() {
        return this.room;
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
        Date date_time = new Date(time);
        return (title + ", time: " + date_time + ", room: " + room + ", id of screening: " + idScreening);
    }
}

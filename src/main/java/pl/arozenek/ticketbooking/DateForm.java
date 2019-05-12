package pl.arozenek.ticketbooking;

import java.util.Date;


/* Class for /date endpoint */

public class DateForm {
    public int day;
    public int month;
    public int year;
    public int hours;
    public int minutes;

    long toLong() {

        //Method deprecated
        Date result = new Date(year - 1900, month, day, hours, minutes);
        return result.getTime();
    }

}

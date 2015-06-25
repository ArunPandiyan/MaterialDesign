package technibits.com.pme.data;

/**
 * Created by technibitsuser on 6/16/2015.
 */
public class ScheduleInfo {
    private int id;
    private String name;
    private String date;
    private String year;
    private String month;
    private String time;
    private boolean notification_state;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotification_state() {
        return notification_state;
    }

    public void setNotification_state(boolean notification_state) {
        this.notification_state = notification_state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

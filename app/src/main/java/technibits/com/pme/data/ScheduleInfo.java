package technibits.com.pme.data;

/**
 * Created by technibitsuser on 6/16/2015.
 */
public class ScheduleInfo {
    private String name;
    private String date;
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
}

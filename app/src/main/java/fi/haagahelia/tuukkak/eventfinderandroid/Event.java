package fi.haagahelia.tuukkak.eventfinderandroid;

public class Event {

    private int id;
    private String title;
    private String address;
    private String date;

    public Event() {

    }

    public Event(String title, String address, String date) {
        this.title = title;
        this.address = address;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return  title + "\n" + date + "\n" + address;
    }
}

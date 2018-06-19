package fi.haagahelia.tuukkak.eventfinderandroid;

public class Event {

    private int id;
    private String title;
    private String venue;
    private String address;
    private String date;
    private String city;

    public Event() {

    }

    public Event(int id, String title, String venue, String address, String date, String city) {
        this.id = id;
        this.title = title;
        this.venue = venue;
        this.address = address;
        this.date = date;
        this.city = city;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return  title.toUpperCase() + " @ " + venue + "\nDate: " + date + "\nVenue address: " + address + ", " + city;
    }
}

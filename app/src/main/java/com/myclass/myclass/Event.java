package com.myclass.myclass;

/**
 * Created by anand on 18/01/18.
 */

public class Event {
    String title,dateandtime,description;

    public Event() {
    }

    public Event(String title, String dateandtime, String description) {
        this.title = title;
        this.dateandtime = dateandtime;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public String getDescription() {
        return description;
    }
}

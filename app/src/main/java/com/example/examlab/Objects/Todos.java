package com.example.examlab.Objects;

public class Todos {
    private String description;
    private int timeHour;
    private int timeMinute;
private int id;


    public Todos(String description, int timeSpanh, int timeSpanm,int id) {
        this.description = description;
        this.timeHour = timeSpanh;
        this.timeMinute = timeSpanm;
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public int getTimeMinute() {
        return timeMinute;
    }

    public String getDescription() {
        return description;
    }

    public int getTimeHour() {
        return timeHour;
    }

}

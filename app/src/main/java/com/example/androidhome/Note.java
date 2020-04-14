package com.example.androidhome;

import java.sql.Time;
import java.sql.Timestamp;

public class Note {
    private String name;
    private String image;
    private String date;
    private String time;
    private String change;

    public Note(){

    }
    public Note (String image, String name, String date, String time, String change){

    this.image = image;
    this.name = name;
    this.date = date;
    this.time = time;
    this.change = change;
}

    public String getImage()
    {
        return image;

    }
    public String getName()
    {
        return name;
    }
    public String getDate()
    {
        return date;    }
    public String getTime()
    {
        return time;

    }
    public String getChange()
    {
        return change;
    }

}
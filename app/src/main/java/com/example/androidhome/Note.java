package com.example.androidhome;


public class Note {
    private String name;
    private String image;
    private String date;
    private String time;
    private String change;
    private String lux;
    private String setting;

    public Note(){

    }
    public Note (String image, String name, String date, String time, String change, String lux, String setting){
        //This document will pull the strings from cloud firestore
        //all strings have to be the same format and case sensitive
    this.image = image;
    this.name = name;
    this.date = date;
    this.time = time;
    this.change = change;
    this.lux = lux;
    this.setting = setting;
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
    public String getLux()
    {

        return lux;
    }
    public String getSetting()
    {

        return setting;
    }

}
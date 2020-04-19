package com.example.olio_9_sr;

public class Movie {


    int id;
    String title;
    String showStart;
    String showEnd;
    String location;
    String[] showstart;
    String[] showend;

    Movie(int id, String title, String showStart, String showEnd, String location) {

        showstart = editTime(showStart);
        showend = editTime(showEnd);

        this.id = id;
        this.title = title;
        this.showStart = showstart[0] + " " + showstart[1];
        this.showEnd = showend[0] + " " + showend[1];
        this.location = location;
    }

    String[] editTime(String date){

        String [] _date = date.split("T", 2);
        return _date;

    }
}
package com.example.olio_9_sr;


import java.util.ArrayList;

public class Movies {

    ArrayList<Movie> movies;

    Movies(){
        movies = new ArrayList<>();
    }

    void addMovie(Movie movie){
        movies.add(movie);
    }

}

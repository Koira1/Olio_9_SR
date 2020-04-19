package com.example.olio_9_sr;

import android.util.Log;

import java.util.ArrayList;

public class Theaters {

    ArrayList<Theater> Theaters;

    Theaters(){
        Theaters = new ArrayList<Theater>();
    }

    public ArrayList<String> getLocations(ArrayList<Theater> theaters, ArrayList<String> Locations){

        for(int i = 0; i < theaters.size(); i++){
            Locations.add(theaters.get(i).Name);
            Log.d("Locations: ", theaters.get(i).Name);
        }
        return Locations;
    }

}
package com.example.flixsterapp.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    //values from API

    private String title;
    private String overview;
    private String posterPath; //not the full URL, just the path
    private String backdropPath;
    public final static String TAG = "Movie";

    //initialize from JSON data
    public Movie(JSONObject object){
        try{
            title = object.getString("title");
            overview = object.getString("overview");
            posterPath = object.getString("poster_path");
            backdropPath = object.getString("backdrop_path");
        }
        catch(JSONException e){
            Log.e(TAG,"Failed parsing for title, overview, poster path or backdrop path", e);
        }
    }

    //getters -- go to generate -> getters

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

}

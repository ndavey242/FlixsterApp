package com.example.flixsterapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //the base url for loading images
    String imageBaseURL;
    //the poster size to use when fetching images, part of the url
    String posterSize;
    //the backdrop size
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get the image base url. string initalized above
        imageBaseURL = images.getString("secure_base_url");
        //get the poster size
        //use response.getJSONArray() and pass in the key whose value you are looking for
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the option at index 3 -- w342. use w342 itself as a fallback.
        //string initalized above
        posterSize = posterSizeOptions.optString(3, "w342");
        //parse the backdrop sizes for the option at index 1 and use w780 as a fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    public String getImageBaseURL() {
        return imageBaseURL;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getImageURL(String size, String path){
        return String.format("%s%s%s", imageBaseURL, size, path); //concatenating the 3 componenents
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}

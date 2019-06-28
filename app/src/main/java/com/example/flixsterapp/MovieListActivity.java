package com.example.flixsterapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flixsterapp.models.Config;
import com.example.flixsterapp.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//start with the API key stuff to connect to the API
//then get the configuration
//set up error handling
//create the new movie class
//set up the new movies endpoint
//set up the adapter and wire it into this main activity code


public class MovieListActivity extends AppCompatActivity {

    //constants
    //base URL
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    //instances
    AsyncHttpClient client;


    //the list of currently playing movies
    ArrayList<Movie> movies;

    //the recycler view
    RecyclerView rvMovies;
    //the adapter wired to the recyclerview
    MovieAdapter adapter;

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //intialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies = new ArrayList<>();
        //initialize the adapter -- movies array cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        //resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //get the configuration upon app creation -- call the method
        getConfiguration();

    }

    // get the list of playing movies from the API
    private void getNowPlaying(){
        //create the URL = the base url + the configuration endpoint
        String url = API_BASE_URL + "/movie/now_playing?";
        //set the request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //execute a GET request expecting a JSON object in response
        //set up client.get, then right click generate overrides
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movies list
                //ALWAYS CHECK WHETHER YOU ARE WORKING WITH A JSON ARRAY OR JSON OBJECT!!!!!!!!
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through array and pass each object through the movie constructor & add new obj to the list
                    for (int i = 0; i < results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify adapter of changes -- that a row was added
                        //just added the row to movies, so it is the last one in movies
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies",results.length()));

                } catch (JSONException e) {
                    logError("Failed parsing data from now playing", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing", throwable, true);
            }
        });
    }

    //get the configuration from the API -- this is for images??
    private void getConfiguration(){
        //create the URL = the base url + the configuration endpoint
        String url = API_BASE_URL + "/configuration?";
        //set the request params
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //execute a GET request expecting a JSON object in response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseURL %s and posterSize %s",
                            config.getImageBaseURL(), config.getPosterSize()));
                    //pass config to adapter
                    adapter.setConfig(config);

                    //get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                logError("Failed getting configuration", throwable, true);
            }

        });

    }

    //handle errors (AVOID SILENT ERRORS) -- log and alert users
    private void logError(String message,  Throwable error, boolean alertUser){
        //always log the error
        Log.e(TAG, message, error);
        //alert the user
        if (alertUser){
            //show a long toast with the error. (toasts are low impact)
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        }
    }

}

package com.example.flixsterapp;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixsterapp.models.Config;
import com.example.flixsterapp.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    //list of movies
    ArrayList<Movie> movies;

    //config needed for image urls
    Config config;

    //context for rendering
    Context context;

    //initialize with list

    public MovieAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }


   // GO OVER THESE CONCEPTS AGAIN!!!!!!! check function below

    //creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //need to get the context and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view object using the item_movie layout
        //create the view from the inflater
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);
        //return a new ViewHolder --> that view wrapped by a viewholder
        return new ViewHolder(movieView);
    }

    //binds an inflated view to a new item (specific data element @ specific location)
    //associates a viewHolder with a specific element in the data set
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //get the movie data at that position
        Movie movie = movies.get(position);
        //populate the view with the data from the movie
        viewHolder.btnTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        //determine the layout orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        String imageURL;

        //if in portrait orientation
        if (isPortrait){
            //build the url for the poster image
            imageURL = config.getImageURL(config.getPosterSize(), movie.getPosterPath());

        } else{
            //build the url for the backdrop image
            imageURL = config.getImageURL(config.getBackdropSize(), movie.getBackdropPath());
        }

        //choose which placeholder to use and what image to load into the imageview
        int placeholderID = isPortrait ? R.drawable.flicks_movie_placeholder: R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage: viewHolder.ivBackdropImage;

        //load the image using Glide
        Glide.with(context)
                .load(imageURL)
                .apply(new RequestOptions()
                        .bitmapTransform(new RoundedCornersTransformation(30,0)))
                .apply(new RequestOptions()
                        .placeholder(placeholderID)
                        .error(R.drawable.flicks_movie_placeholder))
                .into(imageView);

    }

    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //create the viewholder as a static inner class

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView btnTitle;
        TextView tvOverview;

        public ViewHolder(View itemView){
            super(itemView);
            //lookup items by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            btnTitle = (Button) itemView.findViewById(R.id.btnTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
        }

    }




}

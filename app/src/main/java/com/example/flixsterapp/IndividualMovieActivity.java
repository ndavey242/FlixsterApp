package com.example.flixsterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class IndividualMovieActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_individual);

        String title = getIntent().getStringExtra("MOVIE_TITLE");
        String overview = getIntent().getStringExtra("MOVIE_OVERVIEW");

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        TextView tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvOverview.setText(overview);
}

    public void onClickBack(View itemView){
        Intent i = new Intent(IndividualMovieActivity.this, MovieListActivity.class);
        startActivity(i);
    }

}

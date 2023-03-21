package com.github.cptzee.cinemaapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.github.cptzee.cinemaapp.Data.Category;
import com.github.cptzee.cinemaapp.Data.Cinema;
import com.github.cptzee.cinemaapp.Data.Database.AccountHelper;
import com.github.cptzee.cinemaapp.Data.Database.CategoryHelper;
import com.github.cptzee.cinemaapp.Data.Database.CinemaHelper;
import com.github.cptzee.cinemaapp.Data.Database.CredentialHelper;
import com.github.cptzee.cinemaapp.Data.Database.MovieHelper;
import com.github.cptzee.cinemaapp.Data.Database.RatingHelper;
import com.github.cptzee.cinemaapp.Data.Database.ScheduleHelper;
import com.github.cptzee.cinemaapp.Data.Database.SeatHelper;
import com.github.cptzee.cinemaapp.Data.Movie;
import com.github.cptzee.cinemaapp.Data.Rating;
import com.github.cptzee.cinemaapp.Data.Schedule;
import com.github.cptzee.cinemaapp.Data.Seat;

import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("First-Launch", MODE_PRIVATE);
        boolean firstLaunch = preferences.getBoolean("firstLaunch", true);

        if(firstLaunch)
            runFirstRunCode(preferences);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();

        }, 3000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void runFirstRunCode(SharedPreferences preferences){
        SharedPreferences.Editor editor =  preferences.edit();
        AccountHelper accountHelper = new AccountHelper(this);
        CategoryHelper categoryHelper = new CategoryHelper(this);
        CinemaHelper cinemaHelper = new CinemaHelper(this);
        CredentialHelper credentialHelper = new CredentialHelper(this);
        MovieHelper movieHelper = new MovieHelper(this);
        RatingHelper ratingHelper = new RatingHelper(this);
        ScheduleHelper scheduleHelper = new ScheduleHelper(this);
        SeatHelper seatHelper = new SeatHelper(this);

        accountHelper.onCreate(accountHelper.getWritableDatabase());
        categoryHelper.onCreate(categoryHelper.getWritableDatabase());
        cinemaHelper.onCreate(cinemaHelper.getWritableDatabase());
        credentialHelper.onCreate(credentialHelper.getWritableDatabase());
        movieHelper.onCreate(movieHelper.getWritableDatabase());
        ratingHelper.onCreate(ratingHelper.getWritableDatabase());
        scheduleHelper.onCreate(scheduleHelper.getWritableDatabase());
        seatHelper.onCreate(seatHelper.getWritableDatabase());

        String[] categories = {"IMAX", "Ordinary", "3D", "Directors"};
        for(String data : categories){
            Category value = new Category();
            value.setName(data);
            categoryHelper.insert(value);
        }

        String[] ratings = {"SPG", "G"};
        for(String data : ratings){
            Rating value = new Rating();
            value.setName(data);
            ratingHelper.insert(value);
        }

        String[] cinemas = {"Cinema 1", "Cinema 2"};
        for(String data : cinemas){
            Cinema value = new Cinema();
            value.setName(data);
            cinemaHelper.insert(value);
        }

        LocalDateTime[] schedules = {LocalDateTime.now()};
        for(LocalDateTime time : schedules){
            Schedule schedule = new Schedule();
            for(int i = 1; i <= 4; i++){
                schedule.setCinemaID(i);
                schedule.setTime(time);
                scheduleHelper.insert(schedule);
            }
        }

        for(int i = 1; i <= 20; i++){
            Seat value = new Seat();
            value.setScheduleID(1);
            value.setSeat(String.valueOf(i));
        }

        String[] movieTitles = {"Eternals at ang alamat ni Kardo", "Kalampag sa papag"};
        String[] movieDescriptions = {"Default", "Default"};
        int[] movieRatingIDs = { 2, 1};
        int[] movieCategoryIDs = {1, 1};
        int[] movieCinemaIDs = {1, 2};
        for(int i = 0; i <= 1; i++){
            Movie movie = new Movie();
            movie.setTitle(movieTitles[i]);
            movie.setDescription(movieDescriptions[i]);
            movie.setRating(movieRatingIDs[i]);
            movie.setCategoryID(movieCategoryIDs[i]);
            movie.setCinemaID(movieCinemaIDs[i]);
            movieHelper.insert(movie);
        }

        editor.putBoolean("firstLaunch", false);
        editor.commit();
    }
}
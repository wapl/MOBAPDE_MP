package com.example.mobapde_mp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.androdocs.httprequest.HttpRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetails extends AppCompatActivity {
    TextView txtTitle;
    TextView txtPlot;
    String Title;
    String api="48adfd0c";
    String showtimeAPi;
    int  MovieId;
    int CinemaID;
    String ImageUrl;
    String CinemaName;
    RatingBar rating;
    ImageView poster;
    Spinner spinner;
    ArrayList<String> showtimes=new ArrayList<String>();
    private static final String TAG = "MovieDetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        txtTitle=findViewById(R.id.movieTitle);
        txtPlot=findViewById(R.id.txtPlot);
        rating=findViewById(R.id.ratingBar);
        poster=findViewById(R.id.moviePoster);
        spinner=findViewById(R.id.spinner);

        showtimeAPi=getString(R.string.APIKEY);

        Intent intent=getIntent();
        Title=intent.getStringExtra("MOVIETITLE");
        ImageUrl=intent.getStringExtra("IMAGEURL");
        MovieId=intent.getIntExtra("MOVIEID",-1);
        CinemaID=intent.getIntExtra("CINEMAID",-1);

        Picasso.get().load(ImageUrl).into(poster);
        txtTitle.setText(Title);
        Title=Title.replaceAll("\\s","+");
        Log.d(TAG, Title );

        new movieTask().execute();
        new ShowTimes().execute();

    }

    class movieTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response= HttpRequest.excuteGet("http://www.omdbapi.com/?t=" + Title + "&apikey=" + api);
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject jsonObj = new JSONObject(result);
                String plot= jsonObj.getString("Plot");
                JSONArray jsonRatings=jsonObj.getJSONArray("Ratings");
                for(int i=0;i<1;i++)
                {
                    JSONObject jsonRating=jsonRatings.getJSONObject(i);
                    String Rating=jsonRating.getString("Value");
                    String subRating=Rating.substring(0,3);
                    Log.d(TAG, "RATINGS : "+subRating );


                    float fltRating=Float.parseFloat(subRating);
                    Log.d(TAG, "RATINGS float : "+fltRating );
                    rating.setRating(fltRating);
                }

                txtPlot.setText(plot);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    class ShowTimes extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String response= HttpRequest.excuteGet("https://api.internationalshowtimes.com/v4/showtimes?movie_id="+MovieId+"&cinema_id="+CinemaID+"&apikey="+showtimeAPi);
            return  response;
        }

        @Override
        protected void onPostExecute(String result){
            try {
                Log.d(TAG, "HELLO : " );
                JSONObject jsonObj = new JSONObject(result);
                Log.d(TAG, "JSON : "+jsonObj );
                JSONArray jsonShowtimes=jsonObj.getJSONArray("showtimes");
                for(int i=0;i<jsonShowtimes.length();i++)
                {
                    JSONObject jsonTime=jsonShowtimes.getJSONObject(i);
                    String showtime=jsonTime.getString("start_at");
                    showtimes.add(showtime);
                }
                ArrayAdapter<String> showtimeList=new ArrayAdapter<String>(MovieDetails.this, android.R.layout.simple_spinner_item, showtimes);
                showtimeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(showtimeList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



}

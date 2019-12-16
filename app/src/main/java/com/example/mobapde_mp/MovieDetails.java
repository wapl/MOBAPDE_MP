package com.example.mobapde_mp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MovieDetails extends AppCompatActivity {
    TextView txtTitle;
    TextView txtPlot;
    TextView txtSeats;
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
    Button btnReserve;
    ArrayList<String> showtimes=new ArrayList<String>();
    private static final String TAG = "MovieDetails";
    DatabaseReference databaseReference;
    FirebaseUser user;
    List<Reservation> list;
    int seats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        txtTitle=findViewById(R.id.movieTitle);
        txtPlot=findViewById(R.id.txtPlot);
        rating=findViewById(R.id.ratingBar);
        poster=findViewById(R.id.moviePoster);
        spinner=findViewById(R.id.spinner);
        btnReserve=findViewById(R.id.btnReserve);
        list=new ArrayList<Reservation>();
        txtSeats=findViewById(R.id.txtSeats);



        databaseReference= FirebaseDatabase.getInstance().getReference("RESERVATIONS");
        user= FirebaseAuth.getInstance().getCurrentUser();



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

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null)
                {
                    String showtime=spinner.getSelectedItem().toString();
                    String email=user.getEmail();
                    boolean exists=false;
                    for(int i=0;i<list.size()&&exists==false;i++)
                    {
                        Log.d(TAG, "DATA LOOP : "+list.get(i) );
                        if(list.get(i).getCInemaID().equals(String.valueOf(CinemaID)) && list.get(i).getMovieID().equals(String.valueOf(MovieId)) && list.get(i).getEmial().equals(email) && list.get(i).getDate().equals(showtime) )
                        {
                            Log.d(TAG, "DATA EXISTS : "+list.get(i) );
                            exists=true;
                        }
                    }
                    if(exists==false)
                    {
                        if(seats!=0)
                        {
                            String Id=databaseReference.push().getKey();
                            Reservation reservation=new Reservation(Id,String.valueOf(CinemaID),String.valueOf(MovieId),email,showtime);
                            databaseReference.child(Id).setValue(reservation);
                            Toast.makeText(MovieDetails.this, "Reservation added", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MovieDetails.this, "No available seats left", Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(MovieDetails.this, "You already have a Reservation", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        new movieTask().execute();
        new ShowTimes().execute();

    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Reservation reservation=postSnapshot.getValue(Reservation.class);

                    list.add(reservation);
                    Log.d(TAG, "DATA ADDED : "+ reservation );
                }
                int count=0;
                for (int i=0;i<list.size();i++)
                {
                    if(list.get(i).getMovieID().equals(String.valueOf(MovieId))&&list.get(i).getCInemaID().equals(String.valueOf(CinemaID)))
                    {
                        count++;
                    }
                }
                if(count==0)
                {

                    seats=50;
                    Log.d(TAG, "COUNT IS ZERO : ");
                }
                else{


                        seats=50-count;
                        Log.d(TAG, "SEAT NUM: "+seats);
                    
                }
                Log.d(TAG, "Seats : "+ seats );
                txtSeats.setText(String.valueOf(seats));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

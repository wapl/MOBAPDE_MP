package com.example.mobapde_mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class movielist extends AppCompatActivity implements OnMovieListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    MovieAdapter adapter;
    String APIKEY;
    int CinemaID;
    String CInemaName;
    private static final String TAG = "movieslist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        recyclerView=findViewById(R.id.recyclerArea);

        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);

        APIKEY = getString(R.string.APIKEY);
        Intent intent = getIntent();
        CinemaID = intent.getIntExtra("CINEMAID",-1);
        CInemaName = intent.getStringExtra("NAME");
        new movietask().execute();

    }

    @Override
    public void OnMovieClick(int position) {
        MovieModel movieModel=adapter.getMovie(position);
        Intent intent=new Intent(this ,MovieDetails.class);
        intent.putExtra("MOVIEID",movieModel.getMovieID());
        intent.putExtra("MOVIETITLE",movieModel.getTitle());
        intent.putExtra("IMAGEURL",movieModel.getImageURL());
        intent.putExtra("CINEMANAME",CInemaName);
        intent.putExtra("CINEMAID",CinemaID);
        startActivity(intent);
    }

    class movietask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = HttpRequest.excuteGet("https://api.internationalshowtimes.com/v4/movies/?cinema_id=" + CinemaID + "&apikey=" + APIKEY);
            return response;
        }
        protected void onPostExecute(String result){
            Log.d(TAG, "DATA SCRAPE " );

            try {

                JSONObject jsonOBJ=new JSONObject(result);
                Log.d(TAG, String.valueOf(jsonOBJ));
                JSONArray jsonMovies=jsonOBJ.getJSONArray("movies");
                for(int i=0;i<jsonMovies.length();i++)
                {
                    JSONObject jsonMovie=jsonMovies.getJSONObject(i);
                    String id=jsonMovie.getString("id");
                    String Title=jsonMovie.getString("title");
                    String url=jsonMovie.getString("poster_image_thumbnail");
                    MovieModel movieModel=new MovieModel(Integer.parseInt(id),Title,url);

                    adapter.additem(movieModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}


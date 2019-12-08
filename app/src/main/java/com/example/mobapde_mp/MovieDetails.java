package com.example.mobapde_mp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity {
    TextView txtTitle;
    TextView txtPlot;
    String Title="Mr+Bean";
    String api="48adfd0c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        txtTitle=findViewById(R.id.txtTitle);
        txtPlot=findViewById(R.id.txtPlot);


        new movieTask().execute();
    }

    class movieTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response= HttpRequest.excuteGet("http://www.omdbapi.com/?t=" + Title + "&plot=full&apikey=" + api);
            return response;
        }
        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject jsonObj = new JSONObject(result);
                String plot= jsonObj.getString("Plot");
                String title=jsonObj.getString("Title");
                txtTitle.setText(title);
                txtPlot.setText(plot);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}

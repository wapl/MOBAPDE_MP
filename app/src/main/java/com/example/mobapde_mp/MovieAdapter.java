package com.example.mobapde_mp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter  extends RecyclerView.Adapter<MovieHolder> {

    private ArrayList<MovieModel> movies;
    private static final String TAG = "MovieAdapter";
    private OnMovieListener listener;

    public MovieAdapter(OnMovieListener listener)
    {

        movies=new ArrayList<MovieModel>();
        this.listener=listener;
    }
    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.movie_row,parent,false);
        MovieHolder movieHolder=new MovieHolder(view,listener);

        return movieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.setTxtTitle(movies.get(position).getTitle());
        holder.setPoster(movies.get(position).getImageURL());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    public void additem(MovieModel movieModel)
    {
        boolean hasFound=false;
        for(int i=0;i<movies.size() && hasFound==false;i++)
        {
            if(movies.get(i).getMovieID()==movieModel.getMovieID())
                hasFound=true;

        }
        if(hasFound==false)
            movies.add(movieModel);
        Log.d(TAG, String.valueOf(movies));
        notifyItemInserted(movies.size()-1);
    }

    public ArrayList<MovieModel> getMovies()
    {
        return movies;
    }

    public MovieModel getMovie( int position)
    {
        return movies.get(position);
    }



}

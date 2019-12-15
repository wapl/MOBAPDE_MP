package com.example.mobapde_mp;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieHolder extends RecyclerView.ViewHolder implements  View.OnClickListener  {
    private TextView txtTitle;
    private ImageView poster;
    private OnMovieListener listener;
    public MovieHolder(@NonNull View itemView,OnMovieListener listener) {
        super(itemView);
        this.txtTitle=itemView.findViewById(R.id.movieTitle);
        this.poster=itemView.findViewById(R.id.poster);
       // poster.setImageDrawable(null);
        this.listener=listener;
        itemView.setOnClickListener(this);


    }



    public void setTxtTitle(String Title) {
        this.txtTitle.setText(Title);
    }



    public void setPoster(String imageUrl) {

        Picasso.get().load(imageUrl).into(poster);
    }


    @Override
    public void onClick(View v) {
        listener.OnMovieClick(getAdapterPosition());
    }
}

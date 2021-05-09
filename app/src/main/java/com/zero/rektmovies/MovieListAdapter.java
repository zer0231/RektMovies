package com.zero.rektmovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListHolder> {
    private ArrayList<MovieListDetail> moviesList;
    private Context context;

    public MovieListAdapter(Context context, ArrayList<MovieListDetail> MovieList)
    {
        this.moviesList = MovieList;
        this.context = context;
    }
    @NonNull
    @Override
    public MovieListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_card,parent,false);
        return new MovieListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListHolder holder, int position) {
        MovieListDetail currentItem = moviesList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String title = currentItem.getTitle();
        String rating = currentItem.getRating();
        String year = currentItem.getYear();
        String id = currentItem.getId();

        //setting to the UI
        Glide.with(context).load(imageUrl)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.cover)//to show loading
                .into(holder.movie_poster);//to set after loaded
        holder.movie_title.setText(title);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Title: "+title+"\nRating: "+rating+"\nRelease Year: "+year, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MovieDetail.class);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieListHolder extends RecyclerView.ViewHolder {
        public TextView movie_title;
        public ImageView movie_poster;
        public MovieListHolder(@NonNull View itemView) {
            super(itemView);
            movie_title = itemView.findViewById(R.id.movie_title);
            movie_poster = itemView.findViewById(R.id.movie_poster);
        }
    }
}

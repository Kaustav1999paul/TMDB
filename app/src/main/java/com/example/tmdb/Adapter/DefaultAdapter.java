package com.example.tmdb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdb.Model.DefaultMovies;
import com.example.tmdb.Model.Movie;
import com.example.tmdb.R;

import java.util.ArrayList;

public class DefaultAdapter extends RecyclerView.Adapter<DefaultAdapter.DefaultViewHolder> {

    private Context context;
    private ArrayList<DefaultMovies> list;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public DefaultAdapter(Context context, ArrayList<DefaultMovies> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DefaultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.default_layout, parent, false);
        return new DefaultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultViewHolder holder, int position) {
        DefaultMovies defaultMovies = list.get(position);

        String title = defaultMovies.getTitle();
        String imageURL = defaultMovies.getPoster_path();

        holder.textTitle.setText(title);
        Glide.with(context).load(imageURL).into(holder.movie_poster);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public ImageView movie_poster;

        public DefaultViewHolder(View itemView) {
            super(itemView);

            movie_poster = itemView.findViewById(R.id.movie_poster);
            textTitle = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

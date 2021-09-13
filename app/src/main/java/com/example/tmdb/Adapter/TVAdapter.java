package com.example.tmdb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdb.Model.TV;
import com.example.tmdb.R;

import java.util.ArrayList;

public class TVAdapter extends RecyclerView.Adapter<TVAdapter.TVViewHolder> {

    private Context context;
    private ArrayList<TV> tvList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickTV(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public TVAdapter(Context context, ArrayList<TV> tvList) {
        this.context = context;
        this.tvList = tvList;
    }

    @NonNull
    @Override
    public TVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.default_layout, parent, false);
        return new TVViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TVViewHolder holder, int position) {
        TV tv  = tvList.get(position);
        String imageURL = tv.getPoster_path();
        Glide.with(context).load(imageURL).into(holder.movie_poster);
    }

    @Override
    public int getItemCount() {
        return tvList.size();
    }

    public class TVViewHolder extends RecyclerView.ViewHolder{

        public ImageView movie_poster;

        public TVViewHolder(@NonNull View itemView) {
            super(itemView);
            movie_poster = itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClickTV(position);
                        }
                    }
                }
            });
        }
    }
}

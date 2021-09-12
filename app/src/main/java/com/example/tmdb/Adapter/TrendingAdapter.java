package com.example.tmdb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdb.Model.DefaultMovies;
import com.example.tmdb.R;

import java.util.ArrayList;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder> {

    private Context context;
    private ArrayList<DefaultMovies> listT;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClickUP(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public TrendingAdapter(Context context, ArrayList<DefaultMovies> listT) {
        this.context = context;
        this.listT = listT;
    }

    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trending_layout, parent, false);
        return new TrendingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        DefaultMovies defaultMovies = listT.get(position);
        String imageURL = defaultMovies.getPoster_path();

        Glide.with(context).load(imageURL).into(holder.movie_posterT);

    }

    @Override
    public int getItemCount() {
        return listT.size();
    }

    public class TrendingViewHolder extends RecyclerView.ViewHolder{
        public ImageView movie_posterT;

        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            movie_posterT = itemView.findViewById(R.id.movie_posterT);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClickUP(position);
                        }
                    }
                }
            });
        }
    }
}

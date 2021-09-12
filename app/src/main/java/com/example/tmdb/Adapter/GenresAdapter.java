package com.example.tmdb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmdb.Model.Genres;
import com.example.tmdb.R;

import java.util.ArrayList;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenresViewHolder>{

    private Context context;
    private ArrayList<Genres> gList;

    public GenresAdapter(Context context, ArrayList<Genres> gList) {
        this.context = context;
        this.gList = gList;
    }

    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_layout, parent, false);
        return new GenresViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresViewHolder holder, int position) {
        Genres genres = gList.get(position);

        String category = genres.getName();
        holder.category.setText(category);
    }

    @Override
    public int getItemCount() {
        return gList.size();
    }

    public class GenresViewHolder extends RecyclerView.ViewHolder{

        TextView category;

        public GenresViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
        }
    }

}

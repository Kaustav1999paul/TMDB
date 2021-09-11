package com.example.tmdb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdb.Model.Cast;
import com.example.tmdb.R;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private Context context;
    private ArrayList<Cast> cList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public CastAdapter(Context context, ArrayList<Cast> cList){
        this.context = context;
        this.cList = cList;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cast_layout, parent, false);
        return new CastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {

        Cast cast = cList.get(position);

        String photo = cast.getProfile_path();
        Glide.with(context).load(photo).into(holder.castPhoto);

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }


    public class CastViewHolder extends RecyclerView.ViewHolder{

        ImageView castPhoto;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);

            castPhoto = itemView.findViewById(R.id.castPhoto);
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

package com.example.tmdb;

import static com.example.tmdb.HomeActivity.BACK_POSTER;
import static com.example.tmdb.HomeActivity.ID;
import static com.example.tmdb.HomeActivity.LANG;
import static com.example.tmdb.HomeActivity.OVERVIEW;
import static com.example.tmdb.HomeActivity.POSTER;
import static com.example.tmdb.HomeActivity.RELEASE_DATE;
import static com.example.tmdb.HomeActivity.TITLE;
import static com.example.tmdb.HomeActivity.VOTE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tmdb.Adapter.CastAdapter;
import com.example.tmdb.Adapter.GenresAdapter;
import com.example.tmdb.Model.Cast;
import com.example.tmdb.Model.Genres;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class DetailsActivity extends AppCompatActivity implements CastAdapter.OnItemClickListener {

    ImageView backgroundPoster, movie_posterV;
    BlurView blurView;
    Intent intent;
    String poster_path, original_language ,title, backdrop_path, overview, release_date, votes, id;
    TextView titleV, overviewV, releaseDateV, langV, ratingsV, statusV;
    RecyclerView castRV, categoryRV;
    private RequestQueue mRequestQueue;
    private ArrayList<Cast> castArrayList;
    private ArrayList<Genres> genresArrayList;
    private CastAdapter castAdapter;
    private GenresAdapter genresAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        blurView = findViewById(R.id.blurView);
        theme();
        castRV = findViewById(R.id.castRV);
        castRV.setHasFixedSize(true);
        castRV.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        categoryRV = findViewById(R.id.categoryRV);
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        backgroundPoster = findViewById(R.id.backgroundPoster);
        movie_posterV = findViewById(R.id.movie_posterV);
        intent = getIntent();
        langV = findViewById(R.id.langV);
        titleV = findViewById(R.id.titleV);
        overviewV = findViewById(R.id.overviewV);
        releaseDateV = findViewById(R.id.releaseDateV);
        ratingsV = findViewById(R.id.ratingsV);
        statusV = findViewById(R.id.status);

        castArrayList = new ArrayList<>();
        genresArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);


        title = intent.getStringExtra(TITLE);
        backdrop_path = "https://image.tmdb.org/t/p/original"+intent.getStringExtra(BACK_POSTER);
        poster_path = "https://image.tmdb.org/t/p/original"+intent.getStringExtra(POSTER);
        original_language = intent.getStringExtra(LANG);
        overview = intent.getStringExtra(OVERVIEW);
        release_date = intent.getStringExtra(RELEASE_DATE);
        votes = intent.getStringExtra(VOTE);
        id = intent.getStringExtra(ID);



        titleV.setText(title);
        ratingsV.setText(votes);
        overviewV.setText(overview);
        langV.setText(original_language);
        releaseDateV.setText("In theaters from "+release_date);
        Glide.with(this).load(backdrop_path).into(backgroundPoster);
        Glide.with(this).load(poster_path).into(movie_posterV);

        showCast(id);
        getMovieCategory(id);
    }

    private void getMovieCategory(String id) {
        String URL = "https://api.themoviedb.org/3/movie/"+id+"?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status= response.getString("status");

                    statusV.setText(status);

                    JSONArray jsonArray = response.getJSONArray("genres");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject res = jsonArray.getJSONObject(i);

                        String name = res.getString("name");
                        genresArrayList.add(new Genres(name));
                    }
                    genresAdapter = new GenresAdapter(DetailsActivity.this, genresArrayList);
                    categoryRV.setAdapter(genresAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(request);
    }

    private void showCast(String id_Film) {
        String URL = "https://api.themoviedb.org/3/movie/"+id_Film+"/credits?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("cast");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject res = jsonArray.getJSONObject(i);
                        String photo = "https://image.tmdb.org/t/p/original"+res.getString("profile_path");
                        String name = res.getString("name");
                        String character = res.getString("character");
                        int pop = res.getInt("popularity");


                        castArrayList.add(new Cast(name, character, photo, pop));
                    }

                    castAdapter = new CastAdapter(DetailsActivity.this, castArrayList);
                    castRV.setAdapter(castAdapter);
                    castAdapter.setOnItemClickListener(DetailsActivity.this);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mRequestQueue.add(request);
    }

    private void theme() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        float radius = 5f;

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }

    @Override
    public void onItemClick(int position) {
        Cast clickedItem = castArrayList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.cast_dialog, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        ImageView photo = dialogView.findViewById(R.id.cast_photo);
        Glide.with(this).load(clickedItem.getProfile_path()).into(photo);

        TextView castName = dialogView.findViewById(R.id.castName);
        castName.setText(clickedItem.getName());

        TextView charName = dialogView.findViewById(R.id.charName);
        charName.setText("as "+clickedItem.getCharacter());

        TextView popularity = dialogView.findViewById(R.id.popularity);
        popularity.setText(String.valueOf(clickedItem.getPopularity()));

        alertDialog.show();

    }
}
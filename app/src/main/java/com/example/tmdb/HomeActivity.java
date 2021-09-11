package com.example.tmdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tmdb.Adapter.DefaultAdapter;
import com.example.tmdb.Adapter.MovieAdapter;
import com.example.tmdb.Model.DefaultMovies;
import com.example.tmdb.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements DefaultAdapter.OnItemClickListener {

    private RecyclerView popularList,nowPlaying, topRated;
    private MovieAdapter movieAdapter;
    private DefaultAdapter defaultAdapter;
    private ArrayList<Movie> movieArrayList;
    private ArrayList<DefaultMovies> nowPlayingList, topRatedList;
    private RequestQueue mRequestQueue;
    ImageView search;

    public static final String POSTER = "poster_url";
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String BACK_POSTER = "back_poster";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String LANG = "lang";
    public static final String VOTE = "votes";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nowPlaying = findViewById(R.id.nowPlaying);
        popularList = findViewById(R.id.mainList);
        search = findViewById(R.id.search);
        topRated = findViewById(R.id.topRated);

        topRated.setHasFixedSize(true);
        popularList.setHasFixedSize(true);
        nowPlaying.setHasFixedSize(true);

        topRated.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        popularList.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        nowPlaying.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));


        movieArrayList = new ArrayList<>();
        nowPlayingList = new ArrayList<>();
        topRatedList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);

        showResult();
        showNowPlaying();
        showTopRated();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, DetailsActivity.class));
            }
        });

    }

    private void showTopRated() {
        String URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US&page=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i=0;i< jsonArray.length();i++){
                        JSONObject res = jsonArray.getJSONObject(i);
                        String title = res.getString("title");
                        String image = "https://image.tmdb.org/t/p/original"+res.getString("poster_path");
                        String original_language = res.getString("original_language");
                        float rating = (float) res.getDouble("vote_average");
                        String backdrop_path = "https://image.tmdb.org/t/p/original"+res.getString("backdrop_path");
                        String overview = res.getString("overview");
                        String release_date = res.getString("release_date");
                        int id = res.getInt("id");

                        topRatedList.add(new DefaultMovies(id, image,original_language, title,backdrop_path,overview,release_date, rating));
                    }

                    defaultAdapter = new DefaultAdapter(HomeActivity.this, topRatedList);
                    topRated.setAdapter(defaultAdapter);
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

    private void showNowPlaying() {
        String URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US&page=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i=0;i< jsonArray.length();i++){
                        JSONObject res = jsonArray.getJSONObject(i);
                        String title = res.getString("title");
                        String image = "https://image.tmdb.org/t/p/original"+res.getString("poster_path");
                        String original_language = res.getString("original_language");
                        float rating = (float) res.getDouble("vote_average");
                        String backdrop_path = "https://image.tmdb.org/t/p/original"+res.getString("backdrop_path");
                        String overview = res.getString("overview");
                        String release_date = res.getString("release_date");
                        int id = res.getInt("id");

                        nowPlayingList.add(new DefaultMovies(id, image,original_language, title,backdrop_path,overview,release_date, rating));
                    }

                    defaultAdapter = new DefaultAdapter(HomeActivity.this, nowPlayingList);
                    nowPlaying.setAdapter(defaultAdapter);
                    defaultAdapter.setOnItemClickListener(HomeActivity.this);

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

    private void showResult() {
        String URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US&page=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject res = jsonArray.getJSONObject(i);

                        String title = res.getString("title");
                        String image = "https://image.tmdb.org/t/p/original"+res.getString("poster_path");

                        movieArrayList.add(new Movie(title, image));
                    }

                    movieAdapter = new MovieAdapter(HomeActivity.this, movieArrayList);
                    popularList.setAdapter(movieAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        DefaultMovies clickedItem = nowPlayingList.get(position);
        intent.putExtra(TITLE, clickedItem.getTitle());
        intent.putExtra(POSTER, clickedItem.getPoster_path());
        intent.putExtra(BACK_POSTER, clickedItem.getBackdrop_path());
        intent.putExtra(OVERVIEW, clickedItem.getOverview());
        intent.putExtra(RELEASE_DATE, clickedItem.getRelease_date());
        intent.putExtra(VOTE, String.valueOf(clickedItem.getVote_average()));
        intent.putExtra(LANG, clickedItem.getOriginal_language());
        intent.putExtra(ID, String.valueOf(clickedItem.getId()));
        startActivity(intent);
    }
}
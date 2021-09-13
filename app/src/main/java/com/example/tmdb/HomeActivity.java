package com.example.tmdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tmdb.Adapter.DefaultAdapter;
import com.example.tmdb.Adapter.TVAdapter;
import com.example.tmdb.Adapter.TrendingAdapter;
import com.example.tmdb.Model.DefaultMovies;
import com.example.tmdb.Model.TV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements DefaultAdapter.OnItemClickListener, TrendingAdapter.OnItemClickListener, TVAdapter.OnItemClickListener {

    private RecyclerView popularList,nowPlaying, topRated, trendingRV, tvRV;
    private DefaultAdapter defaultAdapter, upcomingAdapter;
    private TrendingAdapter trendingAdapter;
    private ArrayList<DefaultMovies> nowPlayingList, topRatedList, movieArrayList, trendingArrayList;
    private ArrayList<TV> tvArrayList;
    private TVAdapter tvAdapter;
    private RequestQueue mRequestQueue;
    SnapHelper snapHelper;
    Timer timer;
    TimerTask timerTask;
    int position, full;

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
        tvRV = findViewById(R.id.tvRV);
        topRated = findViewById(R.id.topRated);
        trendingRV = findViewById(R.id.trendingRV);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        tvRV.setHasFixedSize(true);
        topRated.setHasFixedSize(true);
        popularList.setHasFixedSize(true);
        nowPlaying.setHasFixedSize(true);
        trendingRV.setHasFixedSize(true);

        tvRV.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        topRated.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        popularList.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        nowPlaying.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        trendingRV.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));

        movieArrayList = new ArrayList<>();
        nowPlayingList = new ArrayList<>();
        topRatedList = new ArrayList<>();
        trendingArrayList = new ArrayList<>();
        tvArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        snapHelper = new LinearSnapHelper();

        showResult();
        showTV();
        showNowPlaying();
        showTopRated();
        trending();
        autoScroll();

    }

    private void showTV() {
        String URLPopular = "https://api.themoviedb.org/3/tv/popular?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US&page=1";
        String URL = "https://api.themoviedb.org/3/tv/on_the_air?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US&page=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URLPopular, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject res = jsonArray.getJSONObject(i);

                        String title = res.getString("name");
                        String image = "https://image.tmdb.org/t/p/original"+res.getString("poster_path");
                        String original_language = res.getString("original_language");
                        float rating = (float) res.getDouble("vote_average");
                        String backdrop_path = "https://image.tmdb.org/t/p/original"+res.getString("backdrop_path");
                        String overview = res.getString("overview");
                        String release_date = res.getString("first_air_date");
                        int id = res.getInt("id");

                        tvArrayList.add(new TV(backdrop_path, title, original_language, overview, image,release_date ,id, rating));
                    }

                    tvAdapter = new TVAdapter(HomeActivity.this, tvArrayList);
                    tvRV.setAdapter(tvAdapter);
                    tvAdapter.setOnItemClickListener(HomeActivity.this);

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

    private void autoScroll(){
        if (timer==null&&timerTask==null){
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (position == full){
                        position = 0;
                        trendingRV.smoothScrollToPosition(position);
                        trendingRV.smoothScrollBy(5,0);
                    }else {
                        position++;
                        trendingRV.smoothScrollToPosition(position);
                    }
                }
            };
            timer.schedule(timerTask, 4000,4000);
        }
    }

    private void trending() {
        String URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US&page=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject res = jsonArray.getJSONObject(i);

                        String title = res.getString("original_title");
                        String image = "https://image.tmdb.org/t/p/original"+res.getString("poster_path");
                        String original_language = res.getString("original_language");
                        float rating = (float) res.getDouble("vote_average");
                        String backdrop_path = "https://image.tmdb.org/t/p/original"+res.getString("backdrop_path");
                        String overview = res.getString("overview");
                        String release_date = res.getString("release_date");
                        int id = res.getInt("id");

                        trendingArrayList.add(new DefaultMovies(id, image,original_language, title,backdrop_path,overview,release_date, rating));
                    }

                    trendingAdapter = new TrendingAdapter(HomeActivity.this, trendingArrayList);
                    trendingRV.setAdapter(trendingAdapter);
                    trendingAdapter.setOnItemClickListener(HomeActivity.this);

                    if (trendingArrayList!= null){
                        full = trendingArrayList.size();
                        for (int j=0;j<full;j++){
                            position++;
                        }
                        trendingRV.smoothScrollToPosition(0);
                    }

                    snapHelper.attachToRecyclerView(trendingRV);
                    trendingRV.smoothScrollBy(5,0);


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
                        String original_language = res.getString("original_language");
                        float rating = (float) res.getDouble("vote_average");
                        String backdrop_path = "https://image.tmdb.org/t/p/original"+res.getString("backdrop_path");
                        String overview = res.getString("overview");
                        String release_date = res.getString("release_date");
                        int id = res.getInt("id");

                        movieArrayList.add(new DefaultMovies(id, image,original_language, title,backdrop_path,overview,release_date, rating));
                    }

                    upcomingAdapter = new DefaultAdapter(HomeActivity.this, movieArrayList);
                    popularList.setAdapter(upcomingAdapter);
                    upcomingAdapter.setOnItemUPClickListener(HomeActivity.this);


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
                    defaultAdapter.setOnItemTopRatedClickListener(HomeActivity.this);

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

    @Override
    public void onItemUPClick(int position) {
        DefaultMovies upcomingClick = movieArrayList.get(position);
        Intent intent1 = new Intent(this, DetailsActivity.class);
        intent1.putExtra(TITLE, upcomingClick.getTitle());
        intent1.putExtra(POSTER, upcomingClick.getPoster_path());
        intent1.putExtra(BACK_POSTER, upcomingClick.getBackdrop_path());
        intent1.putExtra(OVERVIEW, upcomingClick.getOverview());
        intent1.putExtra(RELEASE_DATE, upcomingClick.getRelease_date());
        intent1.putExtra(VOTE, String.valueOf(upcomingClick.getVote_average()));
        intent1.putExtra(LANG, upcomingClick.getOriginal_language());
        intent1.putExtra(ID, String.valueOf(upcomingClick.getId()));
        startActivity(intent1);
    }

    @Override
    public void onItemTopClick(int position) {
        DefaultMovies upcomingClick = topRatedList.get(position);
        Intent intent1 = new Intent(this, DetailsActivity.class);
        intent1.putExtra(TITLE, upcomingClick.getTitle());
        intent1.putExtra(POSTER, upcomingClick.getPoster_path());
        intent1.putExtra(BACK_POSTER, upcomingClick.getBackdrop_path());
        intent1.putExtra(OVERVIEW, upcomingClick.getOverview());
        intent1.putExtra(RELEASE_DATE, upcomingClick.getRelease_date());
        intent1.putExtra(VOTE, String.valueOf(upcomingClick.getVote_average()));
        intent1.putExtra(LANG, upcomingClick.getOriginal_language());
        intent1.putExtra(ID, String.valueOf(upcomingClick.getId()));
        startActivity(intent1);
    }

    @Override
    public void onItemClickUP(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        DefaultMovies clickedItem = trendingArrayList.get(position);
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

    @Override
    public void onItemClickTV(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        TV clickedItem = tvArrayList.get(position);
        intent.putExtra(TITLE, clickedItem.getName());
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
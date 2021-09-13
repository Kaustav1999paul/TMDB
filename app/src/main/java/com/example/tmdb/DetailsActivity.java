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
import com.example.tmdb.Adapter.DefaultAdapter;
import com.example.tmdb.Adapter.GenresAdapter;
import com.example.tmdb.Model.Cast;
import com.example.tmdb.Model.DefaultMovies;
import com.example.tmdb.Model.Genres;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class DetailsActivity extends AppCompatActivity implements CastAdapter.OnItemClickListener, DefaultAdapter.OnItemClickListener {

    ImageView backgroundPoster, movie_posterV;
    BlurView blurView;
    Intent intent;
    FloatingActionButton back;
    String poster_path, original_language ,title, backdrop_path, overview, release_date, votes, id;
    TextView titleV, overviewV, releaseDateV, langV, ratingsV, statusV, recommendationsTitle, categoryTitle;
    RecyclerView castRV, categoryRV, recommendationRV;
    private RequestQueue mRequestQueue;
    private ArrayList<Cast> castArrayList;
    private ArrayList<Genres> genresArrayList;
    private ArrayList<DefaultMovies> similarArrayList;
    private CastAdapter castAdapter;
    private GenresAdapter genresAdapter;
    private DefaultAdapter similarAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        blurView = findViewById(R.id.blurView);
        theme();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recommendationsTitle = findViewById(R.id.recommendationsTitle);
        categoryTitle = findViewById(R.id.categoryTitle);
        castRV = findViewById(R.id.castRV);
        castRV.setHasFixedSize(true);
        castRV.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        categoryRV = findViewById(R.id.categoryRV);
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
        recommendationRV = findViewById(R.id.recommendationRV);
        recommendationRV.setHasFixedSize(true);
        recommendationRV.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
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
        similarArrayList = new ArrayList<>();
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
        releaseDateV.setText("Release date: "+release_date);
        Glide.with(this).load(backdrop_path).into(backgroundPoster);
        Glide.with(this).load(poster_path).into(movie_posterV);

        showCast(id);
        getMovieCategory(id);
        showRecommendations(id);
    }

    private void showRecommendations(String id) {
        String URL = "https://api.themoviedb.org/3/movie/"+id+"/recommendations?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US&page=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    if (jsonArray.length()!=0){
                        recommendationsTitle.setVisibility(View.VISIBLE);
                        recommendationRV.setVisibility(View.VISIBLE);

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject res = jsonArray.getJSONObject(i);
                            String title = res.getString("title");
                            String image = "https://image.tmdb.org/t/p/original"+res.getString("poster_path");
                            String original_language = res.getString("original_language");
                            float rating = (float) res.getDouble("vote_average");
                            String backdrop_path = "https://image.tmdb.org/t/p/original"+res.getString("backdrop_path");
                            String overview = res.getString("overview");
                            String release_date = res.getString("release_date");
                            int id = res.getInt("id");

                            similarArrayList.add(new DefaultMovies(id, image,original_language, title,backdrop_path,overview,release_date, rating));
                        }
                    }else {
                        recommendationsTitle.setVisibility(View.GONE);
                        recommendationRV.setVisibility(View.GONE);
                    }



                    similarAdapter = new DefaultAdapter(DetailsActivity.this, similarArrayList);
                    recommendationRV.setAdapter(similarAdapter);
                    similarAdapter.setOnItemUPClickListener(DetailsActivity.this);

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

    private void getMovieCategory(String id) {
        String URL = "https://api.themoviedb.org/3/movie/"+id+"?api_key=dda6d5e001bdb5b75de31631ec3fa716&language=en-US";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status= response.getString("status");

                    statusV.setText(status);

                    JSONArray jsonArray = response.getJSONArray("genres");

                    if (jsonArray.length()!=0){

                        categoryTitle.setVisibility(View.VISIBLE);
                        categoryRV.setVisibility(View.VISIBLE);

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject res = jsonArray.getJSONObject(i);

                            String name = res.getString("name");
                            genresArrayList.add(new Genres(name));
                        }
                    }else {
                        categoryTitle.setVisibility(View.GONE);
                        categoryRV.setVisibility(View.GONE);
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

    @Override
    public void onItemUPClick(int position) {
        DefaultMovies upcomingClick = similarArrayList.get(position);
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

    }
}
package com.zero.rektmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    SearchFilter searchFilter;
    RelativeLayout searchSpinners;
    String URL;
    RecyclerView movieDisplay;
    ArrayList<MovieListDetail> movieList;
    GridLayoutManager gridLayoutManager;
    SearchView searchView;
    Button retryBtn,nextBtn,prevButn;
    TextView retryTxt,pageNo;
    Spinner genreSpinner,qualitySpinner,ratingSpinner;
    MovieListAdapter movieListAdapter;
    ArrayAdapter<CharSequence> genreAdapter,qualityAdapter,ratingAdapter;
    RequestQueue mRequestQueue;
    int pageNumber;
    private boolean submitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing variables and object
        searchFilter = new SearchFilter();//custom one
        movieList = new ArrayList<>();
        searchSpinners = findViewById(R.id.search_relative_layout);
        gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        movieDisplay = findViewById(R.id.movie_display);
        searchView = findViewById(R.id.searchBar);
        retryBtn = findViewById(R.id.retryBtn);
        nextBtn =findViewById(R.id.nextButton);
        retryTxt = findViewById(R.id.retryTxt);
        prevButn = findViewById(R.id.prevButton);
        pageNumber = 0;
        pageNo =findViewById(R.id.pageNumber);
        Toast.makeText(this, "Long press on movie to get little detail", Toast.LENGTH_SHORT).show();
        //setting values
        submitted = false;
        URL = searchFilter.getURL();
       //searchSpinners.setVisibility(View.GONE);
        movieDisplay.setLayoutManager(gridLayoutManager);
        genreSpinner = findViewById(R.id.genreSpinner);
        qualitySpinner = findViewById(R.id.qualitySpinner);
        ratingSpinner = findViewById(R.id.ratingSpinner);
        //searchFilter.setPageNumber(1);

        pageNo.setText(searchFilter.getPageNumber());

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus )
                {
                    searchSpinners.setVisibility(View.VISIBLE);
                }
                else if(submitted && hasFocus)
                {
                    submitted = false;
                    searchSpinners.setVisibility(View.VISIBLE);
                }
                else {
                    searchSpinners.setVisibility(GONE);
                }
            }
        });

     //spinner started

        //initializing spinners
        genreAdapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.genre, android.R.layout.simple_spinner_item);
        qualityAdapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.quality, android.R.layout.simple_spinner_item);
        ratingAdapter= ArrayAdapter.createFromResource(MainActivity.this,R.array.rating,android.R.layout.simple_spinner_item);

        //setting dropdown resource
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //setting adapter
        genreSpinner.setAdapter(genreAdapter);
        qualitySpinner.setAdapter(qualityAdapter);
        ratingSpinner.setAdapter(ratingAdapter);

        //setting on SelectedMethods
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchFilter.setGenre(parent.getItemAtPosition(position).toString().toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        qualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchFilter.setQuality(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchFilter.setMin_rating(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner ended

        //CHECK INTERNET CONNECTION
        if(!checkInternet())
        {
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkInternet();
                }
            });
        }
//        mRequestQueue = Volley.newRequestQueue(this);
//        FillData(URL);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPageNumber = Integer.parseInt(searchFilter.getPageNumber());
                currentPageNumber++;
                String value = String.valueOf(currentPageNumber);
                searchFilter.setPageNumber(value);
                String url = searchFilter.getURL();
                try {
               //     Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
                    pageNo.setText(searchFilter.getPageNumber());
                    FillData(url);
                }
                catch (Exception ex)
                {
                    retryTxt.setText(ex.toString());
                }
            }
        });

        prevButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPageNumber = Integer.parseInt(searchFilter.getPageNumber());
                if(currentPageNumber == 1)
                {
                    Toast.makeText(MainActivity.this, "This is the first page", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    currentPageNumber--;
                    String value = String.valueOf(currentPageNumber);
                    searchFilter.setPageNumber(value);
                    nextBtn.setText(value);
                    String url = searchFilter.getURL();
                    try {
                       // Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
                        pageNo.setText(searchFilter.getPageNumber());
                        FillData(url);
                    }
                    catch (Exception ex)
                    {
                        retryTxt.setText(ex.toString());
                    }
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                submitted = true;
                if(query.length() == 0)
                {
                    searchFilter.setQuery("");
                }
                else{
                    searchFilter.setQuery(query.toLowerCase());
                }

                URL = searchFilter.getURL();
                searchSpinners.setVisibility(GONE);
                FillData(URL);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0)
                {
                    searchSpinners.setVisibility(GONE);
                }
                else
                {
                    searchSpinners.setVisibility(View.VISIBLE);
                }
            return false;
            }
        });

    }

    private void FillData(String url) {
        movieList.clear();
        try {
            movieListAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Toast.makeText(this,"Loading . . .", Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String data = response.toString();
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject dataMovie = new JSONObject(jsonObject.get("data").toString());
                    JSONArray moviesDetail = dataMovie.getJSONArray("movies");
                    for (int i = 0; i < moviesDetail.length(); i++) {
                        JSONObject movie = (JSONObject) moviesDetail.get(i);
                        movieList.add(new MovieListDetail(movie.getString("medium_cover_image"), movie.getString("title"), movie.getString("rating"), movie.getString("year"), movie.getString("id")));
                    }
                    movieListAdapter = new MovieListAdapter(MainActivity.this, movieList);
                    movieDisplay.setAdapter(movieListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error Encountered", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);
    }


    public boolean checkInternet()
    {
        try {
            String command = "ping -c 1 www.google.com";
            if(Runtime.getRuntime().exec(command).waitFor() == 0)
            {
                retryTxt.setVisibility(GONE);
                retryBtn.setVisibility(GONE);
                mRequestQueue = Volley.newRequestQueue(this);
                FillData(URL);
            }
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            retryTxt.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);

            return false;
        }
    }
}
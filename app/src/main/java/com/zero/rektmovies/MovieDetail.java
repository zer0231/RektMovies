package com.zero.rektmovies;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MovieDetail extends AppCompatActivity {
    RequestQueue mRequestQueue;
    FullDetail Detail;
    TextView detailSummary,detailRating,detailGenre,detailTitle;
    ImageView detailImage;
    LinearLayout downloadLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);

        //setting full detail object
        Detail= new FullDetail();
        //getting id from previous activity
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String url = "https://yts.mx/api/v2/movie_details.json?movie_id="+id;

        mRequestQueue = Volley.newRequestQueue(this);
        Detail = FillData(url);

    }
    private FullDetail FillData(String url) {

        FullDetail fullDetail = new FullDetail();
        List<Torrent> torrents = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String jString = response.toString();
                    JSONObject jsonObject = new JSONObject(jString);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject movie = data.getJSONObject("movie");
                    String title = movie.getString("slug");
                    fullDetail.setImage(movie.getString("medium_cover_image"));

                    fullDetail.setTitle(movie.getString("slug"));
                    fullDetail.setRating(movie.getDouble("rating"));
                    fullDetail.setSummary(movie.getString("description_full"));
                    JSONArray torrentArray = movie.getJSONArray("torrents");
                    JSONArray genreArray = movie.getJSONArray("genres");
                    for(int i =0;i<torrentArray.length();i++)
                    {
                        torrents.add(new Torrent(title,torrentArray.getJSONObject(i).getString("hash"),torrentArray.getJSONObject(i).getString("quality"),torrentArray.getJSONObject(i).getString("type")));
                    }
                    fullDetail.setTorrents(torrents);

                  //  Toast.makeText(MovieDetail.this, genreArray.length(), Toast.LENGTH_SHORT).show();
                    for(int j=0;j<genreArray.length();j++)
                    {
                        genres.add(genreArray.getString(j));
                    }
                    fullDetail.setGenres(genres);

                    //inintalizing objects;
                    detailSummary = findViewById(R.id.detail_summary);
                    detailRating = findViewById(R.id.detail_rating);
                    detailGenre = findViewById(R.id.detail_genres);
                    detailImage=findViewById(R.id.detail_image);
                    detailTitle = findViewById(R.id.detail_title);
                    downloadLinearLayout = findViewById(R.id.downloadLilnearLayout);

                    //setting values
                    detailSummary.setText(fullDetail.getSummary());
                    detailRating.setText(String.valueOf(fullDetail.getRating()));
                    detailTitle.setText(fullDetail.getTitle());
                    Glide.with(MovieDetail.this).load(Detail.getImage())
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.cover)//to show loading
                            .into(detailImage);//to set after loaded

                    for(int i=0;i<fullDetail.getGenres().size();i++)
                    {
                        String toAddGenres = "";
                        toAddGenres +=fullDetail.getGenres().get(i)+"\n";
                        detailGenre.setText(toAddGenres);
                    }

                    for(int i =0;i<fullDetail.getTorrents().size();i++)
                    {
                        MaterialButton button1 =new MaterialButton(MovieDetail.this);
                        button1.setText( fullDetail.getTorrents().get(i).quality+" "+fullDetail.getTorrents().get(i).type);

                        int finalI = i;
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CopyToClipboard(fullDetail.getTorrents().get(finalI).magneticURLGenerator());
                                Toast.makeText(MovieDetail.this, "COPIED to your clipboard", Toast.LENGTH_SHORT).show();
                            }
                        });
                        downloadLinearLayout.addView(button1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetail.this, "Cannot get detail !!! ", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);
        return fullDetail;
    }

    public void CopyToClipboard(String download_url)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("test",download_url);
        clipboard.setPrimaryClip(clip);
    }
}

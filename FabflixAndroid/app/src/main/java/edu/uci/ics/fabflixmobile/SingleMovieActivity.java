package edu.uci.ics.fabflixmobile;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleMovieActivity extends ActionBarActivity {

    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            String movieId = bundle.getString("movieId");
            Toast.makeText(this, "movieId: " + bundle.getString(movieId), Toast.LENGTH_LONG).show();
            ((TextView) findViewById(R.id.single_movie_text)).setText(movieId);
        };

        }
    }

//    public void connectToTomcat(View view){
//        // shared across Activities
//        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
//        final StringRequest queryRequest = new StringRequest(Request.Method.POST, "https://10.0.2.2:8443/Project2/SingleMovie2",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.d("JSON contents: ", response);
//                            JSONArray responseJsonArray = new JSONArray(response);
//                            for (int i = 0; i < responseJsonArray.length(); i++) {
//                                JSONObject line = responseJsonArray.getJSONObject(i);
//                                String id = line.getString("id");
//                                String title = line.getString("title");
//                                String year = line.getString("year");
//                                String director = line.getString("director");
//                                String stars = line.getString("stars");
//                                String genre = line.getString("genres");
//                                String rating = line.getString("rating");
//
//                                result = "ID: " + id + "\n" +
//                                        "Title: " + title + "\n" +
//                                        "Year: " + year + "\n" +
//                                        "Director: " + director + "\n" +
//                                        "Stars: " + stars + "\n" +
//                                        "Genres: " + genre + "\n" +
//                                        "Rating: " + rating;
//
////                                currentPage.add(result);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("movie.error", error.toString());
//                    }
//                }
//        );
//        queue.add(queryRequest);
//    }
//}
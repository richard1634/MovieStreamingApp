package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
public class BlueActivity extends ActionBarActivity {
    private String query_result;
    private String result;
    private int current_result = 0;
    private int prn_result = 0;
    private ArrayList<String> currentPage = new ArrayList<String>();
    private List<String> resultPage = new ArrayList<String>();
    ListView myListView;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        myListView = (ListView)findViewById(R.id.myListView);

        Bundle bundle = getIntent().getExtras();
        Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();

        String query = bundle.getString("message");
        if (query != null && !"".equals(query)) {
            ((TextView) findViewById(R.id.last_page_msg_container)).setText("Search:"+ query);

            query_result = query;

            // HERES WHERE WE HAVE OUR QUERY
            // LETS STRING SPLIT, REMOVE STOP WORDS, AND THEN MYSQL QUERY IN THE BACKEND
            // THEN WE SEND DATA OVER JSONOBJECT
            // PARSE JSON OBJECTS THEN YOUR GONNA HAVE TO LISTVIEW IT
            //ANDROID PROBBALY HAS THEIR OWN FORM OF PAGINATION
        }
        connectToTomcat(null);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = myListView.getItemAtPosition(position);
                Log.d("list type", listItem.toString());
                String stringItem = listItem.toString();
                Log.d("stringItem", stringItem);
                goToSingleMovie(null,stringItem);
            }
        });

    }

    public void prevBtn(View view){
        if (prn_result-5<=0)
        {
            Toast.makeText(this, "You're on the first page", Toast.LENGTH_LONG).show();
        }
        else {
            current_result -=5;
            prn_result -= 5;
            resultPage = currentPage.subList(current_result,prn_result);
            arrayAdapter = new ArrayAdapter<String>(BlueActivity.this, android.R.layout.simple_list_item_1, resultPage);
            myListView.setAdapter(arrayAdapter);
        }
    }

    public void nextBtn(View view){
        if (prn_result==currentPage.size())
        {
            Toast.makeText(this, "You're on the Last page", Toast.LENGTH_LONG).show();
        }
        else {
            if ((prn_result + 5 < currentPage.size())){
                current_result +=5;
                prn_result += 5;
                myListView.setAdapter(null);
                resultPage = currentPage.subList(current_result,prn_result);
                arrayAdapter = new ArrayAdapter<String>(BlueActivity.this, android.R.layout.simple_list_item_1, resultPage);
                myListView.setAdapter(arrayAdapter);
            }
            else if ((prn_result + 5 > currentPage.size())){
                int num = currentPage.size() % 5;
                current_result = current_result + 5;
                prn_result += num;
                myListView.setAdapter(null);
                resultPage = currentPage.subList(current_result, prn_result);
                arrayAdapter = new ArrayAdapter<String>(BlueActivity.this, android.R.layout.simple_list_item_1, resultPage);
                myListView.setAdapter(arrayAdapter);
            }

        }
    }

    private void DisplayResults(){

        resultPage = currentPage.subList(current_result,prn_result);
        arrayAdapter = new ArrayAdapter<String>(BlueActivity.this, android.R.layout.simple_list_item_1, resultPage);
        myListView.setAdapter(arrayAdapter);
    }


    public void connectToTomcat(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://10.0.2.2:8443/your-project-name/SearchResultsServlet2",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("login.success", response);
                            JSONArray responseJsonArray = new JSONArray(response);

                            for (int i = 0;i<responseJsonArray.length();i++){
                                JSONObject line = responseJsonArray.getJSONObject(i);
                                String id = line.getString("id");
                                String title = line.getString("title");
                                String year = line.getString("year");
                                String director = line.getString("director");
                                String stars = line.getString("stars");
                                String genre = line.getString("genres");
                                String rating = line.getString("rating");

                                result = "ID: "+ id+"\n"+
                                                "Title: "+ title+"\n"+
                                                "Year: "+ year+"\n"+
                                                "Director: "+ director+"\n"+
                                                "Stars: "+  stars+"\n"+
                                                "Genres: "+  genre+"\n"+
                                                "Rating: "+  rating;

                                currentPage.add(result);
                            }
                            if (currentPage.size() < 5){
                                prn_result = currentPage.size();
                            }
                            else{
                                prn_result = 5;
                            }
                            DisplayResults();
                        }

                        catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("movie.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                params.put("title", query_result); // change to var
                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);

    }
    public void goToSingleMovie(View view, String movieId)
    {
        Intent goToIntent = new Intent(this, SingleMovieActivity.class);
        goToIntent.putExtra("movieId", movieId);
        startActivity(goToIntent);
    }

}

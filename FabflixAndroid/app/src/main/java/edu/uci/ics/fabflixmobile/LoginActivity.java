package edu.uci.ics.fabflixmobile;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("last_activity") != null) {
                Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();
            }
            String msg = bundle.getString("message");
            if (msg != null && !"".equals(msg)) {
                ((TextView) findViewById(R.id.last_page_msg_container)).setText(msg);
            }
        }
    }

    public void goToRed(View view) {
        Intent goToIntent = new Intent(this, RedActivity.class);
          startActivity(goToIntent);
    }
    public void connectToTomcat(View view) {

        // no user is logged in, so we must connect to the server
        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, "https://10.0.2.2:8443/2019w-project4-login-example/api/username",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("username.reponse", response);
                    ((TextView) findViewById(R.id.http_response)).setText(response);
                    goToRed(null);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                    Log.d("username.error", error.toString());
                }
            }
        );


        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://10.0.2.2:8443/2019w-project4-login-example/api/login",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                try {
                    Log.d("login.success", response);
                    JSONObject responseJsonObject = new JSONObject(response);
                    if (responseJsonObject.getString("status").equalsIgnoreCase("fail")){
                        ((TextView) findViewById(R.id.http_response)).setText("Wrong User/name Password");
                    }
                    // Add the request to the RequestQueue.
                    else if (responseJsonObject.getString("status").equalsIgnoreCase("success")){
                        queue.add(afterLoginRequest);
                    }
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
                    Log.d("login.error", error.toString());
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                String username = ((EditText) findViewById(R.id.txtUser)).getText().toString();
                String password = ((EditText) findViewById(R.id.txtPass)).getText().toString();
                final Map<String, String> params = new HashMap<String, String>();
                params.put("username", username); // change to var
                params.put("password", password);
                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);

    }
    }

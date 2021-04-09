package com.joshi.dhvaniimplementing01;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionActivity extends AppCompatActivity {
    EditText editText;
    Button result;
    Context thisCon;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        thisCon = this;
        editText = findViewById(R.id.song_url);
        result = findViewById(R.id.result_button);


        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                putRequest();
//                getRequest();
                textView = findViewById(R.id.music_genre_classification);

                RequestQueue requestQueue;

                requestQueue = Volley.newRequestQueue(thisCon);
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("url",editText.getText().toString());
                    System.out.println("local url is " + editText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                        "http://mejoshii.pythonanywhere.com/audioProAPI/1",
                        jsonObject,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("onResponse: ", response.toString()) ;

                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Code Error PUT is : " + error);

                    }
                });

                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET,
                        "http://mejoshii.pythonanywhere.com/audioProAPI/1",
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    textView.setText(response.getString("prediction "));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Error code in GET is " + error.getMessage());
                    }
                });

                requestQueue.add(jsonObjectRequest).setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(jsonObjectRequest1).setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            }
        });

    }


    }
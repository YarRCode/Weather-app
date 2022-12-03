package com.whether.wheterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MoreInfoActivity extends AppCompatActivity {

    String urlForFiveDays;

    String dt[] = new String[40];
    int temp[] = new int[40];
    String whetherList[] = new String[40];
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        init();
    }

    public void init(){
        listView = findViewById(R.id.listMoreInfoView);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            urlForFiveDays= null;
        } else {
            urlForFiveDays= extras.getString("urlForFiveDays");
        }

        httpCallFiveDays(urlForFiveDays);
    }

    public void httpCallFiveDays(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            for (int i = 0; i < 40; i++){
                                JSONArray listArray = jsonObject.getJSONArray("list");
                                JSONObject list = listArray.getJSONObject(i);
                                JSONObject main = list.getJSONObject("main");
                                JSONArray weatherArray = list.getJSONArray("weather");
                                JSONObject weather = weatherArray.getJSONObject(0);

                                whetherList[i] = weather.getString("main");
                                temp[i] = (int) main.getDouble("temp");
                                temp[i] -= 273;
                                dt[i] = list.getString("dt_txt");
                            }

                            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(),dt, temp, whetherList);
                            listView.setAdapter(customBaseAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        });

        queue.add(stringRequest);
    }
}
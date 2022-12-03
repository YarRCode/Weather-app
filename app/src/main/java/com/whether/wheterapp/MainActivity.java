package com.whether.wheterapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whether.wheterapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button btnSearchCity, btnFiveDays;
    String cityName = "city", oldCityName = "input", coutryName, tempMain, whetherMain, coordLon, coordLat, oldcoordLat = "efgh", oldcoordLon = "abcd";

    TextView tv, countryTv, tempTv, whetherTv;
    ListView listView;

    String ur ="https://api.openweathermap.org/data/2.5/weather?q=input&appid=78a2622a033a9442913770fedbd6102f";
    String urlForFiveDays = "https://api.openweathermap.org/data/2.5/forecast?lat=efgh&lon=abcd&appid=78a2622a033a9442913770fedbd6102f";

    Intent intent;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        load();
    }

    private void init(){
        btnSearchCity = findViewById(R.id.searchWthether);
        btnFiveDays = findViewById(R.id.fiveDays);

        tv = findViewById(R.id.tvCity);
        countryTv = findViewById(R.id.tvCountry);
        tempTv = findViewById(R.id.tvTemp);
        whetherTv = findViewById(R.id.tvWhetherMain);

        listView = findViewById(R.id.listView);

        intent = new Intent(this, nextFiveDayWhether.class);

        btnSearchCity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                myDialoge();
            }
        });

        btnFiveDays.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                startFiveDaysActivity(v);
            }
        });
    }

    private void save(){
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("textColoum",cityName);
        editor.apply();
    }
    private void load(){
        pref = getSharedPreferences("pref", MODE_PRIVATE);

        cityName = pref.getString("textColoum", cityName);
        tv.setText(cityName);

        ur = ur.replace("input", cityName);
        oldCityName = cityName;

        httpCall(ur);
    }

    private void myDialoge()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog alertDialog = (AlertDialog) dialogInterface;
                EditText editText = alertDialog.findViewById(R.id.editTextCityName);
                assert editText != null;

                tv.setText(editText.getText().toString());
                cityName = editText.getText().toString();
                ur = ur.replace(oldCityName, cityName);
                oldCityName = cityName;
                save();
                httpCall(ur);
            }
        });
        builder.setView(cl);
        builder.show();
    }

    public void httpCall(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject sys = jsonObject.getJSONObject("sys");
                            coutryName = sys.getString("country");
                            countryTv.setText(coutryName);

                            JSONObject main = jsonObject.getJSONObject("main");
                            tempMain = main.getString("temp");
                            int intTemp = (int) Double.parseDouble(tempMain);
                            intTemp -= 273;
                            tempTv.setText(intTemp + "°C");

                            JSONArray weatherArray = jsonObject.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            whetherMain = weather.getString("main");
                            whetherTv.setText(whetherMain);

                            JSONObject coord = jsonObject.getJSONObject("coord");
                            coordLon = coord.getString("lon");
                            coordLat = coord.getString("lat");
                            urlForFiveDays = urlForFiveDays.replace(oldcoordLon,coordLon);
                            urlForFiveDays = urlForFiveDays.replace(oldcoordLat,coordLat);
                            oldcoordLon = coordLon;
                            oldcoordLat = coordLat;
                            intent.putExtra("urlForFiveDays", urlForFiveDays);

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
    public void startFiveDaysActivity(View v)
    {
        startActivity(intent);
    }
}
package com.bkacad.nnt.demoopenweatherd02k11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtCity;
    private Button btnSearch;
    private TextView tvResult;
    private ImageView imgWeather;
    // Thay API KEY CỦa các em. vào xem trên Openweather
    private static final String API_KEY = "xxxxxxxxxxx";

    private void initUI(){
        edtCity = findViewById(R.id.edt_main_city);
        btnSearch = findViewById(R.id.btn_main_search);
        tvResult = findViewById(R.id.tv_main_result);
        imgWeather = findViewById(R.id.img_main_weather);
    }

    private String createURL(String city){
        return String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=vi", city, API_KEY);
    }

    private String createIconURL(String iconCode){
        return String.format("https://openweathermap.org/img/wn/%s@4x.png",iconCode );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        // Sự kiẹn khi click vao button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = edtCity.getText().toString();
                if(input.isEmpty()){
                    edtCity.setError("Hãy nhập dữ liệu");
                    return;
                }
                // Tạo URL
                String URL = createURL(input);

                // Tạo request lên Openweather
                StringRequest myRequest = new StringRequest(Request.Method.GET, URL,
                        response -> {
                            try{
                                JSONObject myJsonObject = new JSONObject(response);

                                double nhietDo = myJsonObject.getJSONObject("main").getDouble("temp");
                                double camNhanNhu = myJsonObject.getJSONObject("main").getDouble("feels_like");


                                String description = myJsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                                String icon = myJsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

                                tvResult.setText("Nhiet do "+ nhietDo + " - cảm nhận như "+ camNhanNhu + " Thời tiết: "+description);

                                Picasso.get().load(createIconURL(icon)).into(imgWeather);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        volleyError -> Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
                );

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(myRequest);

            }
        });
    }
}
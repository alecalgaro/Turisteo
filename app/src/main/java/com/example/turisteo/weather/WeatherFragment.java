package com.example.turisteo.weather;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.turisteo.R;
import com.example.turisteo.home.MainActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    LinearLayout bg_weather_top;
    LinearLayout bg_weather_bottom;
    LinearLayout background;
    ProgressBar progressBar;
    TextView tv_warning;
    TextView tv_progressBar;
    TextView tv_city;
    TextView tv_temp;
    TextView tv_temp_max;
    TextView tv_temp_min;
    TextView tv_description;
    TextView tv_value_min;
    TextView tv_value_max;
    ImageView icon_weather;

    public String url, city, latitude, longitude;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewWeatherFragment = inflater.inflate(R.layout.fragment_weather, container, false);

        bg_weather_top = viewWeatherFragment.findViewById(R.id.bg_weather_top);
        bg_weather_bottom = viewWeatherFragment.findViewById(R.id.bg_weather_bottom);
        background = viewWeatherFragment.findViewById(R.id.background);
        progressBar = viewWeatherFragment.findViewById(R.id.progressBar);
        tv_warning = viewWeatherFragment.findViewById(R.id.tv_warning);
        tv_progressBar = viewWeatherFragment.findViewById(R.id.tv_progressBar);
        tv_city = viewWeatherFragment.findViewById(R.id.tv_city);
        tv_description = viewWeatherFragment.findViewById(R.id.tv_description_weather);
        tv_temp = viewWeatherFragment.findViewById(R.id.tv_temp);
        tv_temp_max = viewWeatherFragment.findViewById(R.id.tv_temp_max);
        tv_value_max = viewWeatherFragment.findViewById(R.id.tv_value_max);
        tv_temp_min = viewWeatherFragment.findViewById(R.id.tv_temp_min);
        tv_value_min = viewWeatherFragment.findViewById(R.id.tv_value_min);
        icon_weather = viewWeatherFragment.findViewById(R.id.icon_weather);

        // Obtengo los datos de la ciudad elegida para hacer la consulta del clima
        city = ((MainActivity)this.getActivity()).city;
        latitude = ((MainActivity)this.getActivity()).latitude;
        longitude = ((MainActivity)this.getActivity()).longitude;

        // Si ya se eligio una ciudad en ConfigFragment (tendremos city, latitude y longitude) puedo hacer la consulta,
        // si no es asi dejo el mensaje indicando al usuario que debe elegir una ciudad para ver su clima
        if(!city.equals("") && !latitude.equals("0") && !longitude.equals("0")){
            tv_warning.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            tv_progressBar.setVisibility(View.VISIBLE);

            // URL para la consulta a la API.
            // Ingresando esa URL en el navegador con una latitud y longitud puedo ver como es el JSON que devuelve.
            url = "https://api.openweathermap.org/data/2.5/weather?&lang=es&units=metric&lat="+latitude+"&lon="+longitude+"&appid=d319e23ac8ff17680001c1213ccec74e";

            // Consulta a la API de OpenWeatherMap usando la libreria Volley
            StringRequest postResquest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);   // si el JSON viene como objeto
                        //JSONArray jsonArray = new JSONArray(response);    // si el JSON viene como array

                        // Descripcion del clima
                        JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        tv_description.setText(jsonObjectWeather.getString("description"));

                        // Icono del clima
                        String icon = jsonObjectWeather.getString("icon");
                        Picasso.get()
                                .load("https://openweathermap.org/img/wn/"+ icon +"@2x.png")
                                .into(icon_weather);

                        // Cambio el fondo de acuerdo al clima
                        switch (icon) {
                            case "01d":
                            case "02d":
                                bg_weather_bottom.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_weather_sun_bottom));
                                bg_weather_top.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_weather_sun_top));
                                break;
                            case "03d":
                            case "04d":
                            case "13d":
                            case "50d":
                                bg_weather_bottom.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_weather_clouds_bottom));
                                bg_weather_top.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_weather_clouds_top));
                                break;
                            case "09d":
                            case "10d":
                            case "11d":
                                bg_weather_bottom.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_weather_rain_bottom));
                                bg_weather_top.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_weather_rain_top));
                                break;
                            case "01n":
                            case "02n":
                            case "03n":
                            case "04n":
                            case "09n":
                            case "10n":
                            case "11n":
                            case "13n":
                            case "50n":
                                bg_weather_bottom.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_weather_night_bottom));
                                bg_weather_top.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_weather_night_top));
                                break;
                        }

                        // Temperatura actual, maxima y minima
                        JSONObject jsonArrayMain = jsonObject.getJSONObject("main");
                        tv_temp.setText(jsonArrayMain.getString("temp"));
                        tv_value_max.setText(jsonArrayMain.getString("temp_max"));
                        tv_value_min.setText(jsonArrayMain.getString("temp_min"));

                        tv_city.setText(city);
                        background.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        tv_progressBar.setVisibility(View.INVISIBLE);

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.getMessage());
                }
            });
            Volley.newRequestQueue(getContext()).add(postResquest);

        }
        return viewWeatherFragment;
    }
}
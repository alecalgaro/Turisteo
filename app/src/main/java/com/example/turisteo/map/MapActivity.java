package com.example.turisteo.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turisteo.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int REQUEST_CODE = 1;
    GoogleMap mMap;
    Button btn_googleMaps;
    ProgressBar progressBar;
    TextView tv_progressBar;

    public double user_latitude = 0, user_longitude = 0;
    public double place_latitude = 0, place_longitude = 0;
    public String place_name;
    public LatLng user_coordinates, place_coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btn_googleMaps = findViewById(R.id.btn_googleMaps);
        progressBar = findViewById(R.id.progressBar);
        tv_progressBar = findViewById(R.id.tv_progressBar);

        // Obtengo la latitud, longitud y nombre del lugar elegido:
        place_latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        place_longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));
        place_name = getIntent().getStringExtra("name");

        // Llamo al metodo para obtener las coordenadas del usuario usando el GPS
        getUserCoordinates();

        // Obtengo el fragment correspondiente al mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);  // al momento de ejecutar esta linea llama al metodo onMapReady

        // Intent con Google Maps (al presionar el boton te da la opcion de abrir la app de Google Maps o desde el navegador)
        btn_googleMaps.setOnClickListener(v -> {
            // Si el usuario activo su ubicacion y acepto el permiso, paso tanto la direccion del usuario como la
            // direccion del lugar y al abrir Google Maps ya te marca la ruta e indicaciones.
            if(user_latitude != 0 && user_longitude != 0){
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+user_latitude+","+user_longitude+"&daddr="+place_latitude+","+place_longitude));
                startActivity(intent);
            } else{
                // si no tengo la ubicacion del usuario (porque no tiene la ubicacion activada o no otorgo el permiso),
                // paso solo la direccion de destino (daddr) y Google Maps se encarga de pedir la ubicación actual.
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+place_latitude+","+place_longitude));
                startActivity(intent);
            }
        });
    }

    // getUserCoordinates: si no se tiene el permiso otorgado (!=) se lo solicita, y si ya fue otorgado paso
    // directo al else donde llamo al metodo getCoordinates() para obtener las coordenadas del usuario
    public void getUserCoordinates() {
        // Si no tiene el permiso ACCESS_FINE_LOCATION otorgado (!= granted) se lo solicito (requestPermissions)
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        } else {
            getCoordinates();
        }
    }

    // onRequestPermissionsResult: si en getUserCoordinates() se entro al if porque aun no se ha otorgado el
    // permiso obtener la localizacion, en este metodo se lo solicita (muestra el cartel con la solicitud).
    // Si el usuario lo otorga (granted) llamo a getCoordinates() y sino indico que el permiso fue denegado.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCoordinates();
            } else {
                Toast.makeText(this, "Permiso de localización denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    // getCoordinate: una vez aceptado el permiso se obtienen las coordenadas del usuario mediante el GPS
    private void getCoordinates() {
        try {
            //progressBar.setVisibility(View.VISIBLE);

            // Para la ubicación del usuario con el GPS:
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);      // intervalo de tiempo en que se estara actualizando la ubicacion del usuario en el mapa
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);    // prioridad que tendra el GPS para estar trabajando (con ese valor PRIORITY_HIGH_ACCURACY le damos mucha importancia para que tenga mayor precision)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;     // Si no se acepto el permiso
            }

            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                // onLocationResult: obtengo las coordenadas del usuario (latitud y longitud)
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(MapActivity.this).removeLocationUpdates(this);
                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                        user_latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                        user_longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    }
                    //progressBar.setVisibility(View.GONE);
                }
            }, Looper.myLooper());

        }catch (Exception ex){
            System.out.println("Error al obtener la ubicación del usuario:" + ex);
        }
    }

    // onMapReady: metodo para generar los marcadores en el mapa y setear las opciones que se quieren
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);     // tipo de mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);    // habilita los botones para hacer zoom
        progressBar.setVisibility(View.VISIBLE);
        tv_progressBar.setVisibility(View.VISIBLE);

        // Ejecuto luego de 4 segundos para esperar a que se haya obtenido la ubicacion del usuario
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Compruebo si se obtuvo la ubicacion del usuario (latitud y longitud != 0)
                if(user_latitude != 0 && user_longitude != 0){
                    user_coordinates = new LatLng(user_latitude, user_longitude);
                    mMap.addMarker(new MarkerOptions()      // agrego un marcador en la ubicacion del usuario
                            .position(user_coordinates)
                            .title("Tu ubicación actual"))
                            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_user));
                }else{
                    Toast.makeText(MapActivity.this, "No se pudo acceder a su ubicación actual", Toast.LENGTH_LONG).show();
                }

                // Agrego un marcador en la ubicación del lugar elegido
                place_coordinates = new LatLng(place_latitude, place_longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(place_coordinates)
                        .title(place_name))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_place));
                // a mMap.moveCamera le paso las coordenadas y el nivel de zoom, usando "newLatLngZoom", sino con "newLatLng" solo las coordenadas
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place_coordinates, 15));

                progressBar.setVisibility(View.GONE);      // quito el progressBar y el mensaje para mostrar el mapa
                tv_progressBar.setVisibility(View.GONE);

                // Centrar mapa en los dos marcadores:
                // LatLngBounds.Builder() me permite crear los limites para el mapa
                LatLngBounds.Builder constructor = new LatLngBounds.Builder();
                // Le paso las coordenadas del lugar y las del usuario
                constructor.include(place_coordinates);
                // Compruebo si se obtuvieron las coordenadas del usuario antes de agregar
                if(user_latitude != 0 && user_longitude != 0){
                    constructor.include(user_coordinates);
                }
                // Almaceno en una variable "limits" los limites obtenidos con esos marcadores
                LatLngBounds limits = constructor.build();
                // Obtengo el ancho y alto del dispositivo
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                // Genero un padding con el alto obtenido y un porcentaje
                int padding = (int) (height * 0.05); // 5% de espacio (padding) superior e inferior
                // Con el metodo CameraUpdateFactory cambio la camara segun los valores generados anteriormente
                CameraUpdate center_markers = CameraUpdateFactory.newLatLngBounds(limits, width, height, padding);
                // Muevo la camara y la centro en los marcadores usando el metodo animateCamera
                mMap.animateCamera(center_markers);

            }}, 4000);
    }
}
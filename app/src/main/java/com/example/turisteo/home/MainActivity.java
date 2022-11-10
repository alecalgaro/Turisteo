package com.example.turisteo.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.turisteo.R;
import com.example.turisteo.config.ConfigFragment;
import com.example.turisteo.favorites.FavoritesFragment;
import com.example.turisteo.map.MapFragment;
import com.example.turisteo.place.PlaceFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Fragments del bottom_navigation
    PlaceFragment placeFragment = new PlaceFragment();
    FavoritesFragment favoritesFragment = new FavoritesFragment();
    MapFragment mapFragment = new MapFragment();
    ConfigFragment configFragment = new ConfigFragment();

    // Fragments del menu de navegacion superior en el Home, para filtrar por categoria de lugar
    HistoricalPlacesFragment historicalPlacesFragment = new HistoricalPlacesFragment();
    BeachPlacesFragment beachPlacesFragment = new BeachPlacesFragment();
    FoodPlacesFragment foodPlacesFragment = new FoodPlacesFragment();
    OthersPlacesFragment othersPlacesFragment = new OthersPlacesFragment();

    // BottomNavigation para el menu inferior
    BottomNavigationView navigation;

    // TabLayout para el menu superior usado en la pantalla principal
    TabLayout tabLayout;

    // Bundle para pasar la informacion de los array a los fragments de categoria de lugares.
    // Esos array se llenan en ConfigFragment luego de hacer la consulta a la BD de Firebase y se pasar ahi mismo en este bundle.
    public Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Al iniciar cargo el configFragment
        String goToFavorite = getIntent().getStringExtra("goToFavorite");   // esto lo puse para probar ir al favoritesFragment cuando elimino
        if(goToFavorite != null){                                                 // uno y asi tener la lista actualizada pero no sirve porque es como
            loadFragment(favoritesFragment);                                      // reiniciar y ya no aparen los datos de lugares en inicio
        }else{
            loadFragment(configFragment);
        }

        // TabLayout: menu de navegacion superior para mostrar lugares por tipo (lugares historicos, restaurantes, etc.)
        // Lo uso en MainActivity y no en HomeFragment porque debo ir reemplazando el fragment y sino perdería esta barra de navegacion
        // o la deberia poner en todos los fragments que la usen y seria repetir codigo innecesario.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        loadFragment(historicalPlacesFragment);
                        break;
                    case 1:
                        loadFragment(beachPlacesFragment);
                        break;
                    case 2:
                        loadFragment(foodPlacesFragment);
                        break;
                    case 3:
                        loadFragment(othersPlacesFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}   // no se usa pero es necesario implementarlo

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}   // no se usa pero es necesario implementarlo
        });
    }

    // Metodo para detectar que boton se presiona en el bottom navigation y cargar el fragment correspondiente
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Cargo el fragment que corresponde segun el boton que se presiona en el bottom navigation
            switch (item.getItemId()){
                case R.id.configFragment:
                    loadFragment(configFragment);
                    tabLayout.setVisibility(View.GONE);
                    return true;
                case R.id.homeFragment:
                    loadFragment(historicalPlacesFragment);
                    tabLayout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.placeFragment:
                    loadFragment(placeFragment);
                    tabLayout.setVisibility(View.GONE);
                    return true;
                case R.id.favoritesFragment:
                    loadFragment(favoritesFragment);
                    tabLayout.setVisibility(View.GONE);
                    return true;
                case R.id.mapFragment:
                    loadFragment(mapFragment);
                    tabLayout.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    // Metodo que reemplaza el fragment correspondiente en el FrameLayout (de id "frame_container") definido en el xml
    public void loadFragment(Fragment fragment){
        // al carga los fragment de tipos de lugares deberia recibir el array completo y filtrarlos por tipo para mostrar los que corresponden
        // para eso deberia agregar un campo en Firebase que sea "type" para cada lugar.
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}
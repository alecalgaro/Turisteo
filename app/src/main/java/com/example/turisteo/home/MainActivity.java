package com.example.turisteo.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.turisteo.R;
import com.example.turisteo.favorites.FavoritesFragment;
import com.example.turisteo.map.MapFragment;
import com.example.turisteo.place.PlaceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Fragments
    HomeFragment homeFragment = new HomeFragment();
    PlaceFragment placeFragment = new PlaceFragment();
    FavoritesFragment favoritesFragment = new FavoritesFragment();
    MapFragment mapFragment = new MapFragment();

    // TabLayout para el menu superior usado en la pantalla principal
    TabLayout tabLayout;

    // BottomNavigation para el menu inferior
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Al iniciar cargo el fragment de inicio
        loadFragment(homeFragment);

        // TabLayout: barra de navegacion superior para mostrar lugares por tipo (lugares historicos, restaurantes, etc.)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        loadFragment(homeFragment);     // debo crear un fragment para cada tipo de lugar y usarlos aca
                        break;
                    case 1:
                        loadFragment(placeFragment);
                        break;
                    case 2:
                        loadFragment(favoritesFragment);
                        break;
                    case 3:
                        loadFragment(mapFragment);
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
                case R.id.homeFragment:
                    loadFragment(homeFragment);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}
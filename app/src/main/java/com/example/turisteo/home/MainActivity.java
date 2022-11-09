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

    // Fragments
    HomeFragment homeFragment = new HomeFragment();
    PlaceFragment placeFragment = new PlaceFragment();
    FavoritesFragment favoritesFragment = new FavoritesFragment();
    MapFragment mapFragment = new MapFragment();
    ConfigFragment configFragment = new ConfigFragment();

    // TabLayout para el menu superior usado en la pantalla principal
    TabLayout tabLayout;

    // BottomNavigation para el menu inferior
    BottomNavigationView navigation;

    // Instancia de la Firestore (base de datos) en Firebase:
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ArrayList<Place> arrayList = new ArrayList<>();
    public Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Al iniciar cargo el fragment de inicio
        loadFragment(configFragment);

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
                case R.id.configFragment:
                    loadFragment(configFragment);
                    tabLayout.setVisibility(View.GONE);
                    return true;
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
        // al carga los fragment de tipos de lugares deberia recibir el array completo y filtrarlos por tipo para mostrar los que corresponden
        // para eso deberia agregar un campo en Firebase que sea "type" para cada lugar.
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}
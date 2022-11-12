package com.example.turisteo.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.turisteo.R;
import com.example.turisteo.config.ConfigFragment;
import com.example.turisteo.favorites.FavoritesFragment;
import com.example.turisteo.map.MapFragment;
import com.example.turisteo.place.PlaceInfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements IComunicacionFragments {

    FrameLayout frameContainer;

    // Fragments del bottom_navigation
    PlaceInfoFragment placeInfoFragment = new PlaceInfoFragment();
    FavoritesFragment favoritesFragment = new FavoritesFragment();
    MapFragment mapFragment = new MapFragment();
    ConfigFragment configFragment = new ConfigFragment();

    // Fragments del menu de navegacion superior en el Home, para filtrar por categoria de lugar
    HistoricalPlacesFragment historicalPlacesFragment = new HistoricalPlacesFragment();
    BeachPlacesFragment beachPlacesFragment = new BeachPlacesFragment();
    FoodPlacesFragment foodPlacesFragment = new FoodPlacesFragment();
    OthersPlacesFragment othersPlacesFragment = new OthersPlacesFragment();

    // BottomNavigation para el menu inferior
    public BottomNavigationView navigation;
    public BottomNavigationItemView bottom_item_config, bottom_item_home, bottom_item_place, bottom_item_favorites, bottom_item_map;

    // TabLayout para el menu superior usado en la pantalla principal
    public TabLayout tabLayout;

    // Bundle para pasar la informacion de los array a los fragments de categoria de lugares.
    // Esos array se llenan en ConfigFragment luego de hacer la consulta a la BD de Firebase y se pasar ahi mismo en este bundle.
    public Bundle bundle = new Bundle();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameContainer = findViewById(R.id.frame_container);
        tabLayout = findViewById(R.id.tabLayout);
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottom_item_config = findViewById(R.id.configFragment);
        bottom_item_home = findViewById(R.id.homeFragment);
        bottom_item_place = findViewById(R.id.placeFragment);
        bottom_item_favorites = findViewById(R.id.favoritesFragment);
        bottom_item_map = findViewById(R.id.mapFragment);

        // Al iniciar cargo el configFragment
        String goToFavorite = getIntent().getStringExtra("goToFavorite");   // esto lo puse para probar ir al favoritesFragment cuando elimino
        if(goToFavorite != null){                                                 // uno y asi tener la lista actualizada pero no sirve porque es como
            loadFragment(favoritesFragment);                                      // reiniciar y ya no aparen los datos de lugares en inicio
        }else{
            loadFragment(configFragment);
        }

        // TabLayout: menu de navegacion superior para mostrar lugares por tipo (lugares historicos, restaurantes, etc.)
        // Lo uso en MainActivity porque debo ir reemplazando el fragment y sino perdería esta barra de navegacion
        // o la deberia poner en todos los fragments que la usen y seria repetir codigo innecesario.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {      // se ejecuta cuando el usuario selecciona una pestaña
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
            public void onTabUnselected(TabLayout.Tab tab) {}   // se ejecuta cuando la pestaña sale del estado de selección

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}   // es ejecuta cuando el usuario selecciona una pestaña que actualmente se encuentra seleccionada
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
                    loadFragment(placeInfoFragment);
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
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    // Se sobreescribe este metodo porque es de la interface que se implementa
    public void sendPlace(Place place) {
        // Instanciamos el Fragment de informacion del lugar:
        placeInfoFragment = new PlaceInfoFragment();

        // Objeto a enviar:
        Bundle bundleInfo = new Bundle();
        bundleInfo.putSerializable("place", place);
        placeInfoFragment.setArguments(bundleInfo);

        // Cargamos el fragment en el activity:     (recordar usar replace y no add, porque sino aparece en la misma pantalla que el listado de lugares)
        // El addToBackStack(null) es porque si no lo tiene, al presionar la flecha hacia atrás se cerraba la app
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, placeInfoFragment)
                .addToBackStack(null).commit();
    }

    // Sobreescribo el metodo de presionar el boton hacia atras para que me seleccione como activo el botton de home en el bottom_navigation.
    // Lo hice porque al mostrar la info de un lugar uso un add (sendPlace) para que se puede volver hacia atras y el scroll en el listado
    // de lugares parmanezca en el mismo lugar donde estaba para que sea mejor la experiencia de usuario.
    // Si necesito que el boton de volver hacia atras funcione en otros fragment deberia ver como hago.
    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        bottom_item_config.setChecked(false);
        bottom_item_home.setChecked(true);
        bottom_item_place.setChecked(false);
        bottom_item_favorites.setChecked(false);
        bottom_item_map.setChecked(false);
        super.onBackPressed();
    }
}
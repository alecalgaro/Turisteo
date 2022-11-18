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
import com.example.turisteo.place.PlaceInfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements IComunicationFragments {

    FrameLayout frameContainer;

    // Fragments del bottom_navigation (el de inicio es el HistoricalPlacesFragment definido abajo)
    public PlaceInfoFragment placeInfoFragment = new PlaceInfoFragment();
    public FavoritesFragment favoritesFragment = new FavoritesFragment();
    public ConfigFragment configFragment = new ConfigFragment();

    // Fragments del menu de navegacion superior en el Home, para filtrar por categoria de lugar
    public HistoricalPlacesFragment historicalPlacesFragment = new HistoricalPlacesFragment();
    public BeachPlacesFragment beachPlacesFragment = new BeachPlacesFragment();
    public FoodPlacesFragment foodPlacesFragment = new FoodPlacesFragment();
    public OthersPlacesFragment othersPlacesFragment = new OthersPlacesFragment();

    // BottomNavigation para el menu inferior
    public BottomNavigationView navigation;
    public BottomNavigationItemView bottom_item_config, bottom_item_home, bottom_item_place, bottom_item_favorites, bottom_item_map;

    // TabLayout para el menu superior usado en la pantalla principal
    public TabLayout tabLayout;

    // Bundle para pasar la informacion de los array a los fragments de categoria de lugares.
    // Esos array se llenan en ConfigFragment luego de hacer la consulta a la BD de Firebase y se pasar ahi mismo en este bundle.
    public Bundle bundle = new Bundle();

    // Variables para usar en el metodo onBackPressed para el boton de volver atras
    public boolean backHome = false;
    public boolean backFavorites = false;
    public boolean inFavorites = false;

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
    // Se sobreescribe este metodo porque es de la interface que se implementa.
    // Si llamo a la funcion sendPlace desde un fragment de Home (de listado de lugares) pongo backHome en true y backFavorites
    // en false, y uso un "add" para que permanezca esa pantalla abierta en el lugar donde quedo para poder volver atras.
    // Y si la llamo desde FavoritesFragment lo contrario, para la funcionalidad del boton onBackPressed abajo y uso "add".
    public void sendPlace(Place place, String backFragment) {
        if(backFragment.equals("home")){
            backHome = true;
            backFavorites = false;
        }else{
            backFavorites = true;
            backHome = false;
        }

        // Instanciamos el Fragment de informacion del lugar:
        placeInfoFragment = new PlaceInfoFragment();

        // Objeto a enviar:
        Bundle bundleInfo = new Bundle();
        bundleInfo.putSerializable("place", place);
        placeInfoFragment.setArguments(bundleInfo);

        // Cargamos el fragment en el activity.
        // El addToBackStack(null) es porque si no lo tiene, al presionar la flecha hacia atrás se cerraba la app
        if(backFragment.equals("home")){
            getSupportFragmentManager().beginTransaction().add(R.id.frame_container, placeInfoFragment)
                    .addToBackStack(null).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, placeInfoFragment)
                    .addToBackStack(null).commit();
        }
    }

    // Sobreescribo el metodo de presionar el boton hacia atras, teniendo cuatro opciones de uso:
    // 1- Si desde el listado de lugares presiono uno para ir a PlaceInfoFragment y luego voy a favoritos en el bottom_navigation y quiero volver
    // hacia atras sin hacer nada en Favoritos, vuelvo directo a Home con el listado de lugares, simulando un click en homeFragment.
    // 2- Si desde un listado de lugares presiono uno y accedo a la pantalla de PlaceInfoFragment (en sendPlace uso backFragment = "home")
    // quiero que al presionar el boton de atras vuelva a la pantalla del listado de lugares para seguir mostrando desde donde se quedo
    // el usuario, por eso selecciono el home en el bottom_navigation y visible el tabLayout.
    // 3- Si estoy en FavoritesFragment y presiono uno de los favoritos, abro su PlaceInfoFragment y en sendPlace uso backFragment = "favorites",
    // asi al presionar el boton de volver hacia atras vuelve a cargar (loadFragment) la pantalla de favoritos para que se actualice la lista si
    // elimino un favorito. En sendPlace uso replace para que no quede la pantalla de favoritos en segundo plano porque si elimina un fav y vuelve
    // seguiria apareciendo ese fav que fue eliminado.
    // 4- Si estoy en otro Fragment, entra al else y no hago nada para dejar deshabilitado el boton de volver hacia atras.
    // (Hago esto porque al mostrar PlaceInfoFragment uso un "add" (en sendPlace), no replace, para que se puede volver hacia atras.
    @SuppressLint({"RestrictedApi", "ResourceType"})
    @Override
    public void onBackPressed() {
        if(backHome == true && inFavorites == true){
            super.onBackPressed();
            navigation.setSelectedItemId(R.id.homeFragment);
            bottom_item_home.setChecked(true);
            bottom_item_place.setChecked(false);
            backHome = false;
            inFavorites = false;
        }else if (backHome == true){
            super.onBackPressed();
            bottom_item_config.setChecked(false);
            bottom_item_home.setChecked(true);
            bottom_item_place.setChecked(false);
            bottom_item_favorites.setChecked(false);
            tabLayout.setVisibility(View.VISIBLE);
            backHome = false;
        } else if (backFavorites == true) {
            super.onBackPressed();
            loadFragment(favoritesFragment);
            backFavorites = false;
        }else{
            // no hago nada para que quede deshabilitado el boton de volver hacia atras
        }
    }
}
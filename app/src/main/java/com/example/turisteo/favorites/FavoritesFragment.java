package com.example.turisteo.favorites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.turisteo.R;
import com.example.turisteo.database_local.AdminLocalDBFavorites;
import com.example.turisteo.home.IComunicationFragments;
import com.example.turisteo.home.MainActivity;
import com.example.turisteo.home.Place;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    // Adapter y ListView
    private ListView lv_favorites;
    private AdapterFavorites adapterFavorites;

    AdminLocalDBFavorites adminLocalDBFavorites;

    // Activity que nos permite tener el contexto de nuestra aplicación:
    Activity activity;
    // Referencia a la interfaz que permite la comunicacion entre los dos fragments:
    IComunicationFragments interfaceComunicationFragments;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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

        // Seteo cual es el item del bottom_navigation que debe estar activo
        setChekedBottomItem();

        // Pongo en true "inFavorites" de MainActivity para el manejo del boton de volver hacia atras
        ((MainActivity)this.getActivity()).inFavorites = true;

        // Creo una instancia de la BD local
        adminLocalDBFavorites = new AdminLocalDBFavorites(getActivity().getApplicationContext(), "favorites_places", null, 1);

        // Inflate the layout for this fragment
        View viewFavorites = inflater.inflate(R.layout.fragment_favorites, container, false);

        // El arrayList lo creo aca, de forma local dentro del onCreateView porque si lo pongo de forma global, al seleccionar un lugar
        // y luego volver con el botón hacia atrás, la lista de lugares se vuelve a llenar y se duplican.
        ArrayList<Place> arrayList = new ArrayList<>();

        lv_favorites = (ListView) viewFavorites.findViewById(R.id.lv_favorites);

        adapterFavorites = new AdapterFavorites(getContext(), arrayList);
        lv_favorites.setAdapter(adapterFavorites);

        // Obtengo los favoritos de la BD local (se guardan en el arrayList y se muestran)
        adminLocalDBFavorites.getAllFavorites(arrayList);

        // Si se presiona sobre la card de un favorito abro su PlaceInfoFragment para ver la información del mismo:
        lv_favorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {    // Recordar que es setOnItemClick... no setOnClick...
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Envío el lugar seleccionado para abrir el otro fragment con su informacion:
                interfaceComunicationFragments.sendPlace(arrayList.get(lv_favorites.getPositionForView(view)), "favorites");
            }
        });

        return viewFavorites;       // para utilizar ese objeto viewPlaces dentro del activity
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Esto es necesario para establecer la conexión entre la lista de peliculas y la información de pelicula

        // Si el contexto que le esta llegando es una instancia de una activity:
        if (context instanceof Activity) {
            this.activity = (Activity) context;
            interfaceComunicationFragments = (IComunicationFragments) this.activity;
        }
    }

    @SuppressLint("RestrictedApi")
    public void setChekedBottomItem() {
        ((MainActivity)this.getActivity()).bottom_item_config.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_home.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_place.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_favorites.setChecked(true);
    }
}
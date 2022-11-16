package com.example.turisteo.home;

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
import android.widget.TextView;

import com.example.turisteo.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoricalPlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoricalPlacesFragment extends Fragment {

    TextView tv_warning;

    // Adapter y ListView
    private ListView lv_places;
    private AdapterPlaces adapterPlaces;

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

    public HistoricalPlacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoricalPlacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoricalPlacesFragment newInstance(String param1, String param2) {
        HistoricalPlacesFragment fragment = new HistoricalPlacesFragment();
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
        View viewPlaces = inflater.inflate(R.layout.list_places, container, false);

        tv_warning = viewPlaces.findViewById(R.id.tv_warning);

        // El arrayList lo creo aca, de forma local dentro del onCreateView porque si lo pongo de forma global, al seleccionar un lugar
        // y luego volver con el botón hacia atrás, la lista de lugares se vuelve a llenar y se duplican.
        ArrayList<Place> arrayList = new ArrayList<>();
        arrayList = (ArrayList<Place>)getArguments().getSerializable("arrayListHistoricalPlaces");

        // Si hay informacion para mostrar, quito el tv_warning y muestro el listado
        if(arrayList != null){
            tv_warning.setVisibility(View.GONE);

            // Seteo el item del bottom_navigation y el tab del menu superior que deben estar activos
            setChekedBottomItem();
            setTabActive();

            lv_places = (ListView) viewPlaces.findViewById(R.id.lv_places);

            adapterPlaces = new AdapterPlaces(getContext(), arrayList);
            lv_places.setAdapter(adapterPlaces);

            // Si se presiona sobre la card de un lugar abro su descripción en otro fragment:
            ArrayList<Place> finalArrayList = arrayList;
            lv_places.setOnItemClickListener(new AdapterView.OnItemClickListener() {    // Recordar que es setOnItemClick... no setOnClick...
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Envío el lugar seleccionado para abrir el otro fragment con su informacion:
                    interfaceComunicationFragments.sendPlace(finalArrayList.get(lv_places.getPositionForView(view)));
                }
            });
        }

        return viewPlaces;       // para utilizar ese objeto viewPlaces dentro del activity
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

    // Activo el item correspondiente en el bottom_navigation
    @SuppressLint("RestrictedApi")
    public void setChekedBottomItem() {
        // Existe ((MainActivity)this.getActivity()).navigation.setSelectedItemId(R.id.bottom_item_home);
        // que queda mucho mejor con esa sola linea pero es como simular que se presiona el boton y algunas veces me da problema
        ((MainActivity)this.getActivity()).bottom_item_config.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_home.setChecked(true);
        ((MainActivity)this.getActivity()).bottom_item_place.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_favorites.setChecked(false);
    }

    // Activo el tab correspondiente en el menu superior (porque al ir a otra pantalla y volver necesito activar el que corresponde)
    public void setTabActive(){
        TabLayout tabLayout = ((MainActivity)this.getActivity()).tabLayout;
        tabLayout.selectTab(tabLayout.getTabAt(0));
    }

}
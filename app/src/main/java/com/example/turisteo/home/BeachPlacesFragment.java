package com.example.turisteo.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.turisteo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BeachPlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeachPlacesFragment extends Fragment {

    // Adapter y ListView
    private ListView lv_places;
    private AdapterPlaces adapterPlaces;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BeachPlacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BeachPlacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BeachPlacesFragment newInstance(String param1, String param2) {
        BeachPlacesFragment fragment = new BeachPlacesFragment();
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

        // El arrayList lo creo aca, de forma local dentro del onCreateView porque si lo pongo de forma global, al seleccionar un lugar
        // y luego volver con el botón hacia atrás, la lista de lugares se vuelve a llenar y se duplican.
        ArrayList<Place> arrayList = new ArrayList<>();
        arrayList = (ArrayList<Place>)getArguments().getSerializable("arrayListBeachPlaces");

        lv_places = (ListView) viewPlaces.findViewById(R.id.lv_places);

        adapterPlaces = new AdapterPlaces(getContext(), arrayList);
        lv_places.setAdapter(adapterPlaces);

        return viewPlaces;       // para utilizar ese objeto viewPlaces dentro del activity
    }
}
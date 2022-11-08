package com.example.turisteo.place;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.turisteo.R;
import com.example.turisteo.database.AdminLocalDB;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceFragment extends Fragment {

    ImageView icon_fav;
    AdminLocalDB adminLocalDB;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceFragment newInstance(String param1, String param2) {
        PlaceFragment fragment = new PlaceFragment();
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

        // Creo una instancia de la BD local
        adminLocalDB = new AdminLocalDB(getActivity().getApplicationContext(), "favorites_places", null, 1);

        // Inflate the layout for this fragment
        View viewPlace = inflater.inflate(R.layout.fragment_place, container, false);

        icon_fav = viewPlace.findViewById(R.id.icon_fav);

        // Añadir lugar a favoritos
        icon_fav.setOnClickListener(v -> {
            icon_fav.setImageResource(R.drawable.ic_fav);

            // Se añade el lugar a la BD local como favorito
            adminLocalDB.insertFavorite("https://res.cloudinary.com/alecalgaro/image/upload/v1666051944/Portfolio%20-%20Alejandro%20Calgaro/portfolio_ikf9jo.webp", "Nombre del lugar");
        });



        return viewPlace;
    }
}
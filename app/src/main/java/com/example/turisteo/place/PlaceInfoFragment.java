package com.example.turisteo.place;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turisteo.R;
import com.example.turisteo.database_local.AdminLocalDB;
import com.example.turisteo.home.MainActivity;
import com.example.turisteo.home.Place;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceInfoFragment extends Fragment {

    AdminLocalDB adminLocalDB;

    TextView tv_title, tv_rating, tv_description, tv_dir, tv_phone, tv_web;
    ImageView image1, image2, image3;
    RatingBar ratingBar;
    Button btn_map, btn_addFavorite;

    String latitude, longitude;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaceInfoFragment() {
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
    public static PlaceInfoFragment newInstance(String param1, String param2) {
        PlaceInfoFragment fragment = new PlaceInfoFragment();
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

        // Creo una instancia de la BD local
        adminLocalDB = new AdminLocalDB(getActivity().getApplicationContext(), "favorites_places", null, 1);

        // Inflate the layout for this fragment
        View viewPlace = inflater.inflate(R.layout.fragment_place, container, false);

        tv_title = viewPlace.findViewById(R.id.tv_title);
        tv_rating = viewPlace.findViewById(R.id.tv_rating);
        tv_description = viewPlace.findViewById(R.id.tv_description);
        tv_dir = viewPlace.findViewById(R.id.tv_dir);
        tv_phone = viewPlace.findViewById(R.id.tv_phone);
        tv_web = viewPlace.findViewById(R.id.tv_web);
        image1 = viewPlace.findViewById(R.id.image1);
        image2 = viewPlace.findViewById(R.id.image2);
        image3 = viewPlace.findViewById(R.id.image3);
        ratingBar = viewPlace.findViewById(R.id.ratingBar);
        btn_addFavorite = viewPlace.findViewById(R.id.btn_addFavorite);

        // Bundle para recibir el objeto enviado:
        Bundle placeReceived = getArguments();
        Place place = null;

        if(placeReceived != null) {   // quiere decir que tenemos argumentos para mostrar
            place = (Place) placeReceived.getSerializable("place");    // lo envie desde MainActivity con el key "place"
            loadInfo(place);
        }

        // Añadir lugar a favoritos
        btn_addFavorite.setOnClickListener(v -> {
            // Se añade el lugar a la BD local como favorito
            adminLocalDB.insertFavorite("https://res.cloudinary.com/alecalgaro/image/upload/v1666051944/Portfolio%20-%20Alejandro%20Calgaro/portfolio_ikf9jo.webp", "Nombre del lugar");
            Toast.makeText(getContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show();
        });

        // Boton para ver el mapa
        //btn_map.setOnClickListener(v -> {

        //});

        return viewPlace;
    }

    // Se carga toda la informacion del lugar en el diseño
    public void loadInfo(Place place){
        tv_title.setText(place.getName());
        tv_rating.setText(place.getStars());
        tv_description.setText(place.getDescription_long());
        tv_dir.setText(place.getDirection());
        tv_phone.setText(place.getPhone());
        tv_web.setText(place.getWeb());
        Picasso.get().load(place.getUrlImage1()).into(image1);
        Picasso.get().load(place.getUrlImage2()).into(image2);
        Picasso.get().load(place.getUrlImage3()).into(image3);
        latitude = place.getLatitude();
        longitude = place.getLongitude();
        ratingBar.setRating(Float.valueOf(place.getStars()));
    }

    @SuppressLint("RestrictedApi")
    public void setChekedBottomItem() {
        ((MainActivity)this.getActivity()).bottom_item_config.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_home.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_place.setChecked(true);
        ((MainActivity)this.getActivity()).bottom_item_favorites.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_map.setChecked(false);
    }

}
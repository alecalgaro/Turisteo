package com.example.turisteo.config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turisteo.R;
import com.example.turisteo.firebase.DBFirestore;
import com.example.turisteo.home.HistoricalPlacesFragment;
import com.example.turisteo.home.MainActivity;
import com.example.turisteo.home.Place;
import com.example.turisteo.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {

    // Select de ciudades:
    String[] items = {"Chajari, Entre Ríos"};
    //String[] items = {"Chajari, Entre Ríos", "Santa Fe, Santa Fe", "Cordoba, Cordoba"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    DBFirestore dbFirestore = new DBFirestore();
    String path;

    ProgressBar progressBar;
    TextView tv_progressBar;
    Button btn_logout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        ConfigFragment fragment = new ConfigFragment();
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

        // Inflate the layout for this fragment
        View viewConfig = inflater.inflate(R.layout.fragment_config, container, false);

        progressBar = viewConfig.findViewById(R.id.progressBar);
        tv_progressBar = viewConfig.findViewById(R.id.tv_progressBar);
        btn_logout = viewConfig.findViewById(R.id.btn_logout);

        autoCompleteTextView = viewConfig.findViewById(R.id.autoCompleteTextView);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.item_select_city, items);
        autoCompleteTextView.setAdapter(adapterItems);

        // Click sobre una ciudad de la lista
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                progressBar.setVisibility(View.VISIBLE);
                tv_progressBar.setVisibility(View.VISIBLE);

                switch (position){
                    case 0: path = "places_chajari"; break;
                    // si se agregan mas ciudades en el listado se irian actualizando los casos aca para pasar el path
                    // correspondiente para la consulta a la BD
                }
                dbFirestore.getDataFirestore(path);     // hago la consulta a la base de datos

                // Espero cuatro segundos para que se realice la consulta y cargo los datos
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(dbFirestore.result == true){     // si la consulta fue existosa
                            progressBar.setVisibility(View.INVISIBLE);
                            tv_progressBar.setVisibility(View.INVISIBLE);
                            loadData();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            tv_progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "Error al conectar con la base de datos. Intente nuevamente.", Toast.LENGTH_LONG).show();
                        }
                    }}, 4000);
            }
        });

        // Boton para cerrar sesion
        btn_logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            ((MainActivity)this.getActivity()).finish();
        });

        return viewConfig;
    }

    // Accedo al bundle del MainActivity (porque es padre de este fragment y asi se puede acceder a metodos u objetos
    // del activity padre) y le agrego el putSerializable para enviar todos los array con lugares y en cada Fragment recibo el que corresponde.
    public void loadData(){
        ((MainActivity)this.getActivity()).bundle.putSerializable("arrayListHistoricalPlaces", dbFirestore.arrayListHistoricalPlaces);
        ((MainActivity)this.getActivity()).bundle.putSerializable("arrayListBeachPlaces", dbFirestore.arrayListBeachPlaces);
        ((MainActivity)this.getActivity()).bundle.putSerializable("arrayListFoodPlaces", dbFirestore.arrayListFoodPlaces);
        ((MainActivity)this.getActivity()).bundle.putSerializable("arrayListOthersPlaces", dbFirestore.arrayListOthersPlaces);
        HistoricalPlacesFragment historicalPlacesFragment = new HistoricalPlacesFragment();
        ((MainActivity)this.getActivity()).loadFragment(historicalPlacesFragment);
        ((MainActivity)this.getActivity()).tabLayout.setVisibility(View.VISIBLE);
        // Luego de hacer click en la ciudad y cargar al arrayList con los lugares, suele demorar unos segundos en recibir la info desde Firebase,
        // entonces le puse un progressBar para indicar que se estan cargando los datos y una vez que estan listos desaparece, asi cuando
        // el usuario vaya al HomeFragment desde el bottom_navigation ya podra ver todos los lugares cargados.
    }

    @SuppressLint("RestrictedApi")
    public void setChekedBottomItem() {
        ((MainActivity)this.getActivity()).bottom_item_config.setChecked(true);
        ((MainActivity)this.getActivity()).bottom_item_home.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_place.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_favorites.setChecked(false);
    }

}
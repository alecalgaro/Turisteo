package com.example.turisteo.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.turisteo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // Adapter y ListView
    private ListView lv_places;
    private AdapterPlaces adapterPlaces;

    // Instancia de la Firestore (base de datos) en Firebase:
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View viewPlaces = inflater.inflate(R.layout.fragment_home, container, false);

        // El arrayList lo creo aca, de forma local dentro del onCreateView porque si lo pongo de forma global, al seleccionar un lugar
        // y luego volver con el botón hacia atrás, la lista de lugares se vuelve a llenar y se duplican.
        ArrayList<Place> arrayList = new ArrayList<>();
        arrayList = (ArrayList<Place>)getArguments().getSerializable("arrayListPlaces");

        lv_places = (ListView) viewPlaces.findViewById(R.id.lv_places);

        adapterPlaces = new AdapterPlaces(getContext(), arrayList);
        lv_places.setAdapter(adapterPlaces);

        //String title = "Nombre del lugar";
        //String descripcion = "Descripción breve del lugar";

        //arrayList.add(new Place("1", R.drawable.img_card_place, title, descripcion));
        //arrayList.add(new Place("2", R.drawable.img_card_place, title, descripcion));
        //arrayList.add(new Place("3", R.drawable.img_card_place, title, descripcion));

        return viewPlaces;       // para utilizar ese objeto viewPlaces dentro del activity

    }

}
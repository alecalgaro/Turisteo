package com.example.turisteo.place;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turisteo.R;
import com.example.turisteo.database_local.AdminLocalDBFavorites;
import com.example.turisteo.database_local.AdminLocalDBRatings;
import com.example.turisteo.firebase.DBFirestore;
import com.example.turisteo.home.MainActivity;
import com.example.turisteo.home.Place;
import com.example.turisteo.map.MapActivity;
import com.squareup.picasso.Picasso;

import static android.Manifest.permission.CALL_PHONE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceInfoFragment extends Fragment {

    LinearLayout warning;

    AdminLocalDBFavorites adminLocalDBFavorites;
    AdminLocalDBRatings adminLocalDBRatings;

    DBFirestore dbFirestore = new DBFirestore();

    public Place place = null;

    TextView tv_title, tv_rating, tv_description, tv_dir, tv_phone, tv_web;
    ImageView image1, image2, image3;
    RatingBar ratingBar;
    Button btn_map, btn_addFavorite;
    public Float stars_count, stars_prom;
    public int number_reviews;
    String latitude, longitude;
    String id_document, collection, currentRating;

    public boolean newFavorite = true;

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

        // Creo una instancia de la BD local para cada tabla (favoritos y calificaciones)
        adminLocalDBFavorites = new AdminLocalDBFavorites(getActivity().getApplicationContext(), "favorites_places", null, 1);
        adminLocalDBRatings = new AdminLocalDBRatings(getActivity().getApplicationContext(), "ratings", null, 1);

        // Inflate the layout for this fragment
        View viewPlace = inflater.inflate(R.layout.fragment_place_info, container, false);

        warning = viewPlace.findViewById(R.id.warning);
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
        btn_map = viewPlace.findViewById(R.id.btn_map);

        // Bundle para recibir el objeto enviado:
        Bundle placeReceived = getArguments();

        if(placeReceived != null) {   // quiere decir que tenemos argumentos para mostrar
            place = (Place) placeReceived.getSerializable("place");    // lo envie desde MainActivity con el key "place"

            if(place != null){      // si se recibio informacion con la clave "place"
                warning.setVisibility(View.GONE);

                // Cargo la informacion del lugar
                loadInfo(place);
                // Cargo la calificacion actual del lugar
                loadCurrentRating();

                // Seteo el boton correspondiente dependiendo de si el lugar esta o no agregado a los favoritos de la BD local
                if(adminLocalDBFavorites.getFavorite(id_document)){
                    btn_addFavorite.setText("Eliminar de favoritos");
                    newFavorite = false;
                }else{
                    btn_addFavorite.setText("Añadir a favoritos");
                    newFavorite = true;
                }

                // Agregar o quitar lugar de favoritos
                Place finalPlace = place;   // al usar la linea para insertar en la BD me pide agregar esto
                btn_addFavorite.setOnClickListener(v -> {
                    // Si newFavorite = true se añade el lugar a la BD local como favorito
                    if(newFavorite){
                        adminLocalDBFavorites.insertFavorite(
                                finalPlace.getCollection(), finalPlace.getId(), finalPlace.getCategory(), finalPlace.getName(), finalPlace.getDescription_short(),
                                finalPlace.getDescription_long(), finalPlace.getUrlImage1(), finalPlace.getUrlImage2(), finalPlace.getUrlImage3(),
                                finalPlace.getDirection(), finalPlace.getPhone(), finalPlace.getWeb(), finalPlace.getLatitude(),
                                finalPlace.getLongitude(), finalPlace.getStarsCount(), finalPlace.getStarsProm(), finalPlace.getNumber_reviews()
                        );
                        btn_addFavorite.setText("Eliminar de favoritos");
                        newFavorite = false;
                        Toast.makeText(getContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }else{  // sino quiere decir que ya fue agregado asi que lo elimino de favoritos
                        adminLocalDBFavorites.deleteFavorite(id_document);
                        btn_addFavorite.setText("Añadir a favoritos");
                        newFavorite = true;
                        Toast.makeText(getContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }
                });

                // Intent para abrir pagina web o red social del lugar al presionar sobre el TextView
                Place finalPlace1 = place;
                tv_web.setOnClickListener(v -> {
                    if(!finalPlace1.getWeb().equals("-")){      // debe ser una url valida (con http://... o https://...)
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(finalPlace1.getWeb()));
                        startActivity(intent);
                    }
                });

                // Si se presiona sobre el numero de telefono del lugar, se solicita permiso para realizar llamadas y si el usuario lo acepta se
                // crea y ejecuta un intent para realizar la llamada, sino se le indica que debe aceptar el permiso antes de realizar una llamada
                tv_phone.setOnClickListener(v -> {
                    if(!tv_phone.getText().equals("-")){
                        if (ContextCompat.checkSelfPermission(getContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(android.content.Intent.ACTION_CALL,
                                    Uri.parse("tel:"+tv_phone.getText()));
                            startActivity(intent);
                        } else {
                            requestPermissions(new String[]{CALL_PHONE}, 1);
                            Toast.makeText(getContext(), "Debes aceptar el permiso para poder realizar llamadas desde la app", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // Boton para mostrar el lugar elegido en el mapa
                btn_map.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Recuerda activar la ubicación del dispositivo", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("name", tv_title.getText());
                    startActivity(intent);
                });
            }
        }

        // Control de calificaciones. Con setOnRatingBarChangeListener se detecta cuando hay un cambio en el ratingBar.
        // Debo considerar dos opciones, si el usuario califica por primera vez el lugar hago la actualizacion en Firebase
        // e inserto esa calificacion en la BD local. Mientras que si el usuario ya califico anteriormente el lugar y quiere
        // modificar su calificacion, debo tomar su calificacion actual y cambiarla por la nueva, no sumarla como si es una distinta.

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {

                if(currentRating == null){      // primera vez que el usuario califica el lugar
                    number_reviews = number_reviews + 1;
                    stars_count = stars_count + rating;     // stars_count es la suma de todas las calificaciones (guardada en Firestore)
                    adminLocalDBRatings.insertRating(id_document, String.valueOf(rating));      // inserto en la tabla de ratings de la BD local
                } else {
                    // Si el usuario ya califico antes el lugar debo actualizar su calificacion, no sumar una nueva, entonces
                    // a la cantidad de estrellas le resto la calificacion actual del usuario, le sumo la nueva y divido por el numero de
                    // calificaciones que ya habia porque no es una nueva sino una actualizacion, asi que no modifico el number_reviews.
                    stars_count = stars_count - Float.parseFloat(currentRating) + rating;
                    adminLocalDBRatings.updateRating(id_document, String.valueOf(rating));      // actualizo la tabla de ratings de la BD local
                }

                stars_prom = stars_count/number_reviews;    // stars_prom es el promedio (todas las calificaciones dividido la cantidad)
                currentRating = String.valueOf(rating);     // actualizo la calificacion actual del usuario por si vuelve a actualizarla en el momento
                // Actualizo en Firebase
                dbFirestore.updateValoration(collection, id_document, Float.toString(stars_count), Float.toString(stars_prom), Integer.toString(number_reviews));

                // Espero dos segundos por las dudas para que se realice la actualizacion y luego muestro si fue exitosa o no y actualizo el TextView.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(dbFirestore.result_update == true){     // si la consulta fue existosa
                            Toast.makeText(getContext(), "Calificación actualizada.", Toast.LENGTH_LONG).show();
                            tv_rating.setText(String.valueOf(stars_prom).substring(0, 3));      // actualizo el TextView
                        }else{
                            Toast.makeText(getContext(), "Hubo un error al actualizar la calificación.", Toast.LENGTH_LONG).show();
                        }
                    }}, 2000);
            }
        });

        return viewPlace;
    }

    // Se carga toda la informacion del lugar en el diseño
    public void loadInfo(Place place){
        collection = place.getCollection();
        id_document = place.getId();
        tv_title.setText(place.getName());
        tv_rating.setText(place.getStarsProm().substring(0, 3));    // muestro tres caracteres (como 4.5), por si da un numero periodico
        tv_description.setText(place.getDescription_long());
        tv_dir.setText(place.getDirection());
        tv_phone.setText(place.getPhone());
        tv_web.setText(place.getWeb());
        Picasso.get().load(place.getUrlImage1()).into(image1);
        Picasso.get().load(place.getUrlImage2()).into(image2);
        Picasso.get().load(place.getUrlImage3()).into(image3);
        latitude = place.getLatitude();
        longitude = place.getLongitude();
        stars_count = Float.parseFloat(place.getStarsCount());
        stars_prom = Float.parseFloat(place.getStarsProm());
        number_reviews = Integer.valueOf(place.getNumber_reviews());
        // Consulto en la BD local si el usuario ya hizo una calificacion del lugar:
        currentRating = adminLocalDBRatings.getRating(id_document);
        if(currentRating != null){ ratingBar.setRating(Float.parseFloat(currentRating)); }
    }

    // Metodo para consulta la calificacion actual del lugar y cargarla en el TextView
    public void loadCurrentRating(){
        // Espero cuatro segundos para que se realice la consulta del stars_prom y cargo la calificacion actual del lugar traida desde
        // Firebase. Esto lo hago porque cuando el usuario realiza una calificacion se actualiza el tv_rating, pero si vuelvo hacia atras y
        // luego entro nuevamente a ese lugar la calificacion no esta actualizada porque los datos de Firestore los obtengo en ConfigFragment
        dbFirestore.getRatingPlace(place.getCollection(), place.getId());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!dbFirestore.currentStarsProm.equals("")){     // si se realizo la consulta y ya actualizo el valor de currentStarsProm
                    tv_rating.setText(dbFirestore.currentStarsProm.substring(0, 3));
                }
            }}, 4000);
    }

    // Metodo para setear el boton correspondient en el bottom_navigation
    @SuppressLint("RestrictedApi")
    public void setChekedBottomItem() {
        ((MainActivity)this.getActivity()).bottom_item_config.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_home.setChecked(false);
        ((MainActivity)this.getActivity()).bottom_item_place.setChecked(true);
        ((MainActivity)this.getActivity()).bottom_item_favorites.setChecked(false);
    }
}
package com.example.turisteo.firebase;

import androidx.annotation.NonNull;

import com.example.turisteo.home.Place;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DBFirestore {

    // Instancia de Firestore (base de datos) en Firebase:
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Arrays para guardar los datos obtenidos:
    public ArrayList<Place> arrayListHistoricalPlaces = new ArrayList<>();
    public ArrayList<Place> arrayListBeachPlaces = new ArrayList<>();
    public ArrayList<Place> arrayListFoodPlaces = new ArrayList<>();
    public ArrayList<Place> arrayListOthersPlaces = new ArrayList<>();

    public boolean result = false;

    public void getDataFirestore(String path){
        // Leo todos los documentos de la base de datos en Firebase que correspondan a la coleccion de la ciudad elegida (recibida en el path):
        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {      // si la consulta fue exitosa
                            result = true;
                            arrayListHistoricalPlaces.clear();      // limpio el array antes de agregar
                            arrayListBeachPlaces.clear();
                            arrayListFoodPlaces.clear();
                            arrayListOthersPlaces.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Segun la categoria del lugar lo agrego en el array correspondiente
                                switch (document.getString("category")){
                                    case "historical":
                                        arrayListHistoricalPlaces.add(
                                                new Place(document.getId(), document.getString("category"),
                                                        document.getString("name"), document.getString("description_short"),
                                                        document.getString("description_long"), document.getString("url_image1"),
                                                        document.getString("url_image2"), document.getString("url_image3"),
                                                        document.getString("direction"), document.getString("phone"), document.getString("web"),
                                                        document.getString("latitude"), document.getString("longitude"),
                                                        document.getString("stars"), document.getString("number_reviews")
                                                ));
                                        break;
                                    case "beach":
                                        arrayListBeachPlaces.add(
                                                new Place(document.getId(), document.getString("category"),
                                                        document.getString("name"), document.getString("description_short"),
                                                        document.getString("description_long"), document.getString("url_image1"),
                                                        document.getString("url_image2"), document.getString("url_image3"),
                                                        document.getString("direction"), document.getString("phone"), document.getString("web"),
                                                        document.getString("latitude"), document.getString("longitude"),
                                                        document.getString("stars"), document.getString("number_reviews")
                                                ));
                                        break;
                                    case "food":
                                        arrayListFoodPlaces.add(
                                                new Place(document.getId(), document.getString("category"),
                                                        document.getString("name"), document.getString("description_short"),
                                                        document.getString("description_long"), document.getString("url_image1"),
                                                        document.getString("url_image2"), document.getString("url_image3"),
                                                        document.getString("direction"), document.getString("phone"), document.getString("web"),
                                                        document.getString("latitude"), document.getString("longitude"),
                                                        document.getString("stars"), document.getString("number_reviews")
                                                ));
                                        break;
                                    case "others":
                                        arrayListOthersPlaces.add(
                                                new Place(document.getId(), document.getString("category"),
                                                        document.getString("name"), document.getString("description_short"),
                                                        document.getString("description_long"), document.getString("url_image1"),
                                                        document.getString("url_image2"), document.getString("url_image3"),
                                                        document.getString("direction"), document.getString("phone"), document.getString("web"),
                                                        document.getString("latitude"), document.getString("longitude"),
                                                        document.getString("stars"), document.getString("number_reviews")
                                                ));
                                        break;
                                }
                            }
                        }
                    }
                });
    }

}

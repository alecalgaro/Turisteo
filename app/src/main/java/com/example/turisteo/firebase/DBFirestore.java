package com.example.turisteo.firebase;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.turisteo.home.Place;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBFirestore {

    // Instancia de Firestore (base de datos) en Firebase:
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Arrays para guardar los datos obtenidos:
    public ArrayList<Place> arrayListHistoricalPlaces = new ArrayList<>();
    public ArrayList<Place> arrayListBeachPlaces = new ArrayList<>();
    public ArrayList<Place> arrayListFoodPlaces = new ArrayList<>();
    public ArrayList<Place> arrayListOthersPlaces = new ArrayList<>();

    public String collection_db;

    public String currentStarsProm = "";
    public String currentStarsCount = "";
    public String currentNumberReviews = "";

    public boolean result = false;
    public boolean result_update = false;

    // Metodo para obtener la informacion de todos los lugares de la ciudad elegida (coleccion)
    public void getDataFirestore(String collection){
        // Leo todos los documentos de la base de datos en Firebase que correspondan a la coleccion de la ciudad elegida (recibida en el path):
        collection_db = collection;      // para poder pasarla a cada Place
        db.collection(collection)
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
                                                new Place(collection_db, document.getId(), document.getString("category"),
                                                        document.getString("name"), document.getString("description_short"),
                                                        document.getString("description_long"), document.getString("url_image1"),
                                                        document.getString("url_image2"), document.getString("url_image3"),
                                                        document.getString("direction"), document.getString("phone"), document.getString("web"),
                                                        document.getString("latitude"), document.getString("longitude"),
                                                        document.getString("stars_count"), document.getString("stars_prom"), document.getString("number_reviews")
                                                ));
                                        break;
                                    case "beach":
                                        arrayListBeachPlaces.add(
                                                new Place(collection_db, document.getId(), document.getString("category"),
                                                        document.getString("name"), document.getString("description_short"),
                                                        document.getString("description_long"), document.getString("url_image1"),
                                                        document.getString("url_image2"), document.getString("url_image3"),
                                                        document.getString("direction"), document.getString("phone"), document.getString("web"),
                                                        document.getString("latitude"), document.getString("longitude"),
                                                        document.getString("stars_count"), document.getString("stars_prom"), document.getString("number_reviews")
                                                ));
                                        break;
                                    case "food":
                                        arrayListFoodPlaces.add(
                                                new Place(collection_db, document.getId(), document.getString("category"),
                                                        document.getString("name"), document.getString("description_short"),
                                                        document.getString("description_long"), document.getString("url_image1"),
                                                        document.getString("url_image2"), document.getString("url_image3"),
                                                        document.getString("direction"), document.getString("phone"), document.getString("web"),
                                                        document.getString("latitude"), document.getString("longitude"),
                                                        document.getString("stars_count"), document.getString("stars_prom"), document.getString("number_reviews")
                                                ));
                                        break;
                                    case "others":
                                        arrayListOthersPlaces.add(
                                                new Place(collection_db, document.getId(), document.getString("category"),
                                                        document.getString("name"), document.getString("description_short"),
                                                        document.getString("description_long"), document.getString("url_image1"),
                                                        document.getString("url_image2"), document.getString("url_image3"),
                                                        document.getString("direction"), document.getString("phone"), document.getString("web"),
                                                        document.getString("latitude"), document.getString("longitude"),
                                                        document.getString("stars_count"), document.getString("stars_prom"), document.getString("number_reviews")
                                                ));
                                        break;
                                }
                            }
                        }
                    }
                });
    }

    // Metodo para actualizar la calificacion de un lugar (stars_count, starts_prom y number_reviews)
    public void updateValoration(String collection, String id_document, String stars_count, String stars_prom, String number_reviews){
        // En un HashMap se van agregando (put) los campos a actualizar (clave, valor)
        Map<String, Object> map = new HashMap<>();
        map.put("stars_count", stars_count);
        map.put("stars_prom", stars_prom);
        map.put("number_reviews", number_reviews);

        // Accedo a la coleccion y documento de ese lugar y actualizo.
        // Con result_update puedo saber desde un activity si la actualizacion fue exitosa o no.
        db.collection(collection).document(id_document)
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                result_update = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result_update = false;
            }
        });
    }

    // Metodo para obtener la calificacion actual de un lugar elegido
    // (actualiza las variables stars_count, stars_prom y number_reviews)
    public void getRatingPlace(String collection, String id_document){
        DocumentReference docRef = db.collection(collection).document(id_document);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        currentStarsCount = document.getString("stars_count");
                        currentStarsProm = document.getString("stars_prom");
                        currentNumberReviews = document.getString("number_reviews");
                    }
                }
            }
        });
    }

}

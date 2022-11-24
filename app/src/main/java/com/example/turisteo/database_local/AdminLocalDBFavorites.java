package com.example.turisteo.database_local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.turisteo.home.Place;

import java.util.ArrayList;

public class AdminLocalDBFavorites extends SQLiteOpenHelper {

    // En esta clase pongo lo relacionado a la BD y la voy utilizando en las pantallas donde necesito

    private static final int db_version = 1;
    private static final String db_nombre = "favorites_places";

    // Un lugar que se agrega como favorito tendra todos los datos de ese lugar, asi el usuario desde la pantalla de favoritos
    // puede presionar sobre la card de uno y que se abra el PlaceInfoFragment con la informacion del lugar.

    // Sentencia para la creación de la tabla Favorites:
    String sqlCreate = "CREATE TABLE Favorites (collection TEXT, id TEXT, category TEXT, name TEXT, description_short TEXT, " +
            "description_long TEXT, url_image1 TEXT, url_image2 TEXT, url_image3 TEXT, direction TEXT, phone TEXT, web TEXT," +
            "latitude TEXT, longitude TEXT, stars_count TEXT, stars_prom TEXT, number_reviews TEXT)";

    // Constructor:
    public AdminLocalDBFavorites(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_nombre, factory, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se ejecuta la sentencia SQL de creacion de la tabla:
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se elimina la versión anterior de la tabla:
        db.execSQL("DROP TABLE IF EXISTS Favorites");
        // Se crea la nueva version de la tabla:
        db.execSQL(sqlCreate);     // también se podria hacer llamando de nuevo a onCreate(db);
    }

    // Obtener si existe un favorito en la BD con un cierto id
    // Se usa para saber si el lugar ya fue o no agregado a favoritos para mostrar el boton para agregar o quitar de favoritos
    public boolean getFavorite(String id_fav){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = new String[] {id_fav};
        Cursor c = db.rawQuery(" SELECT id FROM Favorites WHERE id =?", args);

        // Si existe un favorito con ese id retorno true, sino false
        if (c.moveToFirst()) {
            return true;
        } else{
            return false;
        }
    }

    // Listar todos los favoritos de la BD:
    public void getAllFavorites(ArrayList<Place> arrayList) {       // uso un array de datos para añadirlos al ListView
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT collection, id, category, name, description_short, description_long," +
                "url_image1, url_image2, url_image3, direction, phone, web, latitude, longitude," +
                "stars_count, stars_prom, number_reviews " +
                "FROM Favorites", null);

        // Me aseguro que existe al menos un registro:
        if (c.moveToFirst()) {
            // Reocrro el cursor hasta que no haya mas registros (c.moveToNext()):
            do {
                // Voy obteniendo los datos:
                String collection = c.getString(0);
                String id = c.getString(1);
                String category = c.getString(2);
                String name = c.getString(3);
                String description_short = c.getString(4);
                String description_long = c.getString(5);
                String url_image1 = c.getString(6);
                String url_image2 = c.getString(7);
                String url_image3 = c.getString(8);
                String direction = c.getString(9);
                String phone = c.getString(10);
                String web = c.getString(11);
                String latitude = c.getString(12);
                String longitude = c.getString(13);
                String stars_count = c.getString(14);
                String stars_prom = c.getString(15);
                String number_reviews = c.getString(16);

                // Los voy agregando en el arrayList (ese arrayList luego se usa para mostrar los elementos en el ListView):
                arrayList.add(new Place(collection, id, category, name, description_short, description_long,
                        url_image1, url_image2, url_image3, direction, phone, web,
                        latitude, longitude, stars_count, stars_prom, number_reviews));

            } while (c.moveToNext());
        }
        db.close();
    }

    // Insertar nuevo lugar favorito en la BD:
    public void insertFavorite(String collection, String id, String category, String name, String description_short, String description_long,
                               String url_image1, String url_image2, String url_image3, String direction, String phone, String web,
                               String latitude, String longitude, String stars_count, String stars_prom, String number_reviews)  {

        // Se crea el nuevo favorito a insertar como objeto ContentValues:
        ContentValues newFavorite = new ContentValues();
        newFavorite.put("collection", collection);
        newFavorite.put("id", id);
        newFavorite.put("category", category);
        newFavorite.put("name", name);
        newFavorite.put("description_short", description_short);
        newFavorite.put("description_long", description_long);
        newFavorite.put("url_image1", url_image1);
        newFavorite.put("url_image2", url_image2);
        newFavorite.put("url_image3", url_image3);
        newFavorite.put("direction", direction);
        newFavorite.put("phone", phone);
        newFavorite.put("web", web);
        newFavorite.put("latitude", latitude);
        newFavorite.put("longitude", longitude);
        newFavorite.put("stars_count", stars_count);
        newFavorite.put("stars_prom", stars_prom);
        newFavorite.put("number_reviews", number_reviews);

        // Se inserta en la base de datos:
        SQLiteDatabase db = getWritableDatabase();
        db.insert("Favorites", null, newFavorite);
        db.close();
    }

    // Eliminar favorito de la BD:
    public void deleteFavorite(String id_fav) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = new String[] {id_fav};
        db.execSQL("DELETE FROM Favorites WHERE id =?", args);      // con sentencia SQL
        //db.delete("Favorites", "id=" + id_fav, null);   // con API de SQLite
        db.close();
    }

}

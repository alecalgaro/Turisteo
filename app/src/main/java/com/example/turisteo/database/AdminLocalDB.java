package com.example.turisteo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.turisteo.favorites.Favorite;

import java.util.ArrayList;

public class AdminLocalDB extends SQLiteOpenHelper {

    // En esta clase pongo lo relacionado a la BD y la voy utilizando en las pantallas donde necesito

    private static final int db_version = 1;
    private static final String db_nombre = "favorites_places";

    // Sentencia para la creación de la tabla Favorites:
    String sqlCreate = "CREATE TABLE Favorites (id INTEGER PRIMARY KEY AUTOINCREMENT, image TEXT, name TEXT)";

    // Constructor:
    public AdminLocalDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

    // Listar todos los favoritos de la BD:
    public void getFavorites(ArrayList<Favorite> arrayList) {       // uso un array de datos para añadirlos al ListView
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT id, image, name FROM Favorites", null);

        // Nos aseguramos de que existe al menos un registro:
        if (c.moveToFirst()) {
            // Recorremos el cursor hasta que no haya mas registros (c.moveToNext()):
            do {
                // Voy obteniendo los datos:
                String id = c.getString(0);
                String image = c.getString(1);
                String name = c.getString(2);
                // Los voy agregando en el arrayList (ese arrayList luego se usa para mostrar los elementos en el ListView):
                arrayList.add(new Favorite(id, image, name));
            } while (c.moveToNext());
        }
        db.close();
    }

    // Insertar nuevo lugar favorito en la BD:
    public void insertFavorite(String image, String name) {
        // Se crea el nuevo favorito a insertar como objeto ContentValues:
        ContentValues newFavorite = new ContentValues();
        newFavorite.put("image", image);
        newFavorite.put("name", name);

        // Se inserta en la base de datos:
        SQLiteDatabase db = getWritableDatabase();
        db.insert("Favorites", null, newFavorite);
        db.close();
    }

    // Eliminar favorito de la BD:
    public void deleteFavorite(String id_fav) {     // tambien puedo eliminar por nombre porque todos tendran nombres distintos
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("DELETE FROM Favorites WHERE id = " + id_fav + ";");      // con sentencia SQL
        db.delete("Favorites", "id=" + id_fav, null);   // con API de SQLite
        db.close();
    }

}

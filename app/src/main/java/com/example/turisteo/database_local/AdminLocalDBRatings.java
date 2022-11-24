package com.example.turisteo.database_local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;

public class AdminLocalDBRatings extends SQLiteOpenHelper {

    // En esta clase pongo lo relacionado a la BD y la voy utilizando en las pantallas donde necesito

    private static final int db_version = 1;
    private static final String db_nombre = "ratings";

    public String rating = null;

    // Sentencia para la creación de la tabla Ratings:
    String sqlCreate = "CREATE TABLE Ratings (id INTEGER PRIMARY KEY AUTOINCREMENT, document TEXT, rating TEXT)";

    // Constructor:
    public AdminLocalDBRatings(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
        db.execSQL("DROP TABLE IF EXISTS Ratings");
        // Se crea la nueva version de la tabla:
        db.execSQL(sqlCreate);     // también se podria hacer llamando de nuevo a onCreate(db);
    }

    // Obtener el rating asignado a un lugar.
    // Si al consultar sigue siendo rating = null es porque el usuario aun no hizo una valoracion para ese document.
    public void getRating(String document) {
        //String rating = null;
        SQLiteDatabase db = getWritableDatabase();
        String[] args = new String[] {document};
        Cursor c = db.rawQuery(" SELECT rating FROM Ratings WHERE document=?", args, null);

        // Me aseguro que existe al menos un registro:
        if (c.moveToFirst()) {
            rating = c.getString(0);
        }
        db.close();
        //return rating;
    }

    // Insertar nueva valoracion a un lugar:
    public void insertRating(String document, String rating) {
        // Se crea la nueva valoracion para insertar como objeto ContentValues:
        ContentValues newRating = new ContentValues();
        newRating.put("document", document);
        newRating.put("rating", rating);

        // Se inserta en la base de datos:
        SQLiteDatabase db = getWritableDatabase();
        db.insert("Ratings", null, newRating);
        db.close();
    }

    public void updateRating(String document, String rating){
        // Se establecen los campos y valores a actualizar
        ContentValues updateRating = new ContentValues();
        updateRating.put("rating", rating);

        // Se actualiza la BD
        SQLiteDatabase db = getWritableDatabase();
        // Dejo esta forma de hacer el update para recordar porque tenia problemas con el where y era porque lo debia escribir asi
        db.update("Ratings", updateRating, "document=" + "'" + document + "'", null);
    }

}

package com.example.turisteo.favorites;

import java.io.Serializable;

public class Favorite implements Serializable {    // lo del implements Serializable se hace para poder enviarle ese elemento en el putExtra al hacer click
                                                // Eso lo habia usado para para la app de series, no para Bedelia, pero lo deje para recordar.
    private String id;
    private int idImage;        // int porque hacemos referencia al id de la imagen
    private String nombre;

    public Favorite(String id, int idImage, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.idImage = idImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}

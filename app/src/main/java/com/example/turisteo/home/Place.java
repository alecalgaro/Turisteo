package com.example.turisteo.home;

import java.io.Serializable;

public class Place implements Serializable {    // lo del implements Serializable se hace para poder enviarle ese elemento en el putExtra al hacer click
                                                // Eso lo habia usado para para la app de series, no para Bedelia, pero lo deje para recordar.
    private String id;
    private String urlImage;
    private String nombre;
    private String description;

    public Place(String id, String urlImage, String nombre, String description) {
        this.id = id;
        this.nombre = nombre;
        this.description = description;
        this.urlImage = urlImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(int idImage) {
        this.urlImage = urlImage;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

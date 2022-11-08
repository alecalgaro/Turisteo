package com.example.turisteo.favorites;

import java.io.Serializable;

public class Favorite implements Serializable {    // lo del implements Serializable se hace para poder enviarle ese elemento en el putExtra al hacer click
                                                // Eso lo habia usado para para la app de series, no para Bedelia, pero lo deje para recordar.
    private String id;
    private String image;   // va a ser una url
    private String name;

    public Favorite(String id, String image, String name) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String idImage) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = name;
    }

}

package com.example.turisteo.home;

import java.io.Serializable;

public class Place implements Serializable {    // lo del implements Serializable se hace para poder enviarle ese elemento en el putExtra al hacer click
                                                // Eso lo habia usado para para la app de series, no para Bedelia, pero lo deje para recordar.
    private String collection;
    private String id;
    private String category;
    private String name;
    private String description_short;
    private String description_long;
    private String url_image1;
    private String url_image2;
    private String url_image3;
    private String direction;
    private String phone;
    private String web;
    private String latitude;
    private String longitude;
    private String stars_count;
    private String stars_prom;
    private String number_reviews;

    public Place(String collection, String id, String category, String name, String description_short, String description_long,
                 String url_image1, String url_image2, String url_image3, String direction, String phone, String web,
                 String latitude, String longitude, String stars_count, String stars_prom, String number_reviews) {
        this.collection = collection;
        this.id = id;
        this.category = category;
        this.name = name;
        this.description_short = description_short;
        this.description_long = description_long;
        this.url_image1 = url_image1;
        this.url_image2 = url_image2;
        this.url_image3 = url_image3;
        this.direction = direction;
        this.phone = phone;
        this.web = web;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stars_count = stars_count;
        this.stars_prom = stars_prom;
        this.number_reviews = number_reviews;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) { this.collection = collection; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription_short() {
        return description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;
    }

    public String getDescription_long() {
        return description_long;
    }

    public void setDescription_long(String description_long) {
        this.description_long = description_long;
    }

    public String getUrlImage1() {
        return url_image1;
    }

    public void setUrlImage1(String urlImage1) {
        this.url_image1 = url_image1;
    }

    public String getUrlImage2() {
        return url_image2;
    }

    public void setUrlImage2(String urlImage2) {
        this.url_image2 = url_image2;
    }

    public String getUrlImage3() {
        return url_image3;
    }

    public void setUrlImage3(String urlImage3) {
        this.url_image3 = url_image3;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStarsCount() {
        return stars_count;
    }

    public void setStarsCount(String stars_count) {
        this.stars_count = stars_count;
    }

    public String getStarsProm() {
        return stars_prom;
    }

    public void setStarsProm(String stars_prom) {
        this.stars_prom = stars_prom;
    }

    public String getNumber_reviews() {
        return number_reviews;
    }

    public void setNumber_reviews(String number_reviews) {
        this.number_reviews = number_reviews;
    }
}

package com.example.turisteo.home;

// Interfaz para hacer la comunicación entre los dos fragments.
// En el metodo envio un lugar (place) que tendra toda su informacion para poder enviarlo al
// PlaceInfoFragment y poder mostrar la informacion del mismo. Y tambien envio "backFragment" que
// sera "home" o "favorite" segun si accedo al PlaceInfoFragment desde un fragment con listado de lugares
// o desde el FavoritesFragment, para manejar de forma distinta el boton de volver hacia atras en
// el MainActivity (metodo onBackPressed).
public interface IComunicationFragments {
    public void sendPlace(Place place, String backFragment);
}

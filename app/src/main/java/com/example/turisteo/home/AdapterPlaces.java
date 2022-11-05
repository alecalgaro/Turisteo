package com.example.turisteo.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.turisteo.R;

import java.util.ArrayList;

public class AdapterPlaces extends BaseAdapter {      // recordar que extiende de BaseAdaptar. En caso de usar un RecyclerView (es mas nuevo) es distinto. Lo tengo en la carpeta del curso de Android
    private ArrayList<Place> listPlaces;
    private Context context;                    // para la clase de donde estemos llamando a este adaptador
    private LayoutInflater inflater;

    public AdapterPlaces(Context context, ArrayList<Place> listPlaces) {
        this.context = context;
        this.listPlaces = listPlaces;
    }

    @Override
    public int getCount() {
        return listPlaces.size();   // indica el tamaño de elementos en esa lista
    }

    @Override
    public Object getItem(int position) {
        return listPlaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Objeto para cada item (clase) a mostrar:
        final Place place = (Place) getItem(position);

        view = LayoutInflater.from(context).inflate(R.layout.card_place, null);    // "inflamos" la lista de items, es como para convertir un layout en un View

        // Obtengo los elementos:
        ImageView img = view.findViewById(R.id.image);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_description = view.findViewById(R.id.tv_description);

        // Inicializo:
        img.setImageResource(place.getIdImage());
        tv_name.setText(place.getNombre());
        tv_description.setText(place.getDescription());

        return view;
    }
}

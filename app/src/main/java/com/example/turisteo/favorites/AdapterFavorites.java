package com.example.turisteo.favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.turisteo.R;

import java.util.ArrayList;

public class AdapterFavorites extends BaseAdapter {      // recordar que extiende de BaseAdaptar. En caso de usar un RecyclerView (es mas nuevo) es distinto. Lo tengo en la carpeta del curso de Android
    private ArrayList<Favorite> listFavorites;
    private Context context;                    // para la clase de donde estemos llamando a este adaptador
    private LayoutInflater inflater;

    public AdapterFavorites(Context context, ArrayList<Favorite> listFavorites) {
        this.context = context;
        this.listFavorites = listFavorites;
    }

    @Override
    public int getCount() {
        return listFavorites.size();   // indica el tamaño de elementos en esa lista
    }

    @Override
    public Object getItem(int position) {
        return listFavorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Objeto para cada item (favorito) a mostrar:
        final Favorite favorite = (Favorite) getItem(position);

        view = LayoutInflater.from(context).inflate(R.layout.card_favorite, null);    // "inflamos" la lista de items, es como para convertir un layout en un View

        // Obtengo los elementos:
        ImageView img = view.findViewById(R.id.image);
        TextView tv_name = view.findViewById(R.id.tv_name);
        ImageView icon_delete = view.findViewById(R.id.icon_delete);

        // Inicializo:
        img.setImageResource(favorite.getIdImage());
        tv_name.setText(favorite.getNombre());

        return view;
    }
}

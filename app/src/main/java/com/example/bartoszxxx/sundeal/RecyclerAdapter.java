package com.example.bartoszxxx.sundeal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Product> products;

    public RecyclerAdapter() {

    }

    public RecyclerAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. utwórz inflater (narzędzie do wczytywania widoków stworzonych w XML)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // 2. wczytaj widok jednego wiersza
        View rowView = inflater.inflate(R.layout.product_list_element, parent, false);

        // 3. stwórz obiek ViewHolder, który będzie trzymać odwołania
        // do elementów jednego wiersza (np. tytułu)
        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(rowView);

        // 4. zwróć nowo utworzony obiekt
        return viewHolder;
    }


     //Ustaw produkty
    public void setReviews(List<Product> reviews) {
        this.products = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(products.get(position).getItem());
        holder.author.setText(products.get(position).getDescription());
        holder.text.setText(products.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        //W momencie tworzenia adaptera nie otrzymujemy dlatego products może być nullem
        if (products != null) {
            return products.size();
        }

        return 0;
    }


     //Pozyskaj jedną recenzję z danej pozycji.
     //Lista produktów może być nullem ponieważ nie jest przekazywana przez konstruktor.
    public Product getProduct(int index) {
        return (products != null) ? products.get(index) : null;
    }


     //Holder dla widoków.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Widgety z review_list_element.xml
        public TextView title;
        public TextView author;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            //Wyszukanie widgetów
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            text = itemView.findViewById(R.id.text);
        }
    }
}
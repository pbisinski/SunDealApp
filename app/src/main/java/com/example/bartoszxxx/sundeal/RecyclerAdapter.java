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
        // 1. utwórz inflater (narzędzie do wczytywania widoków stworzonych w XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // 2. wczytaj widok jednego wiersza
        View rowView = inflater.inflate(R.layout.product_list_element, parent, false);

        // 3. stwórz obiek ViewHolder, który będzie trzymać odwołania
        // do elementów jednego wiersza (np. tytułu)
        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(rowView);

        // 4. zwróć nowo utworzony obiekt
        return viewHolder;
    }

    /**
     * Ustaw recenzje.
     * W poprzednich rozwiązaniach (lista książek i komentarzy)
     * recenzje były przekazywane przez konstruktor.
     *
     * @param reviews lista recenzji
     */
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
        // w momencie tworzenia adaptera nie otrzymujemy recenzjie
        // dlatego reviews może być nullem
        if (products != null) {
            return products.size();
        }

        return 0;
    }

    /**
     * Pozyskaj jedną recenzję z danej pozycji.
     * Lista recenzji może być nullem ponieważ recenzje
     * nie są przekazywane przez konstruktor.
     *
     * @param index pozycja recenzji
     * @return recenzja lub null
     */
    public Product getProduct(int index) {
        return (products != null) ? products.get(index) : null;
    }

    /**
     * Holder dla widoków.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //widgety z review_list_element.xml
        public TextView title;
        public TextView author;
        public TextView text;
        //TODO dodać wyświetlanie ID

        public ViewHolder(View itemView) {
            super(itemView);
            //wyszukanie widgetów
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            text = itemView.findViewById(R.id.text);
            //TODO dodać wyświetlanie ID
        }
    }
}
package com.example.bartoszxxx.sundeal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {

    private List<Product> products;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    public MyProductsAdapter(Context context) {

    }

    public MyProductsAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. utwórz inflater (narzędzie do wczytywania widoków stworzonych w XML)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // 2. wczytaj widok jednego wiersza
        View rowView = inflater.inflate(R.layout.product_list_element, parent, false);

        // 3. stwórz obiek ViewHolder, który będzie trzymać odwołania
        // do elementów jednego wiersza (np. tytułu)
        MyProductsAdapter.ViewHolder viewHolder = new MyProductsAdapter.ViewHolder(rowView);

        // 4. zwróć nowoutworzony obiekt
        return viewHolder;
    }


     //Ustaw produkty
    public void setReviews(List<Product> reviews) {
        this.products = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyProductsAdapter.ViewHolder holder, int position) {
        final Product listItem = products.get(position);

        holder.title.setText(products.get(position).getItem());
        holder.author.setText(products.get(position).getDescription());
        holder.text.setText(products.get(position).getLocation());

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.options);
                //inflating menu from xml resource
                popup.inflate(R.menu.item);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        database = FirebaseDatabase.getInstance();
                        ref = database.getReference("Products");
                        ref.child(listItem.getKey()).removeValue();
                        Toast.makeText(context,"Usunięto", Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
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
        public TextView options;

        public ViewHolder(View itemView) {
            super(itemView);
            //Wyszukanie widgetów
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            text = itemView.findViewById(R.id.text);
            options = itemView.findViewById(R.id.options);
        }
    }
}
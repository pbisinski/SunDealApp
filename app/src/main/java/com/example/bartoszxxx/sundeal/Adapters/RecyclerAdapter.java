package com.example.bartoszxxx.sundeal.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartoszxxx.sundeal.Products.ListProduct;
import com.example.bartoszxxx.sundeal.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ListProduct> products;
    private Context context;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public RecyclerAdapter(List<ListProduct> products, Context context) {
        this.products = products;
        this.context = context;
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

        // 4. zwróć nowoutworzony obiekt
        return viewHolder;
    }


     //Produkty to recenzje
    public void setProducts(List<ListProduct> reviews) {
        this.products = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        final ListProduct listItem = products.get(position);

        holder.title.setText(products.get(position).getItem());
        holder.author.setText(products.get(position).getDescription());
        if (products.get(position).getOddam()){
            holder.text.setText("Oddam");
        } else {
            holder.text.setText("Zamienię");
        }

        //Obsluga klikniecia na element listy
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, listItem.getItem(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (products != null) {
            return products.size();
        }
        return 0;
    }

    public ListProduct getProduct(int index) {
        return (products != null) ? products.get(index) : null;
    }

     //Holder dla widoków.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public TextView text;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            text = itemView.findViewById(R.id.text);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
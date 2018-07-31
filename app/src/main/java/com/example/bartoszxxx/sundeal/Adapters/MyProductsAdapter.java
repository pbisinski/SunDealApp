package com.example.bartoszxxx.sundeal.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartoszxxx.sundeal.MyProductsActivity;
import com.example.bartoszxxx.sundeal.Products.FirebaseHelper;
import com.example.bartoszxxx.sundeal.Products.Product;
import com.example.bartoszxxx.sundeal.R;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {

    private List<Product> products;
    private Context context;
    private FirebaseAuth firebaseAuth;

    public MyProductsAdapter() {

    }

    public MyProductsAdapter(Context context) {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.my_product_list_element, parent, false);
        MyProductsAdapter.ViewHolder viewHolder = new MyProductsAdapter.ViewHolder(rowView);
        return viewHolder;
    }

    public void setProducts(List<Product> reviews) {
        this.products = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyProductsAdapter.ViewHolder holder, final int position) {
        final Product listItem = products.get(position);

        holder.title.setText(products.get(position).getItem());
        holder.author.setText(products.get(position).getDescription());
        holder.text.setText(products.get(position).getLocation());

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.options);
                popup.inflate(R.menu.remove);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference(FirebaseHelper.DATABASE_REFERENCE);
                        database.getRef().child(listItem.getKey()).removeValue();
                        Toast.makeText(context, "Usunięto pomyślnie", Toast.LENGTH_LONG).show();
                        products.remove(position);
                        notifyItemRemoved(position);
                        return true;
                    }
                });
                popup.show();
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

    public Product getProduct(int index) {
        return (products != null) ? products.get(index) : null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public TextView text;
        public TextView options;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            text = itemView.findViewById(R.id.text);
            options = itemView.findViewById(R.id.options);
        }
    }
}
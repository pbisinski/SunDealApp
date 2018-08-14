package com.example.bartoszxxx.sundeal.Listing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bartoszxxx.sundeal.Product;
import com.example.bartoszxxx.sundeal.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;

    public MyProductsAdapter() {

    }

    @NonNull
    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rowView = LayoutInflater.from(context).inflate(R.layout.my_product_list_element, parent, false);
        return new MyProductsAdapter.ViewHolder(rowView);
    }

    public void setProducts(List<Product> reviews) {
        this.products = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyProductsAdapter.ViewHolder holder, final int position) {
        holder.title.setText(products.get(position).getTitle());
        holder.author.setText(products.get(position).getDescription());
        holder.text.setText(products.get(position).getLocation());
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
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.text)
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
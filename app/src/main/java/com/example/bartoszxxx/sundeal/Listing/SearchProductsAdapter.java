package com.example.bartoszxxx.sundeal.Listing;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bartoszxxx.sundeal.Product;
import com.example.bartoszxxx.sundeal.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;

    public SearchProductsAdapter() {

    }

    @NonNull
    @Override
    public SearchProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View rowView = LayoutInflater.from(context).inflate(R.layout.product_list_element, parent, false);
        return new SearchProductsAdapter.ViewHolder(rowView);
    }

    public void setProducts(List<Product> reviews) {
        this.products = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchProductsAdapter.ViewHolder holder, int position) {
        holder.title.setText(products.get(position).getTitle());
        if (products.get(position).getGiveaway()) {
            holder.type.setText(R.string.giveaway_product);
        } else {
            holder.type.setText(R.string.exchange_product);
        }
        holder.description.setText(products.get(position).getDescription());
        holder.location.setText(products.get(position).getLocation());

        Glide.with(context)
                .load(products.get(position).getPhotoUrl())
                .into(holder.photo);

        holder.contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", products.get(holder.getAdapterPosition()).getOwner(), null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "SunDeal: " + products.get(holder.getAdapterPosition()).getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, "Witam, jestem zainteresowany Pani/Pana ofertą.");
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e("MAILTO", "error");
                }
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

    private Product getProduct(int index) {
        return (products != null) ? products.get(index) : null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleView)
        TextView title;
        @BindView(R.id.typeView)
        TextView type;
        @BindView(R.id.descriptionView)
        TextView description;
        @BindView(R.id.locationView)
        TextView location;
        @BindView(R.id.contactBtn)
        ImageView contactBtn;
        @BindView(R.id.photoView)
        ImageView photo;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, itemView);
        }
    }
}
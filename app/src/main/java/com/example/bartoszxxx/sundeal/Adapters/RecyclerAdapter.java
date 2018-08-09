package com.example.bartoszxxx.sundeal.Adapters;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.bartoszxxx.sundeal.Products.ProductLocal;
import com.example.bartoszxxx.sundeal.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private RequestManager glide;
    private List<ProductLocal> products;

    public RecyclerAdapter(RequestManager glide) {
        this.glide = glide;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.product_list_element, parent, false);
        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(rowView);
        return viewHolder;
    }

    public void setProducts(List<ProductLocal> reviews) {
        this.products = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, final int position) {
        final Context c = holder.context;
        holder.title.setText(products.get(position).getTitle());
        if (products.get(position).getGiveaway()) {
            holder.type.setText("Oddam");
        } else {
            holder.type.setText("Zamienię");
        }
        holder.description.setText(products.get(position).getDescription());
        holder.location.setText(products.get(position).getLocation());
        glide.load(products.get(position).getPhotoUrl()).into(holder.photo);
        holder.contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", products.get(position).getOwner(), null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "SunDeal: " + products.get(position).getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, "Witam, jestem zainteresowany Pani/Pana ofertą.");
                    c.startActivity(intent);
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

    private ProductLocal getProduct(int index) {
        return (products != null) ? products.get(index) : null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView type;
        private TextView description;
        private TextView location;
        private ImageView contactBtn;
        private ImageView photo;
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleView);
            type = itemView.findViewById(R.id.typeView);
            photo = itemView.findViewById(R.id.photoView);
            description = itemView.findViewById(R.id.descriptionView);
            location = itemView.findViewById(R.id.locationView);
            contactBtn = itemView.findViewById(R.id.contactBtn);
            context = itemView.getContext();
        }
    }
}
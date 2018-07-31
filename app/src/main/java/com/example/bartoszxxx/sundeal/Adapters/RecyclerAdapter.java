package com.example.bartoszxxx.sundeal.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bartoszxxx.sundeal.Products.ListProduct;
import com.example.bartoszxxx.sundeal.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ListProduct> products;
    private Context context;
    private Activity activity;

    public RecyclerAdapter() {

    }

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        activity = (Activity) context;
        View rowView = inflater.inflate(R.layout.product_list_element, parent, false);
        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(rowView);
        return viewHolder;
    }

    public void setProducts(List<ListProduct> reviews) {
        this.products = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, final int position) {
        final ListProduct listItem = products.get(position);

        holder.title.setText(products.get(position).getItem());
        if (products.get(position).getOddam()) {
            holder.text.setText("Oddam");
        } else {
            holder.text.setText("Zamienię");
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                LayoutInflater inflater = alertDialog.getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_product, null);
                alertDialog.setView(v);
                TextView item = v.findViewById(R.id.itemName);
                TextView desc = v.findViewById(R.id.itemDescription);
                TextView ownr = v.findViewById(R.id.itemOwner);
                TextView locn = v.findViewById(R.id.itemLocation);
                item.setText(products.get(position).getItem());
                desc.setText(products.get(position).getDescription());
                ownr.setText(products.get(position).getOwner());
                locn.setText(products.get(position).getLocation());

                alertDialog.setButton("Kontynuuj...", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", products.get(position).getOwner(), null));
                            intent.putExtra(Intent.EXTRA_SUBJECT, "SunDeal: " + products.get(position).getItem());
                            intent.putExtra(Intent.EXTRA_TEXT, "Witam, jestem zainteresowany Pani/Pana ofertą.");
                            activity.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Log.e("MAILTO", "error");
                        }
                    }
                });

                alertDialog.show();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView text;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
package com.sundeal.sundeal;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Item> list = new ArrayList<>();
    private Context context;

    public RecyclerAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View rowView = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new RecyclerAdapter.ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.title.setText(list.get(i).getName());
        String categoryName;
        int category = list.get(i).getCategory();
        Resources res = context.getResources();
        switch (category) {
            case 1:
                categoryName = res.getString(R.string.category_1);
                break;
            case 2:
                categoryName = res.getString(R.string.category_2);
                break;
            case 3:
                categoryName = res.getString(R.string.category_3);
                break;
            default:
                categoryName = "Invalid category:" + category;
                break;
        }
        viewHolder.type.setText(categoryName);
        viewHolder.description.setText(list.get(i).getDescription());
        viewHolder.location.setText(list.get(i).getLocation());

        RequestOptions glideOptions = new RequestOptions().centerCrop();
        Glide.with(context)
                .load(list.get(i).getPhotoUrl())
                .apply(glideOptions)
                .into(viewHolder.photo);
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public void add(List<Item> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void add(Item item) {
        this.list.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
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
        @BindView(R.id.photoBg)
        ImageView photo;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }

    }
}
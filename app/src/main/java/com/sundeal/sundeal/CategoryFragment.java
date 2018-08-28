package com.sundeal.sundeal;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CategoryFragment extends Fragment implements View.OnClickListener {

    View fragment;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_category, container, false);
        Button b1 = fragment.findViewById(R.id.button1);
        Button b2 = fragment.findViewById(R.id.button2);
        Button b3 = fragment.findViewById(R.id.button3);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        Bundle b = new Bundle();
        Resources res = view.getContext().getResources();
        switch (view.getId()) {
            case R.id.button1:
                b.putInt("category", 1);
                b.putString("title", res.getString(R.string.category_1));
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.button2:
                b.putInt("category", 2);
                b.putString("title", res.getString(R.string.category_2));
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.button3:
                b.putInt("category", 3);
                b.putString("title", res.getString(R.string.category_3));
                intent.putExtras(b);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}

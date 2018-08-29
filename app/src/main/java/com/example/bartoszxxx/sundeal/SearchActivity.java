package com.example.bartoszxxx.sundeal;

import android.app.ActionBar;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bartoszxxx.sundeal.Listing.SearchProductsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements FirebaseHelper {

    private SearchProductsAdapter mAdapter;
    private List<Product> products;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.products_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        products = new ArrayList<>();
        mAdapter = new SearchProductsAdapter();
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        returnProducts(ALL_PRODUCTS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            Handler mHandler = new Handler();

            @Override
            public boolean onQueryTextSubmit(final String s) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        returnProducts(s);
                    }
                }, 500);
                return true;
            }
        });
        return true;
    }

    public void returnProducts(String queryString) {
        Query query;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(FirebaseHelper.DATABASE_REFERENCE);
        if (queryString == null) {
            query = database.limitToLast(15);
        } else {
            query = database.orderByChild(SEARCH_PAR).startAt(queryString).endAt(queryString + "\uf8ff");
        }
        getFromDatabase(query);
    }

    public void  getFromDatabase(final Query query) {
        products.clear();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Product product = childDataSnapshot.getValue(Product.class);
                    try {
                        products.add(product);
                    } catch (NullPointerException e) {
                        Log.e("ProductList", e.toString());
                    }
                }
                mAdapter.setProducts(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

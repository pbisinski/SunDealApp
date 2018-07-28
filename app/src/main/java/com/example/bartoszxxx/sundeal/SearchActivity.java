package com.example.bartoszxxx.sundeal;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.bartoszxxx.sundeal.Adapters.RecyclerAdapter;
import com.example.bartoszxxx.sundeal.Products.ListProduct;
import com.example.bartoszxxx.sundeal.Products.ProductFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseHelper firebaseHelper;
    private RecyclerAdapter rAdapter;
    private List<ListProduct> products;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        firebaseHelper = new FirebaseHelper();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.products_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(rAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllProducts("");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                products = null;
                rAdapter.setProducts(products);
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
                if (s.equals("")) {

                    return false;
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAllProducts(s);
                    }
                }, 500);
                return true;
            }

        });
        return true;
    }

    public void getAllProducts(String queryText) {
        products = new ArrayList<>();
        Query queryRef = firebaseHelper.getRef().orderByChild("item_lowercase").startAt(queryText).endAt(queryText + "\uf8ff");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String owner = childDataSnapshot.getValue(ProductFirebase.class).getOwner();
                    String item = childDataSnapshot.getValue(ProductFirebase.class).getItem();
                    String description = childDataSnapshot.getValue(ProductFirebase.class).getDescription();
                    String location = childDataSnapshot.getValue(ProductFirebase.class).getLocation();
                    String key = childDataSnapshot.getValue(ProductFirebase.class).getKey();
                    Boolean oddam = childDataSnapshot.getValue(ProductFirebase.class).getOddam();
                    Boolean zamienie = childDataSnapshot.getValue(ProductFirebase.class).getZamienie();
                    ListProduct product = new ListProduct(owner, item, description, location, oddam, zamienie, key);
                    try {
                        products.add(product);
                    } catch (NullPointerException e) {
                        Log.e("ProductList", e.toString());
                    }
                }
                rAdapter.setProducts(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

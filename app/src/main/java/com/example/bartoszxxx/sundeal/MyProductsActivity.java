package com.example.bartoszxxx.sundeal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bartoszxxx.sundeal.Adapters.MyProductsAdapter;
import com.example.bartoszxxx.sundeal.Products.Product;
import com.example.bartoszxxx.sundeal.Products.ProductFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity {

    private FirebaseHelper firebaseHelper;
    private List<Product> products;
    private MyProductsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseHelper = new FirebaseHelper();

        TextView TvUserName = (TextView) findViewById(R.id.TvUserName);
        TvUserName.setText("Witaj, " + firebaseHelper.getFirebaseUser().getDisplayName());

        mAdapter = new MyProductsAdapter(this);
        recyclerView.setAdapter(mAdapter);
        getUserProducts();

        Button BtnLogout = (Button) findViewById(R.id.BtnLogout);
        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseHelper.getFirebaseAuth().signOut();
                finish();
                startActivity(new Intent(MyProductsActivity.this, SignInActivity.class));
            }
        });

        Button BtnSettings = (Button) findViewById(R.id.BtnSettings);
        BtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProductsActivity.this, SettingsActivity.class));
            }
        });
    }

    public void getUserProducts() {
        products = new ArrayList<>();
        Query queryRef = firebaseHelper.getRef().orderByChild("owner").equalTo(firebaseHelper.getFirebaseUser().getEmail());
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String item = childDataSnapshot.getValue(ProductFirebase.class).getItem();
                    String description = childDataSnapshot.getValue(ProductFirebase.class).getDescription();
                    String location = childDataSnapshot.getValue(ProductFirebase.class).getLocation();
                    String key = childDataSnapshot.getValue(ProductFirebase.class).getKey();
                    Product product = new Product(item, description, location, key);
                    products.add(product);
                }
                mAdapter.setProducts(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

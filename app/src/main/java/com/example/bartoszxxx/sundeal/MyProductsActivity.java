package com.example.bartoszxxx.sundeal;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bartoszxxx.sundeal.Adapters.MyProductsAdapter;
import com.example.bartoszxxx.sundeal.Products.FirebaseHelper;
import com.example.bartoszxxx.sundeal.Products.Product;
import com.example.bartoszxxx.sundeal.Products.ProductFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private List<Product> products;
    private MyProductsAdapter mAdapter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mAdapter = new MyProductsAdapter(this);
        recyclerView.setAdapter(mAdapter);

        Button BtnLogout = (Button) findViewById(R.id.BtnLogout);
        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Log.i("SHARED_PREFRENCES", "preferences cleared");
                Intent intent = new Intent(MyProductsActivity.this, SignInActivity.class);
                finish();
                startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        getUserProducts();
        TextView TvUserName = (TextView) findViewById(R.id.TvUserName);
        String name = prefs.getString("name", "");
        String message = getString(R.string.welcome_message, name);
        TvUserName.setText(message);
    }

    public void getUserProducts() {
        products = new ArrayList<>();
        Query queryRef = FirebaseDatabase.getInstance().getReference(FirebaseHelper.DATABASE_REFERENCE).orderByChild("owner").equalTo(firebaseAuth.getCurrentUser().getEmail());
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

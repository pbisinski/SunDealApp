package com.example.bartoszxxx.sundeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private File file;
    private TextView nav_user;
    private TextView nav_email;
    private List<Product> products;
    private RecyclerAdapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        nav_user = (TextView)hView.findViewById(R.id.nav_name);
        nav_email = (TextView)hView.findViewById(R.id.nav_email);

        //dostep do informacji o zalogowanym uzytkowniku
        firebaseUser = firebaseAuth.getCurrentUser();
        nav_user.setText(firebaseUser.getDisplayName());
        nav_email.setText(firebaseUser.getEmail());

        //utworzenie katalogu danych uzytkownika
        file = new File(this.getFilesDir(), "sundealapp.data");
        if(!file.exists()){
            file.mkdir();
        }

        //pobranie produktow uzytkownika do pliku
        getUserProducts();

        products = new ArrayList<>();

        // znajdź RecyclerView
        RecyclerView recyclerView = findViewById(R.id.products_recycler);

        // ustawienie sposobu rozmieszczenia elementów
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // utworzenie adaptera
        rAdapter = new RecyclerAdapter();

        // połączenie adaptera z RecyclerView
        recyclerView.setAdapter(rAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
                    //nastąpi wylogowanie
                    firebaseAuth.signOut();
                    //zamknięcie aktywności
                    finish();
                    //uruchamianie SignInActivity
                    startActivity(new Intent(this, SignInActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent przejscie;
            przejscie = new Intent(MainActivity.this, MyProductsActivity.class);
            this.startActivity(przejscie);
        } else if (id == R.id.add) {
            Intent przejscie;
            przejscie = new Intent(MainActivity.this, AddItemActivity.class);
            this.startActivity(przejscie);

        } else if (id == R.id.about) {
            Intent przejscie;
            przejscie = new Intent(MainActivity.this, AboutAppActivity.class);
            this.startActivity(przejscie);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //pobranie informacji o produktach uzytkownika
    public void getUserProducts(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
        Query queryRef = ref.orderByChild("owner").equalTo(firebaseUser.getEmail());

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                File data = new File(file, "user_data");
                try{
                    if (data.exists()) {
                        data.delete();
                    }
                    data.createNewFile();
                } catch (IOException e) {
                    Log.e("ERROR", "blad tworzenia pliku");
                }

                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    //User user = dataSnapshot.getValue(User.class);
                    try{
                        FileWriter fw = new FileWriter(data, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.newLine();
                        bw.write(childDataSnapshot.getValue(User.class).getItem());
                        bw.write(",");
                        bw.write(childDataSnapshot.getValue(User.class).getDescription());
                        bw.write(",");
                        bw.write(childDataSnapshot.getValue(User.class).getLocation());
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        Log.e("ERROR", "blad zapisu pliku");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //pobranie i wyswietlenie wyszukanych produktow do RecyclerView
    public void getAllProducts(View view){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
        Query queryRef = ref.orderByKey();

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    //User user = dataSnapshot.getValue(User.class);
                    String item = childDataSnapshot.getValue(User.class).getItem();
                    String description = childDataSnapshot.getValue(User.class).getDescription();
                    String location = childDataSnapshot.getValue(User.class).getLocation();
                    Product product = new Product(item, description, location);
                    products.add(product);
                }
                rAdapter.setReviews(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


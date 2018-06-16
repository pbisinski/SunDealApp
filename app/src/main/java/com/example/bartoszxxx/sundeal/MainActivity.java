package com.example.bartoszxxx.sundeal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.SearchView;
import android.widget.TextView;

import com.example.bartoszxxx.sundeal.Adapters.RecyclerAdapter;
import com.example.bartoszxxx.sundeal.Products.ListProduct;
import com.example.bartoszxxx.sundeal.Products.ProductFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseHelper firebaseHelper;
    private File file;
    private TextView nav_user;
    private TextView nav_email;
    private List<ListProduct> products;
    private RecyclerAdapter rAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Wpisz nazwę produktu");

        //Polaczenie z FireBase
        firebaseHelper = new FirebaseHelper();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Wyszukaj produkt");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Wyswietlenie danych zalogowanego uzytkownika w panelu bocznym
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.nav_name);
        nav_user.setText(firebaseHelper.getFirebaseUser().getDisplayName());
        nav_email = (TextView) hView.findViewById(R.id.nav_email);
        nav_email.setText(firebaseHelper.getFirebaseUser().getEmail());

        //Utworzenie katalogu danych uzytkownika
        file = new File(this.getFilesDir(), "sundealapp.data");
        if(!file.exists()){
            file.mkdir();
        }

        // pobranie produktow uzytkownika do pliku TODO usprawnienie
        getUserProducts();

        // znajdź RecyclerView
        RecyclerView recyclerView = findViewById(R.id.products_recycler);

        // ustawienie sposobu rozmieszczenia elementów
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // utworzenie adaptera
        rAdapter = new RecyclerAdapter(this);

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
        //Dodanie pola dynamicznego wyszukiwania
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();

        //Po otwarciu SearchView wyswietl wszystkie rekordy
        searchView.setOnSearchClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getAllProducts("");
           }
       });
        //Po zamknieciu SearchView wyczysc liste rekordow
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
           @Override
           public boolean onClose() {
                products = null;
                rAdapter.setProducts(products);
                return false;
            }
        });
        //Opoznione wyslanie zapytania po wpisaniu tekstu
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
               if(s.equals("")){

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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            firebaseHelper.getFirebaseAuth().signOut();
//            finish();
//            startActivity(new Intent(this, SignInActivity.class));
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Obsluga opcji panelu bocznego
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent przejscie = new Intent(MainActivity.this, MyProductsActivity.class);
            this.startActivity(przejscie);
        } else if (id == R.id.add) {
            Intent przejscie = new Intent(MainActivity.this, AddItemActivity.class);
            this.startActivity(przejscie);
        } else if (id == R.id.settings) {
            Intent przejscie = new Intent(MainActivity.this, SettingsActivity.class);
            this.startActivity(przejscie);
        } else if (id == R.id.about) {
            Intent przejscie = new Intent(MainActivity.this, AboutAppActivity.class);
            this.startActivity(przejscie);
        } else if (id == R.id.logout) {
            firebaseHelper.getFirebaseAuth().signOut();
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Pobranie informacji o produktach uzytkownika
    public void getUserProducts(){
        Query queryRef = firebaseHelper.getRef().orderByChild("owner").equalTo(firebaseHelper.getFirebaseUser().getEmail());
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                File data = new File(file, "user_data");
                try{
                    if (data.exists()) {
                        data.delete();
                        data.createNewFile();
                    }
                } catch (IOException e) {
                    Log.e("ERROR", "blad tworzenia pliku");
                }

                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    //Wpisanie rekordow do pliku w pamieci lokalnej (oddzielanie znakiem specjalnym)
                    try{
                        FileWriter fw = new FileWriter(data, true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.newLine();
                        bw.write(childDataSnapshot.getValue(ProductFirebase.class).getItem());
                        bw.write("`");
                        bw.write(childDataSnapshot.getValue(ProductFirebase.class).getDescription());
                        bw.write("`");
                        bw.write(childDataSnapshot.getValue(ProductFirebase.class).getLocation());
                        bw.write("`");
                        bw.write(childDataSnapshot.getValue(ProductFirebase.class).getKey());
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

    //Zapytanie i wyswietlenie wyszukanych produktow po nazwie do RecyclerView
    public void getAllProducts(String queryText){
        products = new ArrayList<>();
        Query queryRef = firebaseHelper.getRef().orderByChild("item_lowercase").startAt(queryText).endAt(queryText + "\uf8ff");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    //Pobranie danych z obiektu bazy, dodanie do listy produktow
                    String owner = childDataSnapshot.getValue(ProductFirebase.class).getKey();
                    String item = childDataSnapshot.getValue(ProductFirebase.class).getItem();
                    String description = childDataSnapshot.getValue(ProductFirebase.class).getDescription();
                    String location = childDataSnapshot.getValue(ProductFirebase.class).getLocation();
                    String key = childDataSnapshot.getValue(ProductFirebase.class).getKey();
                    Boolean oddam = childDataSnapshot.getValue(ProductFirebase.class).getOddam();
                    Boolean zamienie = childDataSnapshot.getValue(ProductFirebase.class).getZamienie();
                    ListProduct product = new ListProduct(owner, item, description, location, oddam, zamienie, key);
                    products.add(product);
                }
                rAdapter.setProducts(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


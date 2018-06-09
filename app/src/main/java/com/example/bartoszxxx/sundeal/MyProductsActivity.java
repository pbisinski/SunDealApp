package com.example.bartoszxxx.sundeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity {

    private File file;
    private List<Product> products;
    private RecyclerAdapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //dostep do pliku z danymi
        file = new File(this.getFilesDir(), "sundealapp.data");

        //znajdź RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler);

        //ustawienie sposobu rozmieszczenia elementów
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //stworzenie listy produktow
        products = new ArrayList<>();
        try {
            File data = new File(file, "user_data");
            FileReader fileReader = new FileReader(data);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            //odczytanie jednej linii (pusta)
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] dane = line.split(",",5);
                Product product = new Product(dane[0],dane[1],dane[2]);
                products.add(product);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //utworzenie adaptera
        rAdapter = new RecyclerAdapter(products);

        //połączenie adaptera z RecyclerView
        recyclerView.setAdapter(rAdapter);
    }



}

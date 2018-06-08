package com.example.bartoszxxx.sundeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
        file = new File(this.getFilesDir(), "sundealapp.data");
        list = (ListView) findViewById(R.id.list);
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listItems);
        list.setAdapter(adapter);
        try {
            File data = new File(file, "user_data");
            FileReader fileReader = new FileReader(data);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {

                listItems.add(line);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

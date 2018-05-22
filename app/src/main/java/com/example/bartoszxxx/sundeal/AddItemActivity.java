package com.example.bartoszxxx.sundeal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddItemActivity extends AppCompatActivity {

    EditText item, description, location;
    Button insert;
    FirebaseDatabase database;
    DatabaseReference ref;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = (EditText) findViewById(R.id.item);
        description = (EditText) findViewById(R.id.description);
        location = (EditText) findViewById(R.id.location);
        insert = (Button) findViewById(R.id.BtnInsert);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
        user = new User();

    }

    private void getValues() {
        user.setItem(item.getText().toString());
        user.setDescription(description.getText().toString());
        user.setLocation(location.getText().toString());
    }

    public void BtnInsert(View view) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getValues();
                ref.child("User01").setValue(user);
                Toast.makeText(AddItemActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
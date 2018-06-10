package com.example.bartoszxxx.sundeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends AppCompatActivity {

    EditText item, description, location;
    Button insert;
    Switch oddam, zamienie;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        //Nazwa tabeli z produktami
        ref = database.getReference("Products");

        item = (EditText) findViewById(R.id.item);
        description = (EditText) findViewById(R.id.description);
        location = (EditText) findViewById(R.id.location);
        insert = (Button) findViewById(R.id.BtnInsert);
        oddam = (Switch) findViewById(R.id.switch1);
        zamienie = (Switch) findViewById(R.id.switch2);
    }

    //Czyszczenie pól
    private void setClear() {
        item.setText("");
        description.setText("");
        location.setText("");
        oddam.setChecked(false);
        zamienie.setChecked(false);
    }

    //Dodaj rekord do bazy
    public void BtnInsert(View view) {

        String itemValue = item.getText().toString().trim();
        String descriptionValue = description.getText().toString().trim();
        String locationValue = location.getText().toString();

        if(TextUtils.isEmpty(itemValue)){
            Toast.makeText(this,"Podaj nazwę produktu",Toast.LENGTH_LONG).show();
            return;
        }

        //opis moze byc pusty?

        if(TextUtils.isEmpty(locationValue)){
            Toast.makeText(this,"Podaj lokalizację",Toast.LENGTH_LONG).show();
            return;
        }

        String id = ref.push().getKey();
        ProductFirebase productFirebase = new ProductFirebase(
                firebaseUser.getEmail(),
                itemValue,
                itemValue.toLowerCase(),
                descriptionValue,
                locationValue,
                oddam.isChecked(),
                zamienie.isChecked(),
                id);
        ref.child(id).setValue(productFirebase);
        Toast.makeText(AddItemActivity.this, "Pomyślnie dodano: "+productFirebase.getItem(), Toast.LENGTH_LONG).show();
        setClear();
    }

    @Override
    public void onBackPressed() {
        final Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);
        finish();
    }
}
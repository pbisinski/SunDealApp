package com.example.bartoszxxx.sundeal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddItemActivity extends AppCompatActivity {

    EditText item, description, location;
    Button insert;
    String oddam_zamienie="";
    Switch oddam, zamienie;
    FirebaseDatabase database;
    DatabaseReference ref;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        item = (EditText) findViewById(R.id.item);
        description = (EditText) findViewById(R.id.description);
        location = (EditText) findViewById(R.id.location);
        insert = (Button) findViewById(R.id.BtnInsert);
        oddam = (Switch) findViewById(R.id.switch1);
        zamienie = (Switch) findViewById(R.id.switch2);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");


    }

    private void isOddam() {
        if(oddam.isChecked()){oddam_zamienie = oddam_zamienie.concat("oddam");}
        if(oddam.isChecked()&&zamienie.isChecked()){oddam_zamienie = oddam_zamienie.concat(" , ");}
        if(zamienie.isChecked()){oddam_zamienie = oddam_zamienie.concat("zamienie");}
    }
    private void setClear() {
        item.setText("");
        description.setText("");
        location.setText("");
    }

    public void BtnInsert(View view) {
        isOddam();
        String id = ref.push().getKey();
        User user = new User(item.getText().toString(), description.getText().toString(), location.getText().toString(), oddam_zamienie);
        ref.child(id).setValue(user);
        Toast.makeText(AddItemActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        setClear();
    }}
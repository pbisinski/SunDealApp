package com.example.bartoszxxx.sundeal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        item = (EditText) findViewById(R.id.item);
        description = (EditText) findViewById(R.id.description);
        location = (EditText) findViewById(R.id.location);
        insert = (Button) findViewById(R.id.BtnInsert);
        oddam = (Switch) findViewById(R.id.switch1);
        zamienie = (Switch) findViewById(R.id.switch2);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
    }

    private void setClear() {
        item.setText("");
        description.setText("");
        location.setText("");
        oddam.setChecked(false);
        zamienie.setChecked(false);
    }

    public void BtnInsert(View view) {
        String id = ref.push().getKey();
        User user = new User(firebaseUser.getEmail(), item.getText().toString(), description.getText().toString(), location.getText().toString(), oddam.isChecked(), zamienie.isChecked());
        ref.child(id).setValue(user);
        Toast.makeText(AddItemActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        setClear();
    }
}
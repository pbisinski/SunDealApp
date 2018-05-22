package com.example.bartoszxxx.sundeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //iinicjalizacja obiektu autoryzacji firebase
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            //uruchomienie logowania - SignInActivity
            startActivity(new Intent(this, SignInActivity.class));
        }

                FirebaseUser user = firebaseAuth.getCurrentUser();

        //inicjalizacja widoków
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //wyświetlanie zalogowanego użytkownika
        textViewUserEmail.setText("Witaj "+user.getEmail());

        //odczyt kliknięcia wylogowania
        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //jeżeli klikniemy wyloguj
        if(view == buttonLogout){
            //nastąpi wylogowanie
            firebaseAuth.signOut();
            //zamknięcie aktywności
            finish();
            //uruchamianie SignInActivity
            startActivity(new Intent(this, SignInActivity.class));
        }
    }

    public void przejscie(View view) {
        Intent przejscie;
        przejscie = new Intent(ProfileActivity.this, MainActivity.class);
        this.startActivity(przejscie);
        return;
    }
}

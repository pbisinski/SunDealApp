package com.example.bartoszxxx.sundeal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartoszxxx.sundeal.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {


    //definiowanie pól
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private File file;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //utworzenie obiektu autoryzacji firebase
        firebaseAuth = FirebaseAuth.getInstance();



        //jeżeli not null to uzytkownik zalogowany
        if(firebaseAuth.getCurrentUser() != null){
            //zamyka aktywność
            finish();
            //otwiera aktywność ProfileActivity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //inicjalizacja widoków
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        textViewSignup  = (TextView) findViewById(R.id.textViewSignup);

        progressDialog = new ProgressDialog(this);

        //odbieranie kliknięcia
        buttonSignIn.setOnClickListener(this);
        //textViewSignup.setOnClickListener(this);
    }

    //metoda - logowanie
    private void userLogin(){
        final String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //kontrola czy zostały wpisane e-mail i hasło
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //jeżeli pola nie są puste to wyświetla się progressDialog - logowanie w trakcie

        progressDialog.setMessage("Logowanie, proszę czekać");
        progressDialog.show();

        //logowanie użytkownika
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //jeżeli logowanie się powiodło uruchamia się ProfileActivity
                        if(task.isSuccessful()){
                            //uruchomienie ProfileActivity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            BufferedWriter bw = null;
                            FileWriter fw = null;

                            try{
                                File user_data = new File(file, "user");
                                FileWriter writer = new FileWriter(user_data);
                                writer.append(email);
                                writer.flush();
                                writer.close();

                            } catch (IOException e) {
                                Log.e("ERROR", "blad zapisu pliku");
                            }
                        }
                    }
                });

    }

    //przejście przyciskiem do MainActivity
    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();
        }
    }
}
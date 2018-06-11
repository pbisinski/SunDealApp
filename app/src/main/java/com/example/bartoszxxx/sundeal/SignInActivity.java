package com.example.bartoszxxx.sundeal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {


    //definiowanie pól
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private FirebaseHelper firebaseHelper;
    private ProgressDialog progressDialog;
    private Button buttonSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //polaczenie z baza
        firebaseHelper = new FirebaseHelper();

        //jeżeli not null to uzytkownik zalogowany
        if(firebaseHelper.getFirebaseAuth().getCurrentUser() != null){
            //zamyka aktywność
            finish();
            //otwiera aktywność ProfileActivity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //inicjalizacja widoków
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        progressDialog = new ProgressDialog(this);

        //odbieranie kliknięcia
        buttonSignIn.setOnClickListener(this);
        //textViewSignIn.setOnClickListener(this);
    }

    //metoda - logowanie
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //kontrola czy zostały wpisane e-mail i hasło
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Podaj adres e-mail",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Podaj hasło",Toast.LENGTH_LONG).show();
            return;
        }

        //jeżeli pola nie są puste to wyświetla się progressDialog - logowanie w trakcie
        progressDialog.setMessage("Logowanie, proszę czekać");
        progressDialog.show();

        //logowanie użytkownika
        firebaseHelper.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //jeżeli logowanie się powiodło uruchamia się ProfileActivity
                        if(task.isSuccessful()){
                            //uruchomienie ProfileActivity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(SignInActivity.this,"Ale muka - dane nieprawidłowe...",Toast.LENGTH_LONG).show();
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

        if(view == buttonSignUp){
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }
}
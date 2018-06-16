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

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseHelper firebaseHelper;

    //Widzety
    private Button buttonSignIn;
    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Polaczenie z baza
        firebaseHelper = new FirebaseHelper();

        //Jesli uzytkownik zalogowany przejdz do MainActivity
        if(firebaseHelper.getFirebaseAuth().getCurrentUser() != null){
            //Zamyka aktywność
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        progressDialog = new ProgressDialog(this);

        //Obsluga klikniecia na przycisk
        buttonSignIn.setOnClickListener(this);
    }

    //Zalogowanie uzytkownika
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            editTextEmail.setError("Podaj e-mail");
            return;
        }
        if(TextUtils.isEmpty(password)){
            editTextPassword.setError("Podaj hasło");
            return;
        }
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)) {
            //Wyswietla monit o procesie logowania
            progressDialog.setMessage("Logowanie, proszę czekać");
            progressDialog.show();
            //Logowanie uzytkownika do bazy
            firebaseHelper.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            //Jesli logowanie pomyslne - wyswietl MainActivity
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                //Jesli logowanie niepomyslne - Toast
                                Toast.makeText(SignInActivity.this, "Dane nieprawidłowe", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

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
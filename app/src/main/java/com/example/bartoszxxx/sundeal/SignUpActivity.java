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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        progressDialog = new ProgressDialog(this);

        buttonSignUp.setOnClickListener(this);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Jesli pole e-mail puste - Toast
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Podaj e-mail");
            return;
        }
        //Jesli pole hasla puste lub haslo zbyt krotkie - Toast
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            editTextPassword.setError("Hasło min. 6 znaków");
            return;
        }
        //Jesli dane poprawne rejestruj
        if (!TextUtils.isEmpty(email) && password.length() >= 6) {
            progressDialog.setMessage("Rejestracja, proszę czekać");
            progressDialog.show();
            //Tworzenie nowego uzytkownika
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Jesli proces pomyslny
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Użytkownik został zarejestrowany", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), NewMainActivity.class));
                            } else {
                                //Jesli proces niepomyslny
                                Toast.makeText(SignUpActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignUp) {
            registerUser();
        }
    }
}
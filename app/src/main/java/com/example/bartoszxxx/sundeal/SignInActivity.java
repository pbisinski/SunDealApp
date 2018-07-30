package com.example.bartoszxxx.sundeal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseHelper firebaseHelper;

    private Button buttonSignIn;
    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseHelper = new FirebaseHelper();

        if (firebaseHelper.getFirebaseAuth().getCurrentUser() != null) {
            finish();
            setPreferences();
            startActivity(new Intent(getApplicationContext(), NewMainActivity.class));
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        progressDialog = new ProgressDialog(this);

        buttonSignIn.setOnClickListener(this);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Podaj e-mail");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Podaj hasło");
            return;
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressDialog.setMessage("Logowanie, proszę czekać");
            progressDialog.show();
            firebaseHelper.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                finish();
                                setPreferences();
                                startActivity(new Intent(getApplicationContext(), NewMainActivity.class));
                            } else {
                                Toast.makeText(SignInActivity.this, "Błąd logowania", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void setPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            String name = firebaseHelper.getFirebaseAuth().getCurrentUser().getDisplayName();
            String password = editTextPassword.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            editor.putString("name", name);
            editor.putString("pass", password);
            editor.putString("email", email);
            editor.apply();
        } catch (NullPointerException e) {
            Log.e("FIREBASE", "setPreferences: user null");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            userLogin();
        }

        if (view == buttonSignUp) {
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
package com.example.bartoszxxx.sundeal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.buttonSignUp)
    Button BtnSignUp;
    @BindView(R.id.buttonSignIn)
    Button BtnSignIn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        progressDialog = new ProgressDialog(this);
        BtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }

    private void userLogin() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_email));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_password));
            return;
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressDialog.setMessage(getString(R.string.process_dialog));
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                finish();
                                setPreferences(email, password, firebaseAuth.getCurrentUser());
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void setPreferences(String email, String password, FirebaseUser user) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pass", password);
        editor.putString("email", email);
        editor.putString("name", user.getDisplayName());
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
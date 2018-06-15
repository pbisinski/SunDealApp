package com.example.bartoszxxx.sundeal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText editName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText loginPassword;
    private AuthCredential firebaseCred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Ustawienia");

        //Polaczenie z FireBase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        loginPassword = (EditText) findViewById(R.id.loginPassword);
    }

    public void setChanges(View view) {

        AuthCredential firebaseCred = EmailAuthProvider
                .getCredential(firebaseUser.getEmail(), loginPassword.getText().toString());

        firebaseUser.reauthenticate(firebaseCred)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!TextUtils.isEmpty(editName.getText().toString())) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(editName.getText().toString().trim())
                                    .build();
                            firebaseUser.updateProfile(profileUpdates);
                            Toast.makeText(SettingsActivity.this, "Nazwa", Toast.LENGTH_SHORT).show();
                            editName.setText("");
                        }

                        if (!TextUtils.isEmpty(editEmail.getText().toString())) {
                            firebaseUser.updateEmail(editEmail.getText().toString().trim());
                            Toast.makeText(SettingsActivity.this, "Email", Toast.LENGTH_SHORT).show();
                            editEmail.setText("");
                        }

                        if (!TextUtils.isEmpty(editPassword.getText().toString())) {
                            firebaseUser.updatePassword(editPassword.getText().toString());
                            Toast.makeText(SettingsActivity.this, "Hasło", Toast.LENGTH_SHORT).show();
                            editPassword.setText("");
                        }
                    }


                });
        firebaseUser.reauthenticate(firebaseCred).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, "Niepoprawne dane", Toast.LENGTH_SHORT).show();
            }
        });
        loginPassword.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

package com.example.bartoszxxx.sundeal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseHelper firebaseHelper;

    private EditText etName;
    private EditText etEmail;
    private EditText etNewPassword;
    private EditText etOldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseHelper = new FirebaseHelper();
        firebaseUser = firebaseHelper.getFirebaseUser();

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        Button SetChangesbtn = (Button) findViewById(R.id.SetChangesbtn);
        SetChangesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChanges();
            }
        });
    }

    public void setChanges() {
        if (TextUtils.isEmpty(etOldPassword.getText().toString())) {
            etOldPassword.setError("Pole nie może być puste");
        } else {
            //Refresh auth credential before changes
            AuthCredential firebaseCred = EmailAuthProvider
                    .getCredential(firebaseUser.getEmail(), etOldPassword.getText().toString());
            firebaseUser.reauthenticate(firebaseCred)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!TextUtils.isEmpty(etName.getText().toString())) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(etName.getText().toString().trim())
                                        .build();
                                firebaseUser.updateProfile(profileUpdates);
                                Toast.makeText(SettingsActivity.this, "Nazwa zmieniona!", Toast.LENGTH_SHORT).show();
                                etName.setText("");
                            }
                            if (!TextUtils.isEmpty(etEmail.getText().toString())) {
                                firebaseUser.updateEmail(etEmail.getText().toString().trim());
                                Toast.makeText(SettingsActivity.this, "Adres e-mail uaktualniony!", Toast.LENGTH_SHORT).show();
                                etEmail.setText("");
                            }
                            if (!TextUtils.isEmpty(etNewPassword.getText().toString())) {
                                firebaseUser.updatePassword(etNewPassword.getText().toString());
                                Toast.makeText(SettingsActivity.this, "Hasło zmienione!", Toast.LENGTH_SHORT).show();
                                etNewPassword.setText("");
                            }
                            etOldPassword.setText("");
                            etOldPassword.setError(null);
                        }
                    });

            firebaseUser.reauthenticate(firebaseCred).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, "Błąd logowania", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}

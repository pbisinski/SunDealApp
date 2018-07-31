package com.example.bartoszxxx.sundeal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.bartoszxxx.sundeal.Products.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    SharedPreferences.OnSharedPreferenceChangeListener spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, String s) {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            final String key = s;
            AuthCredential firebaseCred = EmailAuthProvider.getCredential(
                    user.getEmail(), sharedPreferences.getString("pass", "")
            );
            user.reauthenticate(firebaseCred).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    switch (key) {
                        case "name":
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(sharedPreferences.getString("name", ""))
                                    .build();
                            user.updateProfile(profileUpdates);
                            break;

                        case "email":
                            user.updateEmail(sharedPreferences.getString("email", ""));
                            break;

                        default:
                            break;
                    }
                }
            });

            user.reauthenticate(firebaseCred).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, "Błąd logowania", Toast.LENGTH_SHORT).show();
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener(spChanged);
    }
}

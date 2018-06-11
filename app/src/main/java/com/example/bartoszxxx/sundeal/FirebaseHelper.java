package com.example.bartoszxxx.sundeal;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

//Klasa utworzona na potrzeby kazdorazowego laczenia sie z baza
public class FirebaseHelper {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    public FirebaseHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        //Nazwa tabeli z produktami
        ref = database.getReference("Products");
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    //Chyba zbedne ale nie wiem
    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getRef() {
        return ref;
    }

}

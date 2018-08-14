package com.example.bartoszxxx.sundeal;

import com.google.firebase.database.Query;

public interface FirebaseHelper {
    String DATABASE_REFERENCE = "SunDealMain";
    String STORAGE_REFERENCE = "sundeal_photos";
    String ALL_PRODUCTS = null;

    void pushToDatabase(Product product);
    void getFromDatabase(Query query);

}

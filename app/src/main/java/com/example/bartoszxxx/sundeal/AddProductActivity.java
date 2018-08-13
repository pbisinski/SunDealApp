package com.example.bartoszxxx.sundeal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartoszxxx.sundeal.Products.ProductFirebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2;
    private static final int RC_PLACE_PICKER = 4;
    private final int MIN_ITEM_NAME_LENGTH = 4;

    private EditText ItemName, ItemDescription;
    private TextView PhotoFileName, LocationName;
    private LinearLayout detailsView;
    private RadioButton RdBtnGiveaway, RdBtnExchange;
    private RadioGroup RadioGroup;
    private String downloadUrl;
    private Uri selectedImageUri;
    private ProgressBar progressBar;
    private String itemName, itemLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ItemName = (TextInputEditText) findViewById(R.id.EtItemName);
        ItemDescription = (TextInputEditText) findViewById(R.id.EtItemDesc);
        RdBtnGiveaway = findViewById(R.id.RadioBtnGiveaway);
        RdBtnExchange = findViewById(R.id.RadioButtonExchange);
        RadioGroup = findViewById(R.id.RadioGroup);
        PhotoFileName = findViewById(R.id.PhotoFileName);
        LocationName = findViewById(R.id.LocationName);
        progressBar = findViewById(R.id.determinateBar);
        detailsView = findViewById(R.id.DetailsView);

        LinearLayout PhotoPickerButton = findViewById(R.id.PhotoPickerBtn);
        LinearLayout PlacePickerButton = findViewById(R.id.PlacePickerBtn);
        PhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
        PlacePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddProductActivity.this), RC_PLACE_PICKER);
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    Log.e("AddProductActivity", e.toString());
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemName = ItemName.getText().toString().trim();
                if (!RdBtnGiveaway.isChecked() && !RdBtnExchange.isChecked() || itemName.length() < MIN_ITEM_NAME_LENGTH) {
                    Toast.makeText(AddProductActivity.this, R.string.toast_missing_data, Toast.LENGTH_SHORT).show();
                } else {
                    addProduct();
                }
            }
        });

        ItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ItemName.getText().toString().length() < MIN_ITEM_NAME_LENGTH) {
                    ItemName.setError(getString(R.string.titlename_too_short));
                } else {
                    ItemName.setError(null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                selectedImageUri = data.getData();
                String fileName = DocumentFile.fromSingleUri(this, selectedImageUri).getName();
                PhotoFileName.setText(fileName);
                findViewById(R.id.PhotoTitle).setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == RC_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                try {
                    String placeName = place.getAddress().toString();
                    LocationName.setText(placeName);
                    itemLocation = placeName;
                    findViewById(R.id.LocationTitle).setVisibility(View.VISIBLE);
                } catch (NullPointerException e) {
                    Log.e("PLACE_PICKER", e.toString());
                }
            }
        }
    }

    public void addProductWithPhoto() {
        progressBar.setVisibility(View.VISIBLE);
        detailsView.setVisibility(View.INVISIBLE);
        String uniqueID = UUID.randomUUID().toString();
        final StorageReference photoRef = FirebaseStorage.getInstance().getReference("sundeal_photos").child(uniqueID);
        UploadTask uploadTask = photoRef.putFile(selectedImageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult().toString();
                    addProductToDatabase();
                }
            }
        });
    }

    private void setClear() {
        ItemName.setText("");
        ItemDescription.setText("");
        ItemName.clearFocus();
        ItemDescription.clearFocus();
        PhotoFileName.setText("");
        LocationName.setText("");
        RadioGroup.clearCheck();
        ItemName.setError(null);
        detailsView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void addProduct() {
        if (selectedImageUri != null) {
            addProductWithPhoto();
        } else {
            addProductToDatabase();
        }
    }

    private void addProductToDatabase() {
        String itemDescription = ItemDescription.getText().toString().trim();
        if (itemDescription.isEmpty()) {
            itemDescription = getString(R.string.description_default);
        }
        if (itemLocation == null) {
            itemLocation = getString(R.string.location_default);
        }
        Boolean itemGiveaway = RdBtnGiveaway.isChecked();

        if (!RdBtnGiveaway.isChecked() && !RdBtnExchange.isChecked() || itemName.length() < MIN_ITEM_NAME_LENGTH) {
            Toast.makeText(this, R.string.toast_missing_data, Toast.LENGTH_SHORT).show();
        } else {
            String itemOwner = PreferenceManager.getDefaultSharedPreferences(this).getString("email", "no-email");
            DatabaseReference database = FirebaseDatabase.getInstance().getReference(FirebaseHelper.DATABASE_REFERENCE);
            String key = database.push().getKey();
            ProductFirebase productFirebase = new ProductFirebase(
                    itemOwner,
                    itemName,
                    itemName.toLowerCase().trim(),
                    itemDescription,
                    itemLocation,
                    itemGiveaway,
                    key,
                    downloadUrl);
            database.child(key).setValue(productFirebase);
            Toast.makeText(this, getString(R.string.product_addition_success) + productFirebase.getTitle(), Toast.LENGTH_SHORT).show();
            setClear();
        }
    }
}
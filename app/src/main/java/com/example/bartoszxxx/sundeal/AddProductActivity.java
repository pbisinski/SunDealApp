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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductActivity extends AppCompatActivity implements FirebaseHelper {

    private static final int RC_PHOTO_PICKER = 2;
    private static final int RC_PLACE_PICKER = 3;
    private final int MIN_ITEM_NAME_LENGTH = 4;

    @BindView(R.id.EtItemName)
    TextInputEditText ItemName;
    @BindView(R.id.EtItemDesc)
    TextInputEditText ItemDescription;
    @BindView(R.id.RadioBtnGiveaway)
    RadioButton RdBtnGiveaway;
    @BindView(R.id.RadioButtonExchange)
    RadioButton RdBtnExchange;
    @BindView(R.id.RadioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.PhotoFileName)
    TextView PhotoFileName;
    @BindView(R.id.LocationName)
    TextView ItemLocation;
    @BindView(R.id.DetailsView)
    LinearLayout detailsView;
    @BindView(R.id.PhotoPickerBtn)
    LinearLayout PhotoPickerBtn;
    @BindView(R.id.PlacePickerBtn)
    LinearLayout PlacePickerBtn;
    @BindView(R.id.determinateBar)
    ProgressBar progressBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.PhotoTitle)
    TextView PhotoFileTitle;
    @BindView(R.id.LocationTitle)
    TextView LocationNameTitle;

    private DatabaseReference mDatabase;
    private Uri selectedImageUri;
    private String itemLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE);

        PhotoPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
        PlacePickerBtn.setOnClickListener(new View.OnClickListener() {
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String itemName = ItemName.getText().toString().trim();
                final String itemDescription = ItemDescription.getText().toString().trim();
                final String itemOwner = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("email", "no-email");
                final Boolean itemGiveaway = RdBtnGiveaway.isChecked();

                if (itemName.length() < MIN_ITEM_NAME_LENGTH || itemDescription.isEmpty() || itemLocation == null || selectedImageUri == null) {
                    if (itemName.length() < MIN_ITEM_NAME_LENGTH) {
                        ItemName.setError(getString(R.string.titlename_too_short));
                    }
                    if (itemDescription.isEmpty()) {
                        ItemDescription.setError(getString(R.string.description_too_short));
                    }
                    if (itemLocation == null || selectedImageUri == null) {
                        Toast.makeText(AddProductActivity.this, getString(R.string.toast_missing_data), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ItemName.clearFocus();
                    ItemDescription.clearFocus();
                    detailsView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    asyncSubmitProduct(itemOwner, itemName, itemDescription, itemLocation, itemGiveaway);
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
                if (s.length() < MIN_ITEM_NAME_LENGTH) {
                    ItemName.setError(getString(R.string.titlename_too_short));
                } else {
                    ItemName.setError(null);
                }
            }
        });
        ItemDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    ItemDescription.setError(getString(R.string.description_too_short));
                } else {
                    ItemDescription.setError(null);
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
                PhotoFileTitle.setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == RC_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                try {
                    String placeName =  place.getAddress().toString();
                    ItemLocation.setText(placeName);
                    itemLocation = placeName;
                    LocationNameTitle.setVisibility(View.VISIBLE);
                } catch (NullPointerException e) {
                    Log.e("PLACE_PICKER", e.toString());
                }
            }
        }
    }

    private void clearFields() {
        ItemName.setText("");
        ItemDescription.setText("");
        PhotoFileName.setText("");
        ItemLocation.setText("");
        ItemName.setError(null);
        ItemDescription.setError(null);
        detailsView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        LocationNameTitle.setVisibility(View.INVISIBLE);
        PhotoFileTitle.setVisibility(View.INVISIBLE);
        radioGroup.clearCheck();
        itemLocation = null;
    }

    private void asyncSubmitProduct(final String itemOwner, final String itemName, final String itemDescription, final String itemLocation, final Boolean itemGiveaway) {
        String uniqueID = UUID.randomUUID().toString();
        final StorageReference photoRef = FirebaseStorage.getInstance().getReference(STORAGE_REFERENCE).child(uniqueID);
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
                    final String downloadUrl = task.getResult().toString();
                    final String key = mDatabase.push().getKey();
                    Product product = new Product(itemOwner, itemName, itemName.toLowerCase(), itemDescription, itemLocation, itemGiveaway, downloadUrl);
                    product.setKey(key);
                    mDatabase.child(key).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddProductActivity.this, getString(R.string.product_addition_success), Toast.LENGTH_SHORT).show();
                            clearFields();
                        }
                    });
                }
            }
        });
    }

}
package com.example.bartoszxxx.sundeal;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.provider.DocumentFile;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bartoszxxx.sundeal.Products.ProductFirebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int RC_PHOTO_PICKER = 2;
    private final int MIN_ITEM_NAME_LENGTH = 4;
    Geocoder geocoder;
    private FirebaseAuth firebaseAuth;
    private NestedScrollView nScrollView;
    private Marker marker;
    private GoogleMap mMap;
    private EditText ItemName, ItemDescription;
    private TextView ItemLocation;
    private RadioButton RdBtnGiveaway, RdBtnExchange;
    private RadioGroup RadioGroup;
    private Button PhotoPickerButton;
    private String downloadUrl;
    private Uri selectedImageUri;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        nScrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        ItemLocation = (TextView) findViewById(R.id.TvLocation);
        ItemName = (TextInputEditText) findViewById(R.id.EtItemName);
        ItemDescription = (TextInputEditText) findViewById(R.id.EtItemDesc);
        RdBtnGiveaway = (RadioButton) findViewById(R.id.RadioBtnGiveaway);
        RdBtnExchange = (RadioButton) findViewById(R.id.RadioButtonExchange);
        RadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        PhotoPickerButton = (Button) findViewById(R.id.PhotoPickerBtn);
        progressBar = findViewById(R.id.determinateBar);

        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                nScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        PhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImageUri != null) {
                    uploadPhoto();
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
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            String fileName = DocumentFile.fromSingleUri(this, selectedImageUri).getName();
            PhotoPickerButton.setText(fileName);
        }
    }

    public void uploadPhoto() {
        progressBar.setVisibility(View.VISIBLE);
        String uniqueID = UUID.randomUUID().toString();
        final StorageReference photoRef = FirebaseStorage.getInstance().getReference("sundeal_photos").child(uniqueID);
        UploadTask uploadTask = photoRef.putFile(selectedImageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult().toString();
                    addProduct();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng warsaw = new LatLng(52.229676, 21.012229);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw, 11));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address = addresses.get(0);

                if (address != null) {
                    ItemLocation.setText(address.getAddressLine(0));
                }

                if (marker != null) {
                    marker.remove();
                }

                marker = mMap.addMarker((new MarkerOptions().position(point).title(address.getAddressLine(0))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
            }
        });
    }

    private void setClear() {
        ItemName.setText("");
        ItemDescription.setText("");
        ItemName.clearFocus();
        ItemDescription.clearFocus();
        RadioGroup.clearCheck();
        ItemName.setError(null);
        PhotoPickerButton.setText(R.string.add_photo);
        progressBar.setVisibility(View.INVISIBLE);

    }

    private void addProduct() {
        String itemName = ItemName.getText().toString().trim();
        String itemDescription = ItemDescription.getText().toString().trim();
        String itemLocation;
        Boolean itemGiveaway = RdBtnGiveaway.isChecked();

        if (marker == null) {
            itemLocation = getResources().getString(R.string.location_default);
        } else {
            itemLocation = ItemLocation.getText().toString();
        }
        if (!RdBtnGiveaway.isChecked() && !RdBtnExchange.isChecked() || itemName.length() < MIN_ITEM_NAME_LENGTH) {
            Toast.makeText(this, R.string.toast_missing_data, Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference(FirebaseHelper.DATABASE_REFERENCE);
            String id_key = database.push().getKey();
            ProductFirebase productFirebase = new ProductFirebase(
                    firebaseAuth.getCurrentUser().getEmail(),
                    itemName,
                    itemName.toLowerCase(),
                    itemDescription,
                    itemLocation,
                    itemGiveaway,
                    id_key,
                    downloadUrl);
            database.child(id_key).setValue(productFirebase);
            Toast.makeText(this, "Pomyślnie dodano: " + productFirebase.getTitle(), Toast.LENGTH_SHORT).show();
            setClear();
        }
    }
}

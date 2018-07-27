package com.example.bartoszxxx.sundeal;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity implements OnMapReadyCallback {

    Geocoder geocoder;
    private FirebaseHelper firebaseHelper;
    private ScrollView mScrollView;
    private LatLng latLng;
    private Marker marker;
    private GoogleMap mMap;
    private EditText item, description;
    private TextView location;
    private Switch oddam, zamienie;
    private RelativeLayout relativeSwitches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        item = (EditText) findViewById(R.id.item);
        description = (EditText) findViewById(R.id.description);
        location = (TextView) findViewById(R.id.location);
        oddam = (Switch) findViewById(R.id.switch1);
        zamienie = (Switch) findViewById(R.id.switch2);
        relativeSwitches = (RelativeLayout) findViewById(R.id.relativeSwitches);
        Button BtnInsert = (Button) findViewById(R.id.BtnInsert);
        BtnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct();
            }
        });

        firebaseHelper = new FirebaseHelper();

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);

        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        item.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (item.getText().toString().length() < 3) {
                    item.setError("Zbyt krótki tytuł");
                } else {
                    item.setError(null);
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
                latLng = point;

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address = addresses.get(0);

                if (address != null) {
                    location.setText(address.getAddressLine(0));
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
        item.setText("");
        description.setText("");
        oddam.setChecked(false);
        zamienie.setChecked(false);
    }

    public void AddProduct() {

        String itemValue = item.getText().toString().trim();
        String descriptionValue = description.getText().toString().trim();
        String locationValue = location.getText().toString();

        if (TextUtils.isEmpty(itemValue)) {
            Toast.makeText(this, "Tytuł nie może być pusty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!oddam.isChecked() && !zamienie.isChecked()) {
            Toast.makeText(this, "Wybierz rodzaj transakcji", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oddam.isChecked() && zamienie.isChecked()) {
            Toast.makeText(this, "Możesz wybrać tylko jeden rodzaj transakcji", Toast.LENGTH_SHORT).show();
            return;
        }
        if (marker == null) {
            locationValue = getResources().getString(R.string.location_default);
        }

        String id = firebaseHelper.getRef().push().getKey();
        ProductFirebase productFirebase = new ProductFirebase(
                firebaseHelper.getFirebaseUser().getEmail(),
                itemValue,
                itemValue.toLowerCase(),
                descriptionValue,
                locationValue,
                oddam.isChecked(),
                zamienie.isChecked(),
                id);
        firebaseHelper.getRef().child(id).setValue(productFirebase);
        Snackbar.make(mScrollView, "Pomyślnie dodano: " + productFirebase.getItem(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        setClear();
    }
}
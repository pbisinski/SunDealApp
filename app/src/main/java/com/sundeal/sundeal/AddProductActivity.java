package com.sundeal.sundeal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class AddProductActivity extends AppCompatActivity {

    private final static int RC_PHOTO_PICKER = 2;
    private final static int MIN_NAME_LEN = 4;
    private final static int MAX_NAME_LEN = 20;

    @BindView(R.id.basic_toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.name_edit_text)
    ExtendedEditText nameEditText;
    @BindView(R.id.name_edit_box)
    TextFieldBoxes nameEditBox;
    @BindView(R.id.description_edit_text)
    ExtendedEditText descriptionEditText;
    @BindView(R.id.photo_title_name)
    TextView photoTitleName;
    @BindView(R.id.add_btn)
    Button addBtn;
    @BindView(R.id.categories_spinner)
    Spinner spinner;
    @BindView(R.id.photo_picker_btn)
    LinearLayout photoPickerBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private DatabaseReference mDatabase;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference(FirebaseHelper.DATABASE_REFERENCE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(0, R.anim.exit_fade);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                int category = spinner.getSelectedItemPosition();
                if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(AddProductActivity.this, "Brak kategorii", Toast.LENGTH_SHORT).show();
                } else {
                    if (name.length() >= MIN_NAME_LEN && name.length() <= MAX_NAME_LEN && !description.isEmpty() && category != 0 && selectedImageUri != null) {
                        asyncSubmitProduct(name, description, category);
                        progressBar.setVisibility(View.VISIBLE);
                        addBtn.setClickable(false);
                    }
                }
            }
        });
        photoPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < MIN_NAME_LEN) {
                    nameEditBox.setError("Nazwa zbyt krótka", false);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK && data != null) {
                selectedImageUri = data.getData();
                try {
                    photoTitleName.setText(DocumentFile.fromSingleUri(getApplicationContext(), selectedImageUri).getName());
                } catch (NullPointerException e) {
                    Log.e("PhotoPicker", e.toString());
                }

            }
        }
    }

    private void clearFields() {
        String empty = "";
        nameEditText.setText(empty);
        descriptionEditText.setText(empty);
        photoTitleName.setText(empty);
        selectedImageUri = null;
        spinner.setSelection(0);
        nameEditBox.removeError();
    }

    private void asyncSubmitProduct(final String name, final String description, final int category) {
        String uniqueID = UUID.randomUUID().toString();
        final StorageReference photoRef = FirebaseStorage.getInstance().getReference("sundeal-photos").child(uniqueID);
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
                    Item item = new Item(name, description, "address", "owner", category, downloadUrl, name.toLowerCase(), key);
                    mDatabase.child(key).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddProductActivity.this, "Przedmiot dodano", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            addBtn.setClickable(true);
                            clearFields();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProductActivity.this, "Błąd dodawania do bazy: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.exit_fade);
    }

}
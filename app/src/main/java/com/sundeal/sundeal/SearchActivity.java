package com.sundeal.sundeal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchView)
    EditText searchView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.recyclerInfo)
    TextView recyclerInfo;

    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference(FirebaseHelper.DATABASE_REFERENCE);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            int category = b.getInt("category");
            makeQuery(category);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            searchView.setHint(b.getString("title"));
        }

        searchView.requestFocus();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(0, R.anim.exit_fade);
            }
        });
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    makeQuery(textView.getText().toString());
                }
                return true;
            }
        });
    }

    private void makeQuery(final int category) {
        Query ref = mDatabase.orderByChild("category").equalTo(category);
        getFromDatabase(ref);
    }

    private void makeQuery(final String query) {
        Query ref = mDatabase.orderByChild("name").startAt(query).endAt(query + "\uf8ff");
        getFromDatabase(ref);
    }

    private void getFromDatabase(final Query databaseRef) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAdapter.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Item item = childDataSnapshot.getValue(Item.class);
                        mAdapter.add(item);
                    } catch (NullPointerException e) {
                        Log.e("ProductList", e.toString());
                    }
                }
                mAdapter.notifyDataSetChanged();
                updateRecyclerInfo(mAdapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void updateRecyclerInfo(final int resultCount) {
        String resultText;
        if (resultCount <= 0) {
            resultText = "Nie znaleziono wyników";
        } else {
            resultText = "Znalezione wyniki: " + resultCount;
        }
        recyclerInfo.setText(resultText);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.exit_fade);
    }

}

package com.example.evolutionfitnessasd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends BaseActivity {
    RecyclerView mRecyclerView;
    List<ContactsData> mContactList;
    ContactsData mContactData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mRecyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(Contacts.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mContactList = new ArrayList<>();
        mContactData = new ContactsData("Alberto", getString(R.string.description_alberto),
                R.drawable.alberto);
        mContactList.add(mContactData);
        mContactData = new ContactsData("Alessio", getString(R.string.description_alessio),
                R.drawable.alessio);
        mContactList.add(mContactData);
        mContactData = new ContactsData("Gianluigi", getString(R.string.description_gianluigi),
                R.drawable.gianluigi);
        mContactList.add(mContactData);
        mContactData = new ContactsData("Rosa", getString(R.string.description_rosa),
                R.drawable.rosa);
        mContactList.add(mContactData);
        mContactData = new ContactsData("Furio", getString(R.string.description_furio),
                R.drawable.furio);
        mContactList.add(mContactData);

        MyAdapterContacts myAdapter = new MyAdapterContacts(Contacts.this, mContactList);
        mRecyclerView.setAdapter(myAdapter);

    }
}
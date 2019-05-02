package com.example.evolutionfitnessasd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.core.view.View;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class UploadSchedule extends AppCompatActivity{
    private DatabaseReference myRef;
    private static final String TAG = "Upload Schedule";
    private Iterable<DataSnapshot> uid;
    private String names;
    private String surnames;
    private ArrayList<String> userList;
    private ArrayAdapter<String> userAdapter;
    private ListView list;
    private EditText filterS;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_schedule);
        userList = new ArrayList<>();
        list = findViewById(R.id.list_users);
        filterS= (EditText) findViewById(R.id.searchFilterS);
        myRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://evolutionfitness-42b6e.firebaseio.com/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                uid = dataSnapshot.child("Utenti").getChildren();
                userAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userList);
                list.setAdapter(userAdapter);
                list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        shared.setUID(parent.getItemAtPosition(position).toString());
                        System.out.println(shared.getUID());
                        Intent upload = new Intent(UploadSchedule.this, RealScheduleUpload.class);
                        startActivity(upload);
                    }
                });
                userAdapter.clear();
                while(uid.iterator().hasNext()) {
                    DataSnapshot snap=uid.iterator().next();
                    if (snap.hasChild("Cognome")) {
                        names = snap.child("Nome").getValue().toString();
                        surnames = snap.child("Cognome").getValue().toString();
                        userList.add(names+" "+surnames);
                    } else{
                        String names=snap.child("Nome").getValue().toString();
                        userList.add(names);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        filterS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (UploadSchedule.this).userAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}

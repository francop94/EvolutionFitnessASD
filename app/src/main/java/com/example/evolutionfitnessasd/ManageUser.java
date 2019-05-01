package com.example.evolutionfitnessasd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ManageUser extends AppCompatActivity{
    private DatabaseReference myRef;
    private static final String TAG = "Manage User";
    private Iterable<DataSnapshot> uid;
    private String names;
    private String surnames;
    private ArrayList<String> userList;
    private ArrayAdapter<String> userAdapter;
    private ListView list;
    private String chosenName;
    private ArrayList<String> UID;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_schedule);
        userList = new ArrayList<>();
        UID = new ArrayList<>();
        list = findViewById(R.id.list_users);
        myRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://evolutionfitness-42b6e.firebaseio.com/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                uid = dataSnapshot.child("Users").getChildren();
                userAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userList);
                list.setAdapter(userAdapter);
                list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        chosenName=parent.getItemAtPosition(position).toString();
                        if(userList.get(position).equals(chosenName)){
                            shared.setUIDMan(UID.get(position));
                        }
                        Intent manage = new Intent(ManageUser.this, RealManageUser.class);
                        startActivity(manage);
                    }
                });
                userAdapter.clear();
                while(uid.iterator().hasNext()) {
                    DataSnapshot snap=uid.iterator().next();
                    if(snap.getKey()!=null) {
                        UID.add(snap.getKey());
                    }
                    if (snap.hasChild("Surname")) {
                        try {
                            names = snap.child("Name").getValue().toString();
                            surnames = snap.child("Surname").getValue().toString();
                            userList.add(names + " " + surnames);
                        } catch(NullPointerException e){
                            Log.d("MANAGE USER: ", e.getLocalizedMessage());
                        }
                    } else {
                        try {
                            String names = snap.child("Name").getValue().toString();
                            userList.add(names);
                        } catch(NullPointerException e){
                            Log.d("MANAGE USER: ", e.getLocalizedMessage());
                        }

                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}

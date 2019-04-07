package com.example.evolutionfitnessasd;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.*;

import java.io.FileNotFoundException;
import java.util.List;

public class TrainingSchedule extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference myRef;
    private static final String TAG = "Training Schedule";
    private String FROM, NOME, COGNOME;
    private String uid;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_schedule);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        final RequestOptions requestOptions = new RequestOptions();
        //requestOptions.placeholder(R.drawable.ic_placeholder);
        //requestOptions.error(R.drawable.ic_error);
        myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://evolutionfitness-42b6e.firebaseio.com/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FROM = dataSnapshot.child("Users").child(uid).child("Email").getValue().toString();
                NOME = dataSnapshot.child("Users").child(uid).child("Name").getValue().toString();
                if(dataSnapshot.child("Users").child(uid).hasChild("Surname")) {
                    COGNOME = dataSnapshot.child("Users").child(uid).child("Surname").getValue().toString();
                }
                final ImageView imageView = findViewById(R.id.imageView);

                if(COGNOME!=null) {

                    try {
                        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://evolutionfitness-42b6e.appspot.com/" + NOME + " " + COGNOME + "/schedule.jpg");

                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // getting image uri and converting into string
                                String fileUrl;
                                Uri downloadUrl = uri;
                                fileUrl = downloadUrl.toString();
                                Glide.with(getApplicationContext()).load(fileUrl).apply(requestOptions).into(imageView);


                            }
                        });
                    } catch (Exception e){
                        Toast.makeText(TrainingSchedule.this, "Schedule not present, please contact the personal trainer to get a new one", Toast.LENGTH_SHORT).show();
                        Log.d("TRAINING SCHEDULE", e.getLocalizedMessage());
                    }
                }
                else {
                    try{
                    mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://evolutionfitness-42b6e.appspot.com/" + NOME + "/schedule.jpg");
                    mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // getting image uri and converting into string
                            String fileUrl;
                            Uri downloadUrl = uri;
                            fileUrl = downloadUrl.toString();
                            Glide.with(getApplicationContext()).load(fileUrl).apply(requestOptions).into(imageView);
                            imageView.setOnTouchListener(new Touch());

                        }
                    });
                }catch (Exception e){
                        Toast.makeText(TrainingSchedule.this, "Schedule not present, please contact the personal trainer to get a new one", Toast.LENGTH_SHORT).show();
                        Log.d("TRAINING SCHEDULE", e.getLocalizedMessage());
                    }
                }
            }



            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // ImageView in your Activity

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(FROM);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's a download in progress, save the reference so you can query it later
        if (mStorageRef != null) {
            outState.putString("reference", mStorageRef.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was a download in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all DownloadTasks under this StorageReference (in this example, there should be one)
        List<FileDownloadTask> tasks = mStorageRef.getActiveDownloadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the download
            FileDownloadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot state) {
                    // Success!
                    // ...
                }
            });
        }
    }
    protected void sendEmail(String from) {
        Log.i("Send email", "");
        String[] TO = {"chicco.1994@hotmail.it"};
        String[] FROM = {from};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_REFERRER, FROM);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Schedule request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please send me my schedule");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(TrainingSchedule.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}

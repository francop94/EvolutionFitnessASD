package com.example.evolutionfitnessasd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleSignInAccount account;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private static final String TAG = "Navigation";
    private TextView textname;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://evolutionfitness-42b6e.firebaseio.com/");

        mAuth = FirebaseAuth.getInstance();
        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());// [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if ((mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified())||account!=null) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_navigation);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);

            uid = mAuth.getUid();
            String email = mAuth.getCurrentUser().getEmail();
            String name = shared.getNOME();
            String surname = shared.getCOGNOME();
            myRef.child("Users").child(uid).child("Email").setValue(email);
            if(name!=null && surname!=null) {
                myRef.child("Users").child(uid).child("Name").setValue(name);
                myRef.child("Users").child(uid).child("Surname").setValue(surname);
            } else if(account!=null){
                myRef.child("Users").child(uid).child("Name").setValue(account.getDisplayName());
            }

            textname = headerView.findViewById(R.id.textName);
            if (mAuth.getCurrentUser() != null||account!=null) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String name, surname;
                        if(dataSnapshot.child("Users").child(uid).hasChild("Surname")) {
                            name = dataSnapshot.child("Users").child(uid).child("Name").getValue().toString();
                            surname = dataSnapshot.child("Users").child(uid).child("Surname").getValue().toString();

                            if (name != null && surname != null) {
                                textname.setText("Welcome, " + name + " " + surname);
                            }
                        }else{
                            if(dataSnapshot.child("Users").child(uid).hasChild("Name")) {
                                name = dataSnapshot.child("Users").child(uid).child("Name").getValue().toString();
                                if (name != null) {
                                    textname.setText("Welcome, " + name);
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

            }else if(mAuth.getCurrentUser()!=null && !mAuth.getCurrentUser().isEmailVerified()){
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_navigation);
                Toast.makeText(Navigation.this, "To enter the app you have to verify your email address first!",
                    Toast.LENGTH_LONG).show();
                finish();
                Intent login = new Intent(Navigation.this,MainActivity.class);
                startActivity(login);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //revokeAccess(); //da togliere
            finish();
            //super.onBackPressed();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent user_profile = new Intent(Navigation.this, Contacts.class);
            startActivity(user_profile);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //finish();
            Intent training_schedule = new Intent(Navigation.this,TrainingSchedule.class);
            startActivity(training_schedule);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent training_videos = new Intent(Navigation.this,MuscleGroups.class);
            startActivity(training_videos);

        } else if (id == R.id.maps) {
            Intent maps = new Intent(Navigation.this,MapsActivity.class);
            startActivity(maps);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.contacts) {

        }
        else if (id == R.id.disconnect){
            revokeAccess();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void revokeAccess() {
        // Firebase sign out
        if(mAuth!=null) {
            mAuth.signOut();
        }
        // Google revoke access
        if (mGoogleSignInClient != null){
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                            Intent menu = new Intent(Navigation.this, MainActivity.class);
                            startActivity(menu);
                        }
                    });
    }
    super.onBackPressed();
    }

}

package com.example.evolutionfitnessasd;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
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
    private NotificationChannel channel;
    private NotificationManager mNotificationManager ;
    private NotificationCompat.Builder mBuilder;
    private Intent intentNotification;
    private PendingIntent pi;
    private Thread SCADENZA_WEEK_ABB, SCADENZA_WEEK_ANN, SCADENZA_WEEK_CERT, SCADENZA_DAY_ABB, SCADENZA_DAY_ANN, SCADENZA_DAY_CERT;

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

            //THREADS
            createChannelWeekAbb("Channel0","Channel_WEEK_ABB");
            createNotification();
            System.out.println("CHANNEL 0 CREATO-------------------------------------");
            SCADENZA_WEEK_ABB= new Thread(new NOTIFICATION_Thread_WeekAbb(channel,mNotificationManager,mBuilder,pi));

            createChannelWeekAnn("Channel1","Channel_WEEK_ANN");
            createNotification();
            System.out.println("CHANNEL 1 CREATO-------------------------------------");
            SCADENZA_WEEK_ANN= new Thread(new NOTIFICATION_Thread_WeekAnn(channel,mNotificationManager,mBuilder,pi));

            createChannelWeekCert("Channel2","Channel_WEEK_CERT");
            createNotification();
            System.out.println("CHANNEL 2 CREATO-------------------------------------");
            SCADENZA_WEEK_CERT= new Thread(new NOTIFICATION_Thread_WeekCert(channel,mNotificationManager,mBuilder,pi));

            createChannelDayAbb("Channel3","Channel_DAY_ABB");
            createNotification();
            System.out.println("CHANNEL 3 CREATO-------------------------------------");
            SCADENZA_DAY_ABB= new Thread(new NOTIFICATION_Thread_DayAbb(channel,mNotificationManager,mBuilder,pi));

            createChannelDayAnn("Channel4","Channel_DAY_ANN");
            createNotification();
            System.out.println("CHANNEL 4 CREATO-------------------------------------");
            SCADENZA_DAY_ANN= new Thread(new NOTIFICATION_Thread_DayAnn(channel,mNotificationManager,mBuilder,pi));

            createChannelDayCert("Channel5","Channel_DAY_CERT");
            createNotification();
            System.out.println("CHANNEL 5 CREATO-------------------------------------");
            SCADENZA_DAY_CERT= new Thread(new NOTIFICATION_Thread_DayCert(channel,mNotificationManager,mBuilder,pi));

            SCADENZA_WEEK_ABB.start();
            SCADENZA_WEEK_ANN.start();
            SCADENZA_WEEK_CERT.start();
            SCADENZA_DAY_ABB.start();
            SCADENZA_DAY_ANN.start();
            SCADENZA_DAY_CERT.start();

            uid = mAuth.getUid();
            String email = mAuth.getCurrentUser().getEmail();
            String name = shared.getNOME();
            String surname = shared.getCOGNOME();
            myRef.child("Utenti").child(uid).child("Email").setValue(email);
            if(name!=null && surname!=null) {
                myRef.child("Utenti").child(uid).child("Nome").setValue(name);
                myRef.child("Utenti").child(uid).child("Cognome").setValue(surname);
            } else if(account!=null){
                myRef.child("Utenti").child(uid).child("Nome").setValue(account.getDisplayName());
            }

            textname = headerView.findViewById(R.id.textName);
            if (mAuth.getCurrentUser() != null||account!=null) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String name, surname;
                        if(dataSnapshot.child("Utenti").child(uid).hasChild("Cognome")) {
                            name = dataSnapshot.child("Utenti").child(uid).child("Nome").getValue().toString();
                            surname = dataSnapshot.child("Utenti").child(uid).child("Cognome").getValue().toString();

                            if (name != null && surname != null) {
                                textname.setText("Ciao, " + name + " " + surname);
                            }
                        }else{
                            if(dataSnapshot.child("Utenti").child(uid).hasChild("Nome")) {
                                name = dataSnapshot.child("Utenti").child(uid).child("Nome").getValue().toString();
                                if (name != null) {
                                    textname.setText("Ciao, " + name);
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
                Toast.makeText(Navigation.this, "Per entrare nell'app devi prima verificare la tua email!",
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
        /*if (id == R.id.action_settings) {
        }*/

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
            Intent calendar = new Intent(Navigation.this,TrainingCalendar.class);
            startActivity(calendar);
        } else if (id == R.id.nav_slideshow) {
            Intent training_videos = new Intent(Navigation.this,MuscleGroups.class);
            startActivity(training_videos);

        } else if (id == R.id.maps) {
            Intent maps = new Intent(Navigation.this,MapsActivity.class);
            startActivity(maps);

        } else if (id == R.id.nav_share) {
            Intent share = new Intent(Navigation.this,ShareImageUser.class);
            startActivity(share);

        } else if (id == R.id.contacts) {
            Intent staff = new Intent(Navigation.this,Contacts.class);
            startActivity(staff);
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
                        }
                    });
    }
    super.onBackPressed();
    }
    public void createChannelWeekAbb(String title, String content) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channel = new NotificationChannel("0", "Scadenza abbonamento tra una settimana", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canale per scadenza abbonamento tra una settimana");
        mNotificationManager.createNotificationChannel(channel);
        System.out.println("Canale creato");
    }
    public void createChannelWeekAnn(String title, String content) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channel = new NotificationChannel("1", "Scadenza quota annuale tra una settimana", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canale per scadenza quota annuale tra una settimana");
        mNotificationManager.createNotificationChannel(channel);
        System.out.println("Canale creato");
    }
    public void createChannelWeekCert(String title, String content) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channel = new NotificationChannel("2", "Scadenza certificato medico tra una settimana", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Canale per scadenza certificato medico tra una settimana");
        mNotificationManager.createNotificationChannel(channel);
        System.out.println("Canale creato");
    }
    public void createChannelDayAbb(String title, String content) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channel = new NotificationChannel("3", "Scadenza abbonamento tra un giorno", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Canale per scadenza abbonamento tra un giorno");
        mNotificationManager.createNotificationChannel(channel);
        System.out.println("Canale creato");
    }
    public void createChannelDayAnn(String title, String content) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channel = new NotificationChannel("4", "Scadenza quota annuale tra un giorno", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Canale per scadenza quota annuale tra un giorno");
        mNotificationManager.createNotificationChannel(channel);
        System.out.println("Canale creato");
    }
    public void createChannelDayCert(String title, String content) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channel = new NotificationChannel("5", "Scadenza certficato medico tra un giorno", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Canale per scadenza certificato medico tra un giorno");
        mNotificationManager.createNotificationChannel(channel);
        System.out.println("Canale creato");
    }


    public void createNotification(){


        System.out.println("ENTRATO NEL BUILDER");
        mBuilder = new NotificationCompat.Builder(this,channel.getId() );
        intentNotification = new Intent(getApplicationContext(), Navigation.class);
        pi = PendingIntent.getActivity(this, 0, intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);



    }

}

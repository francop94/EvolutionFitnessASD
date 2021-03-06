package com.example.evolutionfitnessasd;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothSocket;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NOTIFICATION_Thread_WeekCert extends Application implements Runnable{
        private NotificationChannel channel;
        private NotificationManager mNotificationManager ;
        private NotificationCompat.Builder mBuilder;
        private PendingIntent pendingIntent;
        private DatabaseReference mDataBase;
        private String UID, currentDate,currentCert;
        private FirebaseUser user;
        private Calendar myCalendar, certC;
        private boolean sendedCert;
        private String myFormat;
        private SimpleDateFormat sdf;
        private String TAG="NOTIFICATION THREAD";
        private String certMed;
        private Date newCertDate;
        public NOTIFICATION_Thread_WeekCert (NotificationChannel chan, NotificationManager man, NotificationCompat.Builder build,PendingIntent pending) {

            channel = chan;
            mNotificationManager = man;
            mBuilder = build;
            pendingIntent = pending;
            user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            UID = user.getUid();
            myCalendar= Calendar.getInstance();
            certC= Calendar.getInstance();
            mDataBase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://evolutionfitness-42b6e.firebaseio.com/");
            myFormat = "dd/MM/yy"; //In which you need put here
            sdf = new SimpleDateFormat(myFormat, Locale.ITALY);
            currentDate = sdf.format(myCalendar.getTime());
            System.out.println(currentDate);
            mDataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child("Utenti").child(UID).hasChild("Data Certificato Medico")){
                        certMed = Objects.requireNonNull(dataSnapshot.child("Utenti").child(UID).child("Data Certificato Medico").getValue()).toString();
                    }
                }




                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }


        public void run(){
            sendedCert=false;
            while (!Thread.currentThread().isInterrupted()&&!sendedCert) {

                try {
                    Thread.currentThread().sleep(5000);
                    if(certMed!=null){
                        try {
                            newCertDate= sdf.parse(certMed);
                            certC.setTime(newCertDate);
                            certC.add(certC.YEAR,1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (weekMed()) {
                        createNotification("Certificato medico in scadenza", "Manca una settimana alla scadenza del certificato medico", R.drawable.logo);
                        mNotificationManager.notify(2, mBuilder.build());
                        sendedCert=true;
                        mDataBase.child("Utenti").child(UID).child("Scadenza Settimanale Certificato Medico").setValue("true");
                    }
                    stop();

                }catch(ConcurrentModificationException x){
                    System.out.println("CONCURRENT MODIFICATION EXCEPTION");
                } catch (InterruptedException e) {
                    System.out.println("NOTIFICATION THREAD INTERRUPTED----------------------------");
                }

            }
        }


        public void stop(){

            Thread.currentThread().interrupt();
        }

        public boolean weekMed(){
            if(certC!=null){
                certC.add(certC.DAY_OF_MONTH,-7);
            }
            currentCert = sdf.format(Objects.requireNonNull(certC).getTime());
            System.out.println("WEEK CERT: "+currentCert);
            if(certMed!=null&&currentDate.equals(currentCert)) {
                return true;
            }
            return false;
        }

        public void createNotification(String title, String text,int Icon){
            System.out.println("ENTRATO NELLA CREAZIONE DELLA NOTIFICA");
            mBuilder.setSmallIcon(Icon);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(text);
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            System.out.println("notifica creata");
            mBuilder.setContentIntent(pendingIntent);
        }
    }

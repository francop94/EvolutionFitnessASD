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

public class NOTIFICATION_Thread_DayAnn extends Application implements Runnable{
        private NotificationChannel channel;
        private NotificationManager mNotificationManager ;
        private NotificationCompat.Builder mBuilder;
        private PendingIntent pendingIntent;
        private DatabaseReference mDataBase;
        private String UID, currentDate, currentAnn;
        private FirebaseUser user;
        private Calendar myCalendar, annC;
        private boolean sendedAnn;
        private String myFormat;
        private SimpleDateFormat sdf;
        private String TAG="NOTIFICATION THREAD";
        private String ann;
        private Date newAnnDate;
        public NOTIFICATION_Thread_DayAnn (NotificationChannel chan, NotificationManager man, NotificationCompat.Builder build,PendingIntent pending) {

            channel = chan;
            mNotificationManager = man;
            mBuilder = build;
            pendingIntent = pending;
            user = FirebaseAuth.getInstance().getCurrentUser();
            UID = user.getUid();
            myCalendar= Calendar.getInstance();
            annC= Calendar.getInstance();
            mDataBase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://evolutionfitness-42b6e.firebaseio.com/");
            myFormat = "dd/MM/yy"; //In which you need put here
            sdf = new SimpleDateFormat(myFormat, Locale.ITALY);
            currentDate = sdf.format(myCalendar.getTime());
            System.out.println(currentDate);
            mDataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Utenti").child(UID).hasChild("Data Quota Annuale")) {
                        ann = dataSnapshot.child("Utenti").child(UID).child("Data Quota Annuale").getValue().toString();
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
            sendedAnn=false;
            while (!Thread.currentThread().isInterrupted()&&!sendedAnn) {

                try {
                    Thread.currentThread().sleep(5000);
                    if(ann!=null){
                        try {
                            newAnnDate= sdf.parse(ann);
                            System.out.println("ANN: "+ann);
                            annC.setTime(newAnnDate);
                            annC.add(annC.YEAR,1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (dayAnn()) {
                        createNotification("Quota annuale in scadenza", "Manca un giorno alla scadenza della quota annuale", R.drawable.logo);
                        mNotificationManager.notify(4, mBuilder.build());
                        sendedAnn=true;
                        mDataBase.child("Utenti").child(UID).child("Scadenza Giornaliera Quota Annuale").setValue("true");
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

        public boolean dayAnn(){
            if(annC!=null){
                annC.add(annC.DAY_OF_MONTH,-1);

            }
            currentAnn = sdf.format(annC.getTime());
            System.out.println("DAY ANN: "+currentAnn);
            if(ann!=null&&currentDate.equals(currentAnn)) {
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

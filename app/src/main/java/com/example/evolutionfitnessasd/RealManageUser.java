package com.example.evolutionfitnessasd;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RealManageUser extends AppCompatActivity implements
        View.OnClickListener{
    private Spinner spinMonths;
    private Calendar myCalendar;
    private EditText abbonamento;
    private EditText annualFee;
    private EditText medicalCertificate;
    private boolean setAbb, setAnn, setMed;
    String dateAbb, dateAnn, dateMed, month;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_manage_user);
        myCalendar = Calendar.getInstance();

        abbonamento= (EditText) findViewById(R.id.abbonamento);
        annualFee= (EditText) findViewById(R.id.annualFee);
        medicalCertificate= (EditText) findViewById(R.id.medicalCertificate);
        // Buttons
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        myRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://evolutionfitness-42b6e.firebaseio.com/");
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(setAbb){ updateLabelAbb();}
                if(setMed){ updateLabelMed();}
                if(setAnn){ updateLabelAnn();}
            }

        };

        abbonamento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setMed=false;
                setAnn=false;
                // TODO Auto-generated method stub
                new DatePickerDialog(RealManageUser.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                setAbb= true;

            }
        });
        annualFee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setMed=false;
                setAbb=false;
                // TODO Auto-generated method stub
                new DatePickerDialog(RealManageUser.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                setAnn=true;
            }
        });
        medicalCertificate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setAbb=false;
                setAnn=false;
                new DatePickerDialog(RealManageUser.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                setMed=true;
            }
        });



        spinMonths= (Spinner) findViewById(R.id.spinMonths);//fetch the spinner from layout file
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.payments_month_array));//setting the country_array to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMonths.setAdapter(adapter);
//if you want to set any action you can do in this listener
        spinMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                month = arg0.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }
    private void updateLabelAbb() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);
        abbonamento.setText(sdf.format(myCalendar.getTime()));
        dateAbb = abbonamento.getText().toString();
    }
    private void updateLabelAnn() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);
        annualFee.setText(sdf.format(myCalendar.getTime()));
        dateAnn = annualFee.getText().toString();
    }
    private void updateLabelMed() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);
        medicalCertificate.setText(sdf.format(myCalendar.getTime()));
        dateMed = medicalCertificate.getText().toString();
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.save) {
            String abb = abbonamento.getText().toString();
            String ann = annualFee.getText().toString();
            String med = medicalCertificate.getText().toString();
            if (TextUtils.isEmpty(abb)&&TextUtils.isEmpty(ann) && TextUtils.isEmpty(med)) {
                Toast.makeText(this, "You have to select both date abb and month to save it", Toast.LENGTH_LONG).show();
            } else {
                if (!TextUtils.isEmpty(abb) && !TextUtils.isEmpty(ann) && !TextUtils.isEmpty(med)) {
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Abbonamento").setValue(dateAbb);
                    myRef.child("Users").child(shared.getUIDMan()).child("Months").setValue(month);
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Annual fee").setValue(dateAnn);
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Medical certificate").setValue(dateMed);
                } else if (!TextUtils.isEmpty(abb)) {
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Abbonamento").setValue(dateAbb);
                    myRef.child("Users").child(shared.getUIDMan()).child("Months").setValue(month);
                } else if (!TextUtils.isEmpty(ann)) {
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Annual fee").setValue(dateAnn);
                } else if (!TextUtils.isEmpty(med)) {
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Medical certificate").setValue(dateMed);
                } else if (!TextUtils.isEmpty(abb) && !TextUtils.isEmpty(med)) {
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Medical certificate").setValue(dateMed);
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Abbonamento").setValue(dateAbb);
                    myRef.child("Users").child(shared.getUIDMan()).child("Months").setValue(month);
                } else if (!TextUtils.isEmpty(abb) && !TextUtils.isEmpty(ann)) {
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Abbonamento").setValue(dateAbb);
                    myRef.child("Users").child(shared.getUIDMan()).child("Months").setValue(month);
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Annual fee").setValue(dateAnn);
                } else {
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Annual fee").setValue(dateAnn);
                    myRef.child("Users").child(shared.getUIDMan()).child("Date of Medical certificate").setValue(dateMed);
                }
            }
        }else if (i == R.id.delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RealManageUser.this);
            builder.setCancelable(true);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure to delete this user?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myRef.child("Users").child(shared.getUIDMan()).removeValue();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

}

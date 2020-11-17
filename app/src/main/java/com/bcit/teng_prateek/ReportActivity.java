package com.bcit.teng_prateek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    ArrayList<Reading> rdList;
    DatabaseReference myDatabase;
    TextView textViewEmail;
    TextView textViewSystolicReading;
    TextView textViewDiastolicReading;
    TextView textViewAvg;
    String spinnerSel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent myIntent = getIntent();
        spinnerSel = myIntent.getStringExtra("sSel");


        rdList = new ArrayList<>();
        myDatabase = FirebaseDatabase.getInstance().getReference("users/" + spinnerSel);


    }

    @Override
    public void onStart() {

        super.onStart();

        myDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    Reading reading = data.getValue(Reading.class);
                    rdList.add(reading);
                }
                calAvg(rdList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning:", "something went wrong and I don't know why");
            }

            public void calAvg(ArrayList<Reading> lst) {
                double systolicSum = 0;
                double diastolicSum = 0;
                int counter = 0;
                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String month = Integer.toString(localDate.getMonthValue());

                for(Reading r : lst) {
                    if(!r.getDatetime().isEmpty() && r.getDatetime() != null) {

                        String m = r.getDatetime().substring(0, 2);

                        if(m.equals(month)) {
                            systolicSum += r.getSystolic();
                            diastolicSum += r.getDiastolic();
                            ++counter;
                        }
                    }
                }

                systolicSum = systolicSum / counter;
                diastolicSum = diastolicSum / counter;

                populateData(systolicSum, diastolicSum, month);
            }

            public void populateData(double systolicAvg, double diastolicAvg, String month) {

                if(spinnerSel.equals("father")) {
                    textViewEmail.setText(R.string.father_email);

                } else if(spinnerSel.equals("mather")) {

                } else if(spinnerSel.equals("grandpa")) {

                } else {

                }

            }
        });




    }
}
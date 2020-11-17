package com.bcit.teng_prateek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    ArrayList<Reading> readings;
    DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        readings = new ArrayList<>();
        myDatabase = FirebaseDatabase.getInstance().getReference("users/");
        Intent my_intent = getIntent();


    }

    @Override
    public void onStart() {
        super.onStart();

        myDatabase.addValueEventListener(new ValueEventListener() {
            String[] users = {"father", "mother", "grandpa", "grandma"};

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot families : snapshot.getChildren()) {
                    for(DataSnapshot ds : families.getChildren()) {
                        Reading temp = ds.getValue(Reading.class);
                        Reading oneReading = new Reading(temp.getId(), temp.getSystolic(), temp.getDiastolic(), temp.getDatetime());
                        readings.add(oneReading);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("warning:", "something wrong and I don't know why");
            }
        });



    }
}
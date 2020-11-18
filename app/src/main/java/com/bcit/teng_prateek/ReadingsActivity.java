package com.bcit.teng_prateek;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadingsActivity extends AppCompatActivity {

    private static String user;
    RecyclerView recyclerView;

    RecyclerAdapter recyclerAdapter;

    List<Reading> readingList;

    public static String getUser() {
        return user;
    }

    DatabaseReference databaseReadings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        readingList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        Intent intent = getIntent();
        user = intent.getStringExtra("key").split("@")[0];

        databaseReadings = FirebaseDatabase.getInstance().getReference("users/" + user);
    }

    @Override
    protected void onStart() {

        super.onStart();

        databaseReadings.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readingList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Reading reading = ds.getValue(Reading.class);
                    readingList.add(reading);
                }
                recyclerAdapter = new RecyclerAdapter(readingList);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

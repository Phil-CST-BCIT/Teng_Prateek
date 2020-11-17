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
import java.util.Locale;

/**
 * This class supports the activity_report UI
 * It retrieves data from the firebase realtime database and calculates
 * the average of the systolic readings as well as the average of the diastolic reading.
 * It populates the results.
 */
public class ReportActivity extends AppCompatActivity {

    ArrayList<Reading> rdList; // stores the readings back from the database
    DatabaseReference myDatabase;
    TextView textViewEmail;
    TextView textViewSystolicReading;
    TextView textViewDiastolicReading;
    TextView textViewAvg;
    TextView textViewTitle;
    String spinnerSel; // the spinner selection in main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent myIntent = getIntent();
        spinnerSel = myIntent.getStringExtra("sSel");

        //initialize data fields
        rdList = new ArrayList<>();
        myDatabase = FirebaseDatabase.getInstance().getReference("users/" + spinnerSel);

        textViewTitle = findViewById(R.id.textViewReportTitle);
        textViewEmail = findViewById(R.id.textViewFamilyMemberEmail);
        textViewSystolicReading = findViewById(R.id.textViewAvgSystolic);
        textViewDiastolicReading = findViewById(R.id.textViewAvgDiastolic);
        textViewAvg = findViewById(R.id.textViewAvgCondition);

    }

    @Override
    public void onStart() {

        super.onStart();

        //read the database once
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

            //calculates the average of systolic and diastolic readings
            private void calAvg(ArrayList<Reading> lst) {
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

                systolicSum =  systolicSum / counter;
                diastolicSum = diastolicSum / counter;

                populateData(systolicSum, diastolicSum, month);
            }

            //fill out the email text view, systolic average text view, and diastolic average text view
            private void populateData(double systolicAvg, double diastolicAvg, String month) {

                switch (spinnerSel) {
                    case "father":
                        textViewEmail.setText(R.string.father_email);
                        populateCommonFields(systolicAvg, diastolicAvg, month);
                        break;
                    case "mather":
                        textViewEmail.setText(R.string.mother_email);
                        populateCommonFields(systolicAvg, diastolicAvg, month);
                        break;
                    case "grandpa":
                        textViewEmail.setText(R.string.grandpa_email);
                        populateCommonFields(systolicAvg, diastolicAvg, month);
                        break;
                    default:
                        textViewEmail.setText(R.string.grandma_email);
                        populateCommonFields(systolicAvg, diastolicAvg, month);
                        break;
                }
            }

            // a helper method
            private void populateCommonFields(double systolicAvg, double diastolicAvg, String month) {
                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String year = Integer.toString(localDate.getYear());

                String title = textViewTitle.getText().toString() + " " + month + " " + year;
                String sysAvg = String.format(Locale.getDefault(), "%1$, .2f", systolicAvg);
                String diaAvg = String.format(Locale.getDefault(), "%1$, .2f", diastolicAvg);
                String conAvg = condition(systolicAvg, diastolicAvg);
                textViewTitle.setText(title);
                textViewSystolicReading.setText(sysAvg);
                textViewDiastolicReading.setText(diaAvg);
                textViewAvg.setText(conAvg);
            }

            // checks the blood pressure condition based on the average values
            private String condition(double systolicAvg, double diastolicAvg) {

                if(systolicAvg < 120 && diastolicAvg < 80)
                    return "Normal";
                else if ((systolicAvg > 120 && systolicAvg < 129) && diastolicAvg < 80)
                    return "Elevated";
                else if ((systolicAvg > 130 && systolicAvg < 139) || (diastolicAvg > 80 &&diastolicAvg < 89))
                    return "Stage 1";
                else if ((systolicAvg > 140) || (diastolicAvg >= 90))
                    return "Stage 2";
                else
                    return "Crisis";
            }
        });
    }
}
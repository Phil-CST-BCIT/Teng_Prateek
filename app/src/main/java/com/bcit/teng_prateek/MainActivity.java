package com.bcit.teng_prateek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText editTextSystolic;
    EditText editTextDiastolic;
    TextView date;
    TextView time;


    DatabaseReference databaseReadings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDiastolic = findViewById(R.id.editTextDiastolic);
        editTextSystolic = findViewById(R.id.editTextSystolic);
        date = findViewById(R.id.textViewDate);
        time = findViewById(R.id.textViewTime);

        populateSpinner();
        populateCurrentDate();
        populateCurrentTime();

        Button btnSubmit = findViewById(R.id.buttonSubmit);
        //hook the submit button with the button listener class
        btnSubmit.setOnClickListener(btnListener);

        // starts ReportActivity
        Button btnGenerateReport = findViewById(R.id.generateReportBtn);
        btnGenerateReport.setOnClickListener(this);
    }

    /**
     * gets the current local date
     * @return dateStr: a string of date in format Month:Day:Year
     */
    private void populateCurrentDate() {
        String dateStr = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault()).format(new Date());
        TextView date  = (TextView) findViewById(R.id.textViewDate);
        date.setText(dateStr);
    }

    /**
     * gets the current local time
     * @return timeStr: a string of time in format Hour:Minute:Second
     */
    private void populateCurrentTime() {
        String timeStr = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        TextView time = (TextView) findViewById(R.id.textViewTime);
        time.setText(timeStr);
    }


    /**
     * populates the family member spinner with data from strings.xml
     */
    private void populateSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerFamilyMember);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.familyMember, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    /**
     * the family member spinner listener
     * @param parent
     * @param view
     * @param pos
     * @param id
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    /**
     * if nothing is selected then do...
     * @param parent
     */
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Anonymous class for button listener
     */
    private View.OnClickListener btnListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int systolic = Integer.parseInt(editTextSystolic.getText().toString().trim());
            int diastolic = Integer.parseInt(editTextDiastolic.getText().toString().trim());
            String datetime = date.getText().toString().trim() + " " + time.getText().toString().trim();

            if (systolic == 0) {
                Toast.makeText(MainActivity.this, "You must enter a Systolic value.", Toast.LENGTH_LONG).show();
                return;
            }

            if (diastolic == 0) {
                Toast.makeText(MainActivity.this, "You must enter a Diastolic value.", Toast.LENGTH_LONG).show();
                return;
            }

            Spinner spinner = (Spinner)findViewById(R.id.spinnerFamilyMember);

            databaseReadings = FirebaseDatabase.getInstance().getReference("users/" + spinner.getSelectedItem().toString().split("@")[0]);

            String id = databaseReadings.push().getKey();
            Reading reading = new Reading(id, systolic, diastolic, datetime);

            Task setValueTask = databaseReadings.child(id).setValue(reading);

            setValueTask.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(MainActivity.this,"Reading added.",Toast.LENGTH_LONG).show();

                    editTextDiastolic.setText("");
                    editTextSystolic.setText("");
                }
            });

            setValueTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,
                            "something went wrong.\n" + e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            });            return;
        }
    };

    public void onClickFamilyMemberConfirm(View view) {

        Spinner spinner = (Spinner)findViewById(R.id.spinnerFamilyMember);
        String value = spinner.getSelectedItem().toString();

        Intent myIntent = new Intent(MainActivity.this, ReadingsActivity.class);
        myIntent.putExtra("key", value);
        startActivity(myIntent);
    }

    @Override
    public void onClick(View v) {
        Intent generateReportIntent = new Intent(MainActivity.this, ReportActivity.class);
        startActivity(generateReportIntent);
    }


}
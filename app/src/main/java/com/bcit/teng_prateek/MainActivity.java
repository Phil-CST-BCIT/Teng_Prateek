package com.bcit.teng_prateek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateSpinner();
        String currentDate = populateCurrentDate();
        String currentTime = populateCurrentTime();

        Button btnSubmit = findViewById(R.id.buttonSubmit);
        //hook the submit button with the button listener class
        btnSubmit.setOnClickListener(btnListener);
    }

    /**
     * gets the current local date
     * @return dateStr: a string of date in format Month:Day:Year
     */
    private String populateCurrentDate() {
        String dateStr = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault()).format(new Date());
        TextView date  = (TextView) findViewById(R.id.textViewDate);
        date.setText(dateStr);

        return dateStr;
    }

    /**
     * gets the current local time
     * @return timeStr: a string of time in format Hour:Minute:Second
     */
    private String populateCurrentTime() {
        String timeStr = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        TextView time = (TextView) findViewById(R.id.textViewTime);
        time.setText(timeStr);

        return timeStr;
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
            //do something when the submit button is clicked
            return;
        }
    };

    public void onClickFamilyMemberConfirm(View view) {

        Spinner spinner = (Spinner)findViewById(R.id.spinnerFamilyMember);
        String value = spinner.getSelectedItem().toString();

        Intent myIntent = new Intent(MainActivity.this, ReadingsActivity.class);
        myIntent.putExtra("key", value);
        startActivity(myIntent);


    }
}
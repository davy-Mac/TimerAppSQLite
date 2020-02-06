package com.davidmaceda.sqlitesavedata;

import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper recordsDB;

    // activity_main elements
    EditText timerName; //edit text field
    Chronometer chronometer;
    Button start_btn;
    Button pause_btn;
    Button reset_btn;
    Button save_btn; //button to add data
    Button view_records_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordsDB = new DatabaseHelper(this);

        timerName = (EditText) findViewById(R.id.timerName);
        chronometer = (Chronometer) findViewById(R.id.mChronometer);
        start_btn = (Button) findViewById(R.id.start_btn);
        pause_btn = (Button) findViewById(R.id.pause_btn);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        save_btn = (Button) findViewById(R.id.save_btn);
        view_records_btn = (Button) findViewById(R.id.view_records_btn);

        addData();
        viewData();
        startChronometer();
        stopChronometer();
        resetChronometer();
    }

    /*
    public void startChronometer(View v) {
        int stoppedMilliseconds = 0;

        String chronoText = mChronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                    + Integer.parseInt(array[1]) * 60 * 1000
                    + Integer.parseInt(array[2]) * 1000;
        }

        mChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        mChronometer.start();
    }
     */
    public void startChronometer() {
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stoppedMilliseconds = 0;

                String chronoText = chronometer.getText().toString();
                String array[] = chronoText.split(":");
                if (array.length == 2) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                            + Integer.parseInt(array[1]) * 1000;
                } else if (array.length == 3) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                            + Integer.parseInt(array[1]) * 60 * 1000
                            + Integer.parseInt(array[2]) * 1000;
                }

                chronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                chronometer.start();
            }
        });
    }

    public void stopChronometer() {
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
            }
        });
    }

    public void resetChronometer() {
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });
    }

    public void addData() {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = timerName.getText().toString();
                String value = chronometer.getText().toString();
                if (timerName.length() != 0) {

                    boolean insertData = recordsDB.addData(name, value);

                    if (insertData == true) {
                        Toast.makeText(MainActivity.this, " Data successfully Inserted..!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Something Went Wrong...!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You Must Enter a Timer Name...!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void viewData() {
        view_records_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = recordsDB.showData();
                if (data.getCount() == 0) {
                    // display error message
                    displayMessage("Error", "No Data Found..!");
                }
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
                // old way of presenting data
                /*StringBuffer buffer = new StringBuffer();
                while (data.moveToNext()){
                    buffer.append("id: " + data.getString(0) + "\n");
                    buffer.append("timerName: " + data.getString(1) + "\n");
                    buffer.append("value: " + data.getString(2) + "\n");

                    // display message
                    displayMessage("Record Stored: ", buffer.toString());
                }*/
            }
        });
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}

package com.example.androidhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class heatingActivity extends AppCompatActivity {
    //btns
    private Button btnMotor1;
    private Button btnMotor2;
    private Button btnMotor3;
    private Button btnMotor4;
    private ImageButton homeButton;
    //db
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    DatabaseReference motorRef;

    //seekbar
    private SeekBar customMotor;
    private TextView customMotorText;

    //events
    private static final String LOG_DATE = "date";
    private static final String LOG_TIME = "time";
    private static final String LOG_CHANGE = "change";
    private static final String LOG_LUX = "lux";

    private int id;
    //users
    private static final String LOG_NAME = "name";
    private static final String LOG_IMAGE = "image";
    private FirebaseAuth DBauth = FirebaseAuth.getInstance();
    private String UID = DBauth.getCurrentUser().getUid();
    public String change;


    //time and date
    Calendar c = Calendar.getInstance();
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");


    String currentTime = timeFormat.format(c.getTime());
    String currentDate = dateFormat.format(c.getTime());


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heating);


        //heating controls
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference motorControl = database.getReference("motor1");


        //find ids
        homeButton = (ImageButton) findViewById(R.id.btnBack);
        btnMotor1 = (Button) findViewById(R.id.motor_ctrl0);
        btnMotor2 = (Button) findViewById(R.id.motor_ctrl1);
        btnMotor3 = (Button) findViewById(R.id.motor_ctrl2);
        btnMotor4 = (Button) findViewById(R.id.motor_ctrl3);
        customMotor = (SeekBar) findViewById(R.id.customAngle);
        customMotorText = (TextView) findViewById(R.id.customMotorText);
        //OFF
        btnMotor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorControl.setValue("0"); //send value to DB
                Toast.makeText(getBaseContext(), "Heating turned OFF", Toast.LENGTH_SHORT).show();
                btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.white));
                btnMotor1.setTextColor(getResources().getColor(android.R.color.black));
                btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor2.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor3.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor4.setTextColor(getResources().getColor(android.R.color.white));
                //update calendar
                change = "off";
                calendarAdd();
            }

        });
        //60 DEGREES
        btnMotor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorControl.setValue(60);
                Toast.makeText(getBaseContext(), "Heating turned ON at setting ONE", Toast.LENGTH_SHORT).show();
                btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.white));
                btnMotor2.setTextColor(getResources().getColor(android.R.color.black));
                btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor1.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor3.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor4.setTextColor(getResources().getColor(android.R.color.white));
                //update calendar
                change = "1";
                calendarAdd();
            }

        });
        //120 DEGREES
        btnMotor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorControl.setValue(120);
                Toast.makeText(getBaseContext(), "Heating turned ON at setting TWO", Toast.LENGTH_SHORT).show();
                btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.white));
                btnMotor3.setTextColor(getResources().getColor(android.R.color.black));
                btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor1.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor2.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor4.setTextColor(getResources().getColor(android.R.color.white));
                //update calendar
                change = "2";
                calendarAdd();
            }

        });
        //180 DEGREES
        btnMotor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorControl.setValue(180);
                Toast.makeText(getBaseContext(), "Heating on MAX", Toast.LENGTH_SHORT).show();
                btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.white));
                btnMotor4.setTextColor(getResources().getColor(android.R.color.black));
                btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor1.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor3.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor2.setTextColor(getResources().getColor(android.R.color.white));
                //update calendar
                change = "max";
                calendarAdd();
            }

        });

        customMotor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                customMotorText.setText(progress + "°");
                motorControl.setValue(progress);
                //MAKE ALL STATES OFF WHEN CUSTOM BAR USED
                btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor1.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor2.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor3.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor4.setTextColor(getResources().getColor(android.R.color.white));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToHome();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //sync DB/APP states
        //db connection
        motorRef = FirebaseDatabase.getInstance().getReference();
        motorRef.addValueEventListener(new ValueEventListener() {
            Integer loadCount = 1;   //to stop interference with user decisions

            //updates app with states from DB
            @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //load update once at app start
                while (loadCount == 1) {
                    String motorValue = dataSnapshot.child("motor1").getValue().toString();
                    int result = Integer.parseInt(motorValue);

                    customMotor.setProgress(result);
                    customMotorText.setText(result + "%");
                    if (motorValue.equals("0")) {

                        //change colour of buttons to match state
                        btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.white));
                        btnMotor1.setTextColor(getResources().getColor(android.R.color.black));
                        btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor2.setTextColor(getResources().getColor(android.R.color.white));
                        btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor3.setTextColor(getResources().getColor(android.R.color.white));
                        btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor4.setTextColor(getResources().getColor(android.R.color.white));

                    }
                    if (motorValue.equals("60")) {

                        btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.white));
                        btnMotor2.setTextColor(getResources().getColor(android.R.color.black));
                        btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor1.setTextColor(getResources().getColor(android.R.color.white));
                        btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor3.setTextColor(getResources().getColor(android.R.color.white));
                        btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor4.setTextColor(getResources().getColor(android.R.color.white));


                    }
                    if (motorValue.equals("120")) {

                        btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.white));
                        btnMotor3.setTextColor(getResources().getColor(android.R.color.black));
                        btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor1.setTextColor(getResources().getColor(android.R.color.white));
                        btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor2.setTextColor(getResources().getColor(android.R.color.white));
                        btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor4.setTextColor(getResources().getColor(android.R.color.white));

                    }
                    if (motorValue.equals("180")) {

                        btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.white));
                        btnMotor4.setTextColor(getResources().getColor(android.R.color.black));
                        btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor1.setTextColor(getResources().getColor(android.R.color.white));
                        btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor3.setTextColor(getResources().getColor(android.R.color.white));
                        btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.black));
                        btnMotor2.setTextColor(getResources().getColor(android.R.color.white));

                    }

                    loadCount = 2;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //if user is not logged in
        if (currentUser == null) {
            sendToLogin();
        }
    }

    public void sendToLogin() {
        Intent intent = new Intent(heatingActivity.this, LoginActivity.class);
        startActivity(intent); //bring up login screen
        finish(); //not allow user to go back by pressing back button
    }

    public void sendToHome() {
        Intent intent = new Intent(heatingActivity.this, portalActivity.class);
        startActivity(intent); //bring up login screen
        finish(); //not allow user to go back by pressing back button
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }

    //LOGOUT CODE
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_options:
                Intent optionIntent = new Intent(heatingActivity.this, SetupActivity.class);
                startActivity(optionIntent);
            case R.id.action_recent_activity:
                Intent settingsIntent = new Intent(heatingActivity.this, SetupActivity.class);
                startActivity(settingsIntent);
            default:
                return false;
        }

    }

    private void logout() {
        //firebase sign out
        mAuth.signOut();
        //redirect to login
        sendToLogin();
    }

    public void calendarAdd() {
        db2.collection("users").document(UID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String userName = task.getResult().getString("name");
                            String userImage = task.getResult().getString("image");

                            Map<String, Object> newAdd = new HashMap<>();
                            newAdd.put(LOG_IMAGE, userImage);
                            newAdd.put(LOG_NAME, userName);
                            newAdd.put(LOG_DATE, currentDate);
                            newAdd.put(LOG_TIME, currentTime);
                            newAdd.put(LOG_CHANGE, change);

                            db.collection("Heatlist").document().set(newAdd);

                        }

                    }

                });


    }

}


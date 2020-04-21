package com.example.androidhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

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

import java.util.HashMap;

import java.util.Map;
import java.util.Calendar;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {




    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    //light buttons
    private Button btnLight1;
    private Button btnLight2;
    private Button btnLight3;
    private Button btnLight4;
    private Button btnOff1;
    private Button btnOff2;
    private Button btnOff3;
    private Button btnOff4;
    private ImageButton homeBtn;


    private SeekBar luxOne;
    private SeekBar luxTwo;
    private SeekBar luxThree;
    private SeekBar luxFour;
    private TextView custom1;
    private TextView custom2;
    private TextView custom3;
    private TextView custom4;
    private StorageReference mStorageRef;
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
    public String lux;

    //calendar
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();

    Calendar c = Calendar.getInstance();
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");


    //time and date
    String currentTime = timeFormat.format(c.getTime());
    String currentDate = dateFormat.format(c.getTime());



    DatabaseReference lightRef;
    DatabaseReference luxRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            //initialise db
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //LIGHTS
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference light_one_db = database.getReference("LIGHT_ONE");
        final DatabaseReference light_two_db = database.getReference("LIGHT_TWO");
        final DatabaseReference light_three_db = database.getReference("LIGHT_THREE");
        final DatabaseReference light_four_db = database.getReference("LIGHT_FOUR");
        final DatabaseReference light_setting_one = database.getReference("lux1");
        final DatabaseReference light_setting_two = database.getReference("lux2");
        final DatabaseReference light_setting_three = database.getReference("lux3");
        final DatabaseReference light_setting_four = database.getReference("lux4");

                custom1 = (TextView) findViewById(R.id.cust1);
                custom2 = (TextView) findViewById(R.id.cust2);
                custom3 = (TextView) findViewById(R.id.cust3);
                custom4 = (TextView) findViewById(R.id.cust4);

            luxOne = (SeekBar) findViewById(R.id.lux1);
            luxTwo = (SeekBar) findViewById(R.id.lux2);
            luxThree = (SeekBar) findViewById(R.id.lux3);
            luxFour = (SeekBar) findViewById(R.id.lux4);

        btnLight1 = (Button) findViewById(R.id.light_btn);
        btnLight2 = (Button) findViewById(R.id.light_btn1);
        btnLight3 = (Button) findViewById(R.id.light_btn2);
        btnLight4 = (Button) findViewById(R.id.light_btn3);

        btnOff1 = (Button) findViewById(R.id.light_off);
        btnOff2 = (Button) findViewById(R.id.light_off1);
        btnOff3 = (Button) findViewById(R.id.light_off2);
        btnOff4 = (Button) findViewById(R.id.light_off3);

        homeBtn = (ImageButton) findViewById(R.id.btn_home);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToHome();
            }
        });





        //LIGHT BUTTON ON/OFF 1
        //NOTE: Change length_short time
        btnLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_one_db.setValue(0);
                Toast.makeText(getBaseContext(), "Light one activated",Toast.LENGTH_SHORT).show();
                     btnOff1.setVisibility(VISIBLE);
                     btnLight1.setVisibility(View.INVISIBLE);
                     luxOne.setVisibility(VISIBLE);
                custom1.setVisibility(VISIBLE);
             change = "light one on";
             calendarAdd();

            }
        });
        btnOff1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_one_db.setValue(1);
                Toast.makeText(getBaseContext(), "Light one deactivated",Toast.LENGTH_SHORT).show();
                btnOff1.setVisibility(View.INVISIBLE);
                btnLight1.setVisibility(VISIBLE);
               luxOne.setVisibility(View.INVISIBLE);
                custom1.setVisibility(View.INVISIBLE);
                change = "light one off";
                calendarAdd();


            }
        });



        //LIGHT BUTTON ON/OFF 2
        btnLight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_two_db.setValue(0);
                Toast.makeText(getBaseContext(), "Light two activated",Toast.LENGTH_SHORT).show();
                btnOff2.setVisibility(VISIBLE);
                btnLight2.setVisibility(View.INVISIBLE);
                luxTwo.setVisibility(VISIBLE);
                custom2.setVisibility(VISIBLE);
                change = "light two on";
                calendarAdd();


            }
        });
        btnOff2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_two_db.setValue(1);
                Toast.makeText(getBaseContext(), "Light two deactivated",Toast.LENGTH_SHORT).show();
                btnOff2.setVisibility(View.INVISIBLE);
                btnLight2.setVisibility(VISIBLE);
                luxTwo.setVisibility(View.INVISIBLE);
                custom2.setVisibility(View.INVISIBLE);
                change = "light two off";
                calendarAdd();


            }
        });



        btnLight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_three_db.setValue(0);
                Toast.makeText(getBaseContext(), "Light three activated",Toast.LENGTH_SHORT).show();
                btnOff3.setVisibility(VISIBLE);
                btnLight3.setVisibility(View.INVISIBLE);
                luxThree.setVisibility(VISIBLE);
                custom3.setVisibility(VISIBLE);
                change = "light three on";
                calendarAdd();



            }
        });
        btnOff3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_three_db.setValue(1);
                Toast.makeText(getBaseContext(), "Light three deactivated",Toast.LENGTH_SHORT).show();
                btnOff3.setVisibility(View.INVISIBLE);
                btnLight3.setVisibility(VISIBLE);
                luxThree.setVisibility(View.INVISIBLE);
                custom3.setVisibility(View.INVISIBLE);
                change = "light three off";
                calendarAdd();


            }
        });

        btnLight4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_four_db.setValue(0);
                Toast.makeText(getBaseContext(), "Light four activated",Toast.LENGTH_SHORT).show();
                btnOff4.setVisibility(VISIBLE);
                btnLight4.setVisibility(View.INVISIBLE);
                luxFour.setVisibility(VISIBLE);
                custom4.setVisibility(VISIBLE);
                change = "light four on";
                calendarAdd();




            }
        });
        btnOff4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_four_db.setValue(1);
                Toast.makeText(getBaseContext(), "Light four deactivated",Toast.LENGTH_SHORT).show();
                btnOff4.setVisibility(View.INVISIBLE);
                btnLight4.setVisibility(VISIBLE);
                luxFour.setVisibility(View.INVISIBLE);
                custom4.setVisibility(View.INVISIBLE);
                change = "light four off";
                calendarAdd();


            }
        });


                //custom light settings
        luxOne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress1, boolean fromUser) {
                custom1.setText(progress1 + "%");
                light_setting_one.setValue(progress1);
                change = "light one to";



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        luxTwo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress2, boolean fromUser) {
                custom2.setText(progress2 + "%");
                light_setting_two.setValue(progress2);
                change = "light two to";

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        luxThree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress3, boolean fromUser) {
                custom3.setText(progress3 + "%");
                light_setting_three.setValue(progress3);
                change = "light three to";
                lux = String.valueOf(progress3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        luxFour.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress4, boolean fromUser) {
                custom4.setText(progress4 + "%");
                light_setting_four.setValue(progress4);
                change = "light four to";
                lux = String.valueOf(progress4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    //custom lights

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance() .getCurrentUser();
        //if user is not logged in
        if(currentUser == null){
            sendToLogin();
        }

        //sync DB/APP states
        //db connection
        lightRef = FirebaseDatabase.getInstance().getReference();
        lightRef.addValueEventListener(new ValueEventListener() {
            Integer loadCount = 1;   //to stop interference with user decisions

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                while(loadCount == 1){
                    String lightOneValue = dataSnapshot.child("LIGHT_ONE").getValue().toString();
                    String lightTwoValue = dataSnapshot.child("LIGHT_TWO").getValue().toString();
                    String lightThreeValue = dataSnapshot.child("LIGHT_THREE").getValue().toString();
                    String lightFourValue = dataSnapshot.child("LIGHT_FOUR").getValue().toString();
                    if (lightOneValue.equals("0")) {
                        //Download data

                        //change colour of buttons to match state
                        btnOff1.setVisibility(VISIBLE);
                        btnLight1.setVisibility(View.INVISIBLE);
                        luxOne.setVisibility(VISIBLE);
                        custom1.setVisibility(VISIBLE);
                    }
                    if (lightTwoValue.equals("0")) {

                        btnOff2.setVisibility(VISIBLE);
                        btnLight2.setVisibility(View.INVISIBLE);
                        luxTwo.setVisibility(VISIBLE);
                        custom2.setVisibility(VISIBLE);
                    }
                    if (lightThreeValue.equals("0")) {

                        btnOff3.setVisibility(VISIBLE);
                        btnLight3.setVisibility(View.INVISIBLE);
                        luxThree.setVisibility(VISIBLE);
                        custom3.setVisibility(VISIBLE);
                    }
                    if (lightFourValue.equals("0")) {

                        btnOff4.setVisibility(VISIBLE);
                        btnLight4.setVisibility(View.INVISIBLE);
                        luxFour.setVisibility(VISIBLE);
                        custom4.setVisibility(VISIBLE);
                    }

                    loadCount = 2;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //LUX state syncing
        luxRef = FirebaseDatabase.getInstance().getReference();
        luxRef.addValueEventListener(new ValueEventListener() {
            Integer loadCount = 1;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                while (loadCount == 1) {
                    String luxOneValue = dataSnapshot.child("lux1").getValue().toString();
                    String luxTwoValue = dataSnapshot.child("lux2").getValue().toString();
                    String luxThreeValue = dataSnapshot.child("lux3").getValue().toString();
                    String luxFourValue = dataSnapshot.child("lux4").getValue().toString();
                    int result1 = Integer.parseInt(luxOneValue); //convert to int
                    luxOne.setProgress(result1);
                    custom1.setText(result1 +"%");
                    lux = String.valueOf(result1);
                    calendarAdd();
                    int result2 = Integer.parseInt(luxTwoValue);
                    luxOne.setProgress(result2);
                    custom2.setText(result2 +"%");
                    int result3 = Integer.parseInt(luxThreeValue);
                    luxOne.setProgress(result3);
                    custom3.setText(result3 +"%");
                    int result4 = Integer.parseInt(luxFourValue);
                    luxOne.setProgress(result4);
                    custom4.setText(result4 +"%");
                    loadCount = 2;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sendToLogin(){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent); //bring up login screen
            finish(); //not allow user to go back by pressing back button
    }
    public void sendToHome(){
        Intent homeIntent = new Intent(MainActivity.this, portalActivity.class);
        startActivity(homeIntent); //bring up login screen
        finish(); //not allow user to go back by pressing back button
    }


    public void calendarAdd()
    {
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
                    newAdd.put(LOG_LUX, lux);

                    db.collection("Lightlist").document().set(newAdd);

                }

    }

    });
    }}

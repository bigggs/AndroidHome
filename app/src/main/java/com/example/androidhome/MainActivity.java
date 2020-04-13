package com.example.androidhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
    private Button settingsBtn;
    private Button acBtn;

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
    private static final String LOG_DATE = "Date";
    private static final String LOG_TIME = "Time";
    private static final String LOG_CHANGE = "Change";

    //users
    private static final String LOG_NAME = "name";
    private static final String LOG_IMAGE = "image";
    private FirebaseAuth DBauth = FirebaseAuth.getInstance();
    private String UID = DBauth.getCurrentUser().getUid();
    public String change;

    //calendar
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();



    //time and date
    Date currentTime = Calendar.getInstance().getTime();
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    DatabaseReference lightRef;
    DatabaseReference luxRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


                //calendar



        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        settingsBtn = (Button) findViewById(R.id.settings1_btn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToHeating();


            }
        });

        acBtn = (Button) findViewById(R.id.ac_btn);
        acBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AC = new Intent(MainActivity.this, AcActivity.class);
                startActivity(AC); //bring up AC

            }
        });




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
             change = "Light ONE on";
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

            }
        });


                //custom light settings
        luxOne.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress1, boolean fromUser) {
                custom1.setText(progress1 + "%");

                light_setting_one.setValue(progress1);

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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
                //TOOL BAR
                mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);


    setSupportActionBar(mainToolbar);

    getSupportActionBar().setTitle("Android Home");


    }

    //custom lights

    @Override
    protected void onStart(){
        super.onStart();


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
        FirebaseUser currentUser = FirebaseAuth.getInstance() .getCurrentUser();
        //if user is not logged in
        if(currentUser == null){
            sendToLogin();
        }
    }

    public void sendToLogin(){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent); //bring up login screen
            finish(); //not allow user to go back by pressing back button
    }

    public void sendToHeating(){
        Intent heating = new Intent(MainActivity.this, firstActivity.class);
        startActivity(heating); //bring up login screen
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
            switch (item.getItemId()){
                case R.id.action_logout:
                    logout();
                        return true;
                case R.id.action_options:
                    Intent optionIntent = new Intent(MainActivity.this, SetupActivity.class);
                    startActivity(optionIntent);
                case R.id.action_recent_activity:
                    Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                    startActivity(settingsIntent);
                default:
                    return false;
        }

    }
    private void logout(){
        //firebase sign out
            mAuth.signOut();
            //redirect to login
            sendToLogin();
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
                    db.collection("calendar").document().set(newAdd);

                }

    }

    });
    }}

package com.example.androidhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AcActivity extends AppCompatActivity {
    private Button on_btn;
    private Button off_btn;
    private ImageButton home_btn;
    private SeekBar ac_controls;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private TextView ACtext;
    DatabaseReference ACref;

    //db calendar sync
    private static final String LOG_DATE = "date";
    private static final String LOG_TIME = "time";
    private static final String LOG_CHANGE = "change";
    private static final String LOG_SETTING = "setting";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();

    //Time and date
    Calendar c = Calendar.getInstance();
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");


    String currentTime = timeFormat.format(c.getTime());
    String currentDate = dateFormat.format(c.getTime());


    private int id;
    //users
    private static final String LOG_NAME = "name";
    private static final String LOG_IMAGE = "image";
    private FirebaseAuth DBauth = FirebaseAuth.getInstance();
    private String UID = DBauth.getCurrentUser().getUid();
    public String change;
    public String logSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);


        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        ACtext = (TextView) findViewById(R.id.customAC);
        on_btn = (Button) findViewById(R.id.btn_on);

        off_btn = (Button) findViewById(R.id.btn_off);
        ac_controls = (SeekBar) findViewById(R.id.AC_controls);

        home_btn = (ImageButton) findViewById(R.id.btn_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference acControls = database.getReference("AC");

        on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acControls.setValue(0);
                Toast.makeText(getBaseContext(), "AC Turned ON", Toast.LENGTH_SHORT).show();
                on_btn.setBackgroundColor(getResources().getColor(android.R.color.white));
               on_btn.setTextColor(getResources().getColor(android.R.color.black));
                off_btn.setBackgroundColor(getResources().getColor(android.R.color.black));
                off_btn.setTextColor(getResources().getColor(android.R.color.white));
                change = " on ";
                calendarAdd();

            }

        });

        off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acControls.setValue(70);
                Toast.makeText(getBaseContext(), "AC Turned OFF", Toast.LENGTH_SHORT).show();
                off_btn.setBackgroundColor(getResources().getColor(android.R.color.white));
                off_btn.setTextColor(getResources().getColor(android.R.color.black));
                on_btn.setBackgroundColor(getResources().getColor(android.R.color.black));
                on_btn.setTextColor(getResources().getColor(android.R.color.white));
                change = " off ";
                calendarAdd();


            }

        });



        ac_controls.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int result = 70 - progress;
                //multiply to make 70x = 100x
                //round to whole number
                DecimalFormat df = new DecimalFormat("0");
                Double hundieProgress = progress * 1.4285714285;

                ACtext.setText(df.format(hundieProgress) + "%");
                acControls.setValue(result);
                if(progress == 0) {
                    on_btn.setBackgroundColor(getResources().getColor(android.R.color.black));
                    on_btn.setTextColor(getResources().getColor(android.R.color.white));
                    off_btn.setBackgroundColor(getResources().getColor(android.R.color.black));
                    off_btn.setTextColor(getResources().getColor(android.R.color.white));

                }
                else{
                    off_btn.setBackgroundColor(getResources().getColor(android.R.color.black));
                    off_btn.setTextColor(getResources().getColor(android.R.color.white));
                }
                String resultString = String.valueOf(progress);
                logSetting = resultString;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


            home_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToHome();
                }
            });


    }

    public void sendToLogin() {
        Intent intent = new Intent(AcActivity.this, LoginActivity.class);
        startActivity(intent); //bring up login screen
        finish(); //not allow user to go back by pressing back button
    }
    public void sendToHome(){
      Intent intent = new Intent(AcActivity.this,portalActivity.class);
        startActivity(intent); //bring up login screen
        finish(); //not allow user to go back by pressing back button
   }


    @Override
    protected void onStart(){
        super.onStart();
        
        //sync DB/APP states
        //db connection
        ACref = FirebaseDatabase.getInstance().getReference();
        ACref.addValueEventListener(new ValueEventListener() {
            Integer loadCount = 1;   //to stop interference with user decisions

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                while(loadCount == 1){
                    String ACvalue = dataSnapshot.child("AC").getValue().toString();
                    int recalc = Integer.parseInt(ACvalue);
                    int result = recalc - 70;
                    ac_controls.setProgress(result);
                    ACtext.setText(recalc + "%");
                    if (ACvalue.equals("0")) {

                        //change colour of buttons to match state
                        on_btn.setBackgroundColor(getResources().getColor(android.R.color.white));
                        on_btn.setTextColor(getResources().getColor(android.R.color.black));
                        off_btn.setBackgroundColor(getResources().getColor(android.R.color.black));
                        off_btn.setTextColor(getResources().getColor(android.R.color.white));

                    }
                    if (ACvalue.equals("70")) {

                        off_btn.setBackgroundColor(getResources().getColor(android.R.color.white));
                        off_btn.setTextColor(getResources().getColor(android.R.color.black));
                        on_btn.setBackgroundColor(getResources().getColor(android.R.color.black));
                        on_btn.setTextColor(getResources().getColor(android.R.color.white));

                    }

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
                            newAdd.put(LOG_SETTING, logSetting);
                            db.collection("AClist").document().set(newAdd);

                        }

                    }

                });


    }

    }


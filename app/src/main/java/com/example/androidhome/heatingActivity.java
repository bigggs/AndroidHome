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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.view.View.VISIBLE;

public class heatingActivity extends AppCompatActivity {

    private Button btnMotor1;
    private Button btnMotor2;
    private Button btnMotor3;
    private Button btnMotor4;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    private SeekBar customMotor;
    private TextView customMotorText;
    private Button homeButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mStorageRef = FirebaseStorage.getInstance().getReference();



        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heating);
        //heating controls
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference motorControl = database.getReference("motor1");




        homeButton = (Button) findViewById(R.id.btnBack);
        btnMotor1 = (Button) findViewById(R.id.motor_ctrl0);
        btnMotor2 = (Button) findViewById(R.id.motor_ctrl1);
        btnMotor3 = (Button) findViewById(R.id.motor_ctrl2);
        btnMotor4 = (Button) findViewById(R.id.motor_ctrl3);
        customMotor = (SeekBar) findViewById(R.id.customAngle);
        customMotorText = (TextView) findViewById(R.id.customMotorText);
        btnMotor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motorControl.setValue("0");
                Toast.makeText(getBaseContext(), "Heating turned OFF", Toast.LENGTH_SHORT).show();
                btnMotor1.setBackgroundColor(getResources().getColor(android.R.color.white));
                btnMotor1.setTextColor(getResources().getColor(android.R.color.black));
                btnMotor2.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor2.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor3.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor3.setTextColor(getResources().getColor(android.R.color.white));
                btnMotor4.setBackgroundColor(getResources().getColor(android.R.color.black));
                btnMotor4.setTextColor(getResources().getColor(android.R.color.white));
            }

    });
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
            }

        });
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
            }

        });
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
            }

        });

        customMotor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    customMotorText.setText(progress + "°");
                    motorControl.setValue(progress);

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
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance() .getCurrentUser();
        //if user is not logged in
        if(currentUser == null){
            sendToLogin();
        }
    }

    public void sendToLogin(){
        Intent intent = new Intent(heatingActivity.this, LoginActivity.class);
        startActivity(intent); //bring up login screen
        finish(); //not allow user to go back by pressing back button
    }
    public void sendToHome(){
        Intent intent = new Intent(heatingActivity.this,MainActivity.class);
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
        switch (item.getItemId()){
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
    private void logout(){
        //firebase sign out
        mAuth.signOut();
        //redirect to login
        sendToLogin();
    }


}


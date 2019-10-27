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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private TextView welcomeText;
    //light buttons
    private Button btnLight1;
    private Button btnLight2;
    private Button btnLight3;
    private Button btnLight4;
    private Button btnOff1;
    private Button btnOff2;
    private Button btnOff3;
    private Button btnOff4;

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth = FirebaseAuth.getInstance();


        //LIGHTS
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference light_one_db = database.getReference("LIGHT_ONE");
        final DatabaseReference light_two_db = database.getReference("LIGHT_TWO");
        final DatabaseReference light_three_db = database.getReference("LIGHT_THREE");
        final DatabaseReference light_four_db = database.getReference("LIGHT_FOUR");

        btnLight1 = (Button) findViewById(R.id.light_btn);
        btnLight2 = (Button) findViewById(R.id.light_btn1);
        btnLight3 = (Button) findViewById(R.id.light_btn2);
        btnLight4 = (Button) findViewById(R.id.light_btn3);

        btnOff1 = (Button) findViewById(R.id.light_off);
        btnOff2 = (Button) findViewById(R.id.light_off1);
        btnOff3 = (Button) findViewById(R.id.light_off2);
        btnOff4 = (Button) findViewById(R.id.light_off3);

        //LIGHT BUTTON ON/OFF 1
        //NOTE: Change lenght_short time
        btnLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_one_db.setValue("0");
                Toast.makeText(getBaseContext(), "Light one activated",Toast.LENGTH_SHORT).show();
                     btnOff1.setVisibility(VISIBLE);
                     btnLight1.setVisibility(View.INVISIBLE);


            }
        });
        btnOff1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_one_db.setValue("1");
                Toast.makeText(getBaseContext(), "Light one deactivated",Toast.LENGTH_SHORT).show();
                btnOff1.setVisibility(View.INVISIBLE);
                btnLight1.setVisibility(VISIBLE);

            }
        });



        //LIGHT BUTTON ON/OFF 2
        btnLight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_two_db.setValue("0");
                Toast.makeText(getBaseContext(), "Light two activated",Toast.LENGTH_SHORT).show();
                btnOff2.setVisibility(VISIBLE);
                btnLight2.setVisibility(View.INVISIBLE);
            }
        });
        btnOff2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_two_db.setValue("1");
                Toast.makeText(getBaseContext(), "Light two deactivated",Toast.LENGTH_SHORT).show();
                btnOff2.setVisibility(View.INVISIBLE);
                btnLight2.setVisibility(VISIBLE);
            }
        });



        btnLight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_three_db.setValue("0");
                Toast.makeText(getBaseContext(), "Light three activated",Toast.LENGTH_SHORT).show();
                btnOff3.setVisibility(VISIBLE);
                btnLight3.setVisibility(View.INVISIBLE);

            }
        });
        btnOff3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_three_db.setValue("1");
                Toast.makeText(getBaseContext(), "Light three deactivated",Toast.LENGTH_SHORT).show();
                btnOff3.setVisibility(View.INVISIBLE);
                btnLight3.setVisibility(VISIBLE);
            }
        });

        btnLight4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_four_db.setValue("0");
                Toast.makeText(getBaseContext(), "Light four activated",Toast.LENGTH_SHORT).show();
                btnOff4.setVisibility(VISIBLE);
                btnLight4.setVisibility(View.INVISIBLE);

            }
        });
        btnOff4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                light_four_db.setValue("1");
                Toast.makeText(getBaseContext(), "Light four deactivated",Toast.LENGTH_SHORT).show();
                btnOff4.setVisibility(View.INVISIBLE);
                btnLight4.setVisibility(VISIBLE);
            }
        });
        //TESTING DB SEND STATE TO APP
      //  if(light_one_db.toString().equals("0")){
     //       btnOff1.setVisibility(VISIBLE);
    //        btnLight1.setVisibility(View.INVISIBLE);
 //       }
    //    else if(light_one_db.toString().equals("1")){
   //         btnOff1.setVisibility(View.INVISIBLE);
   //         btnLight1.setVisibility(VISIBLE);
      //  }

                //TOOL BAR
                mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
    welcomeText = (TextView) findViewById(R.id.welcome_text);

    welcomeText.setText("Welcome " + userName );
    setSupportActionBar(mainToolbar);

    getSupportActionBar().setTitle("Android Home");

    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
   // userName = user.getEmail();
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
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
}

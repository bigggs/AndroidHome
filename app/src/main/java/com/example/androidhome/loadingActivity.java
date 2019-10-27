package com.example.androidhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.lang.Thread.sleep;

public class loadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

  //  @Override
 //   protected void onStart(){
     //   super.onStart();
     //   try {
     //       sleep(3000);
    //    }catch (InterruptedException e)
    //    { e.printStackTrace();
   //     }

           // Intent sucessLogin = new Intent(loadingActivity.this, MainActivity.class);
          //  startActivity(sucessLogin); //bring main screen
     //       finish(); //not allow user to go back by pressing back button
        }

/* } */

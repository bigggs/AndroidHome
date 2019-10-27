package com.example.androidhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import static android.widget.Toast.LENGTH_LONG;

public class LoginActivity extends AppCompatActivity {
    //declaring text fields and buttons
    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button loginRegBtn;


    //declaring DB authentication and progress bar
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //Text fields and buttons
        loginEmailText = (EditText) findViewById(R.id.login_email);
        loginPassText = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginRegBtn = (Button) findViewById(R.id.login_reg_btn);
        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);



        //Register button
        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            //sends to register page
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });



            //When user presses login
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty((loginPass))){
                    loginProgress.setVisibility(View.VISIBLE);


                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if login works
                            if(task.isSuccessful()) {
                                loginSucess();
                            } else{//error message for failed login

                                String errorLogin = task.getException().getMessage();
                                //Shows android error for each scenario
                                Toast.makeText(LoginActivity.this, "Error: " + errorLogin, LENGTH_LONG).show();
                                loginProgress.setVisibility(View.INVISIBLE);
                            }
                        }

                    });


                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //If user is logged in
        if(currentUser !=null){

            loginSucess();

        }
    }

    private void loginSucess() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}

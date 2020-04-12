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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText reg_email_field;
    private EditText reg_passs_field;
    private EditText reg_pass_field2;

    private Button reg_btn;
    private Button reg_login_btn;

    private ProgressBar reg_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        reg_email_field = (EditText) findViewById(R.id.reg_email);
        reg_passs_field = (EditText) findViewById(R.id.reg_pass);
        reg_pass_field2 = (EditText) findViewById(R.id.reg_pass2);

        reg_btn = (Button) findViewById(R.id.reg_back);
        reg_login_btn = (Button) findViewById(R.id.reg_submit);

        reg_progress = (ProgressBar) findViewById(R.id.reg_progressbar);

        //Back button/already signed up
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(returnIntent);
            }
        });

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = reg_email_field.getText().toString();
                String pass = reg_passs_field.getText().toString();
                String pass2 = reg_pass_field2.getText().toString();

                //confirmation of information being valid

                //fields not empty
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(pass2)) {
                    //passwords match
                    if (pass.equals(pass2)) {
                        reg_progress.setVisibility(View.VISIBLE);
                        //login with Firebase Auth
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Login
                                if (task.isSuccessful()) {
                                    Intent intentSetup = new Intent (RegisterActivity.this, SetupActivity.class);
                                    startActivity(intentSetup);
                                            finish();
                                   // sendToMain();
                                //ERRORS
                                } else {
                                    String errorLogin = task.getException().getMessage();
                                    //Shows android error for each scenario
                                    Toast.makeText(RegisterActivity.this, "Error: " + errorLogin, LENGTH_LONG).show();
                                    reg_progress.setVisibility(View.INVISIBLE);
                                }
                                reg_progress.setVisibility(View.INVISIBLE);
                            }
                        });

                    } else {
                        Toast.makeText(RegisterActivity.this, "Error, Please re enter your desired information" , LENGTH_LONG).show();


                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null)

        sendToMain();{
    }
}
    //send user to main screen
    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        startActivity(mainIntent);
    }
    }

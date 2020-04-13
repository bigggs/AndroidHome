package com.example.androidhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {


    private EditText optionsName;
    private Button optionsSubmit;
    private CircularImageView avatar_img;
    private Uri mainImage = null;
    private ProgressBar optionsProgress;
    private FirebaseAuth DBauth;
    private Uri download_uri;
    private StorageReference mStorageRef;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar setupToolbar = findViewById(R.id.setup_toolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Details");

        //FIREBASE STORAGE
       DBauth = FirebaseAuth.getInstance();
       mStorageRef = FirebaseStorage.getInstance().getReference();
        UID = DBauth.getCurrentUser().getUid();



        //FIREBASE FIRESTORE

        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        optionsProgress = findViewById(R.id.options_progress);
        optionsName = findViewById(R.id.options_name);
        optionsSubmit = findViewById(R.id.btn_submit_options);
            avatar_img = findViewById(R.id.avatar_icon);

        db.collection("Users").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String name = task.getResult().getString("name");
                    String image = task.getResult().getString("image");
                    optionsName.setText(name);
                }
                else {
                    Toast.makeText(SetupActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    avatar_img.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
                //MARSHAMELLOW OR MORE
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {   //checks if app has user permission, if not ask user to grant app permission
                if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(SetupActivity.this, "Please enable permissions for Android Home in your settings", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                }else{
                    //sends user to crop activity
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(SetupActivity.this);
                }

            }
     /*       //if user has below mashmellow skip permissions
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(SetupActivity.this);*/
        }
    });
    //submit button
    optionsSubmit.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

       final String name = optionsName.getText().toString();

       //user must have a name but avatar is optional
         if (!TextUtils.isEmpty(name)) {
          final String ID = DBauth.getCurrentUser().getUid();
          optionsProgress.setVisibility(View.VISIBLE);


          //root of firebase storage
             StorageReference img_location = mStorageRef.child("Images").child(ID + ".jpg");
             img_location.putFile(mainImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                 //ERROR HANDLE IF SUBMIT FAIL
                 //Firebase code was outdated, found solution https://www.codeproject.com/Questions/1248011/What-do-I-use-instead-of-getdownloadurl-in-android
                  if (task.isSuccessful()) {
                    Task<Uri> download_uri = task.getResult().getStorage().getDownloadUrl();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("image", mainImage.toString());

                        db.collection("users").document(ID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                             Toast.makeText(SetupActivity.this, "Image Sucessfully Uploaded", Toast.LENGTH_LONG).show();
                             Intent intent = new Intent(SetupActivity.this, MainActivity.class);
                             startActivity(intent); //bring up main screen
                         }
                        }).addOnFailureListener(new OnFailureListener() {
                           @Override
                         public void onFailure(@NonNull Exception e) { Toast.makeText(SetupActivity.this, "Something went wrong" + e, Toast.LENGTH_LONG).show(); }
                                                                                                                           });

                        optionsProgress.setVisibility(View.INVISIBLE);
                  } }

                  ;

             }
             );
         } }
    });



    }
                //CODE FROM https://github.com/ArthurHub/Android-Image-Cropper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImage = result.getUri();
                avatar_img.setImageURI(mainImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}

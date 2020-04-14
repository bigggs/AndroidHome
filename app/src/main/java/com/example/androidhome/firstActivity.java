package com.example.androidhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;


import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

public class firstActivity extends AppCompatActivity {
    //db sync


    private static final String KEY_NAME = "Name";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_DATE = "Date";
    private static final String KEY_TIME = "Time";
    private static final String KEY_CHANGE = "Change";

    private TextView dataText;

    //image
    private CircularImageView avatarImg;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String LOG_NAME = "name";
    private static final String LOG_IMAGE = "image";
    private FirebaseAuth DBauth = FirebaseAuth.getInstance();
    private String UID = DBauth.getCurrentUser().getUid();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();

        //Calendar
    private CollectionReference noteRef = db.collection("calendar");

    private SliderLayout mDemoSlider;

    DatabaseReference totalRef;

    String lightOneValue;
    String lightTwoValue;
    String lightThreeValue;
    String lightFourValue;
    String luxOneValue;
    String luxTwoValue;
    String luxThreeValue;
    String luxFourValue;
    String ACvalue;
    String motorValue;

    //recycler
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //recycler
        ArrayList<String> animalNames = new ArrayList<>();

        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");





        //FIREBASE STORAGE
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Images");

        //FIREBASE FIRESTORE


        dataText = (TextView) findViewById(R.id.text_view_data);
        mDemoSlider = findViewById(R.id.slider);


        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();

        listUrl.add("https://cnet4.cbsistatic.com/img/AXwQBJb1aVW35t86aTpUZFoote8=/940x0/2017/09/20/32a94ec6-03a1-4225-b799-cf2e4f3d127f/cree-floodlight-4.jpg");
        listName.add("Lighting");

        listUrl.add("https://blog.fantasticservices.com/wp-content/uploads/2019/12/central-heating-inhibitor-radiator.jpg");
        listName.add("Heating");

        listUrl.add("https://www.handymancraftywoman.com/wp-content/uploads/2019/12/my-super-ha-feature-rac-3_v1.jpg");
        listName.add("Air Conditioning");


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);

        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(this);
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", listName.get(i));
            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.stopCyclingWhenTouch(false);
    }








    @Override
    protected void onStart() {
        super.onStart();
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        String flowData = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            String name = note.getName();
                            String image = note.getImage();
                            String date = note.getDate();
                            String time = note.getTime();
                            String change = note.getChange();


                            flowData += ( name + " " + date + " " + time + " " + change + "\n\n");

                            dataText.setText(flowData);

                        }
                    }

                });






        //sync DB/APP states
        //db connection
        totalRef = FirebaseDatabase.getInstance().getReference();
        totalRef.addValueEventListener(new ValueEventListener() {
            Integer loadCount = 1;   //to stop interference with user decisions

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                while (loadCount == 1) {
                    lightOneValue = dataSnapshot.child("LIGHT_ONE").getValue().toString();
                    lightTwoValue = dataSnapshot.child("LIGHT_TWO").getValue().toString();
                    lightThreeValue = dataSnapshot.child("LIGHT_THREE").getValue().toString();
                    lightFourValue = dataSnapshot.child("LIGHT_FOUR").getValue().toString();
                    luxOneValue = dataSnapshot.child("lux1").getValue().toString();
                    luxTwoValue = dataSnapshot.child("lux2").getValue().toString();
                    luxThreeValue = dataSnapshot.child("lux3").getValue().toString();
                    luxFourValue = dataSnapshot.child("lux4").getValue().toString();
                    ACvalue = dataSnapshot.child("AC").getValue().toString();
                    motorValue = dataSnapshot.child("motor1").getValue().toString();


                    if (lightOneValue.equals("0")) {

                        lightOneValue = "On";
                    } else {
                        lightOneValue = "Off";
                    }
                    if (lightTwoValue.equals("0")) {

                        lightTwoValue = "On";
                    } else {
                        lightTwoValue = "Off";
                    }
                    if (lightThreeValue.equals("0")) {

                        lightThreeValue = "On";
                    } else {
                        lightThreeValue = "Off";
                    }
                    if (lightFourValue.equals("0")) {

                        lightFourValue = "On";
                    } else {
                        lightFourValue = "Off";
                    }


                    loadCount = 2;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }


    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().getString("extra") + "", Toast.LENGTH_SHORT).show();
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    public void onPageSelected(int position) {

    }


    public void onPageScrollStateChanged(int state) {

    }

    public void collectID() {

        db2.collection("users").document(UID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String userName = task.getResult().getString("name");
                            String userImage = task.getResult().getString("image");
                        }
                    }
                });
    }
}

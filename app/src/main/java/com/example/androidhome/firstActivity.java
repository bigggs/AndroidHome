package com.example.androidhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;



import android.view.View;

import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class firstActivity extends AppCompatActivity {
    //db sync


    private static final String KEY_NAME = "Name";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_DATE = "Date";
    private static final String KEY_TIME = "Time";
    private static final String KEY_CHANGE = "Change";


    //scrollbar and scrollers
    private TextView dataTextLights;
    private TextView dataTextHeat;
    private TextView dataTextAC;
    private TextView textNameId;
    private ScrollView lightScroll;
    private ScrollView heatScroll;
    private ScrollView acScroll;

    //image
    private CircularImageView avatarImg;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    //imgbtn
    private ImageButton homebtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String LOG_NAME = "name";
    private static final String LOG_IMAGE = "image";
    private FirebaseAuth DBauth = FirebaseAuth.getInstance();
    private String UID = DBauth.getCurrentUser().getUid();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();
private String abc;
        //Calendar
    private CollectionReference lightRef = db.collection("Lightlist");
    private CollectionReference heatRef = db.collection("Heatlist");
    private CollectionReference acRef = db.collection("AClist");



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

    String heatText;
    String acText;
    //recycler
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //img btn
        homebtn = (ImageButton) findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToHome();
            }
        });


        //FIREBASE STORAGE
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Images");

        //FIREBASE FIRESTORE

        //text for changes
        dataTextLights = (TextView) findViewById(R.id.text_view_data);
        dataTextHeat = (TextView) findViewById(R.id.text_view_heat);
        dataTextAC = (TextView) findViewById(R.id.text_view_AC);

        lightScroll = (ScrollView) findViewById(R.id.scroll3);
        heatScroll = (ScrollView) findViewById(R.id.scroll1);
        acScroll = (ScrollView) findViewById(R.id.scroll2);

        //title text
        textNameId = (TextView) findViewById(R.id.textnameid);


        //intial datatext
        lightScroll.setVisibility(View.VISIBLE);
        dataTextLights.setVisibility(View.VISIBLE);

        getDBdata();
    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    public void scrollSlider() {


        ArrayList<String> lightsOn = new ArrayList<>();
        if (lightOneValue == "on"){
            String lightOneOn = "1";
            lightsOn.add(lightOneOn);
        }
        if (lightTwoValue == "on"){
            String lightTwoOn = "2";
            lightsOn.add(lightTwoOn);
        }
        if (lightThreeValue == "on"){
            String lightThreeOn = "3";
            lightsOn.add(lightThreeOn);
        }
        if (lightFourValue == "on") {
            String lightFourOn = "4";
            lightsOn.add(lightFourOn);
        }





        //Slider
        mDemoSlider = findViewById(R.id.slider);
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();
        //Light scroller
        listUrl.add("https://cnet4.cbsistatic.com/img/AXwQBJb1aVW35t86aTpUZFoote8=/940x0/2017/09/20/32a94ec6-03a1-4225-b799-cf2e4f3d127f/cree-floodlight-4.jpg");
        listName.add("Lights on: " + lightsOn.toString());
        //Heat scroller


        listUrl.add("https://blog.fantasticservices.com/wp-content/uploads/2019/12/central-heating-inhibitor-radiator.jpg");
        listName.add(heatText);





        //AC scroller
        listUrl.add("https://www.handymancraftywoman.com/wp-content/uploads/2019/12/my-super-ha-feature-rac-3_v1.jpg");
        listName.add(acText);



        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();


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
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        //25 secs per auto scroll
        mDemoSlider.setDuration(25000);
        mDemoSlider.stopCyclingWhenTouch(false);


        //slider page change
        //page2
        mDemoSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                    switch (position) {
                        case 0:
                            //lights
                            acScroll.setVisibility(View.INVISIBLE);
                            heatScroll.setVisibility(View.INVISIBLE);
                            lightScroll.setVisibility(View.VISIBLE);

                            dataTextLights.setVisibility(View.VISIBLE);
                            dataTextHeat.setVisibility(View.INVISIBLE);
                            dataTextAC.setVisibility(View.INVISIBLE);
                            textNameId.setText("Lights");
                            break;

                        case 1:
                            //ac
                            acScroll.setVisibility(View.INVISIBLE);
                            heatScroll.setVisibility(View.VISIBLE);
                            lightScroll.setVisibility(View.INVISIBLE);

                            dataTextLights.setVisibility(View.INVISIBLE);
                            dataTextHeat.setVisibility(View.VISIBLE);
                            dataTextAC.setVisibility(View.INVISIBLE);
                            textNameId.setText("Heating");
                            break;

                        case 2:
                            //heating
                            acScroll.setVisibility(View.VISIBLE);
                            heatScroll.setVisibility(View.INVISIBLE);
                            lightScroll.setVisibility(View.INVISIBLE);

                            dataTextLights.setVisibility(View.INVISIBLE);
                            dataTextHeat.setVisibility(View.INVISIBLE);
                            dataTextAC.setVisibility(View.VISIBLE);
                            textNameId.setText("AC");
                            break;


                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
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

    //Light Data collection
    public void getLightData(){
        lightRef.orderBy("time", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        String flowData = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            //get db values for each variable
                            String name = note.getName();
                            String image = note.getImage();
                            String date = note.getDate();
                            String time = note.getTime();
                            String change = note.getChange();
                            String luxData = note.getLux();


                            //write to app
                            flowData += ( name + " turned " + change + " at " + date + " " + time +  "\n\n");
                            dataTextLights.setText(flowData);

                        }
                    }

                });
    }
    //Heat Data collection
    public void getHeatData(){
        heatRef.orderBy("time").get()
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
                            String lux = note.getLux();

                            flowData += ( name + " turned heating to setting " + change +" at " + date + " " + time +  "\n\n");
                            dataTextHeat.setText(flowData);

                        }
                    }

                });
    }
    //AC Data collection
    public void getACData(){
        acRef.orderBy("time").get()

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
                            String setting = note.getSetting();

                            flowData += ( name + " turned " + change + " at " + date + " " + time + " at setting " +setting +  "\n\n");

                            dataTextAC.setText(flowData);

                        }
                    }

                });
    }
    public void getDBdata(){

        //collect db values for each textview
        getLightData();
        getHeatData();
        getACData();

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


                    //light setting for scroller
                    if (lightOneValue.equals("0")) {

                        lightOneValue = "on";
                    } else {
                        lightOneValue = "off";
                    }
                    if (lightTwoValue.equals("0")) {

                        lightTwoValue = "on";
                    } else {
                        lightTwoValue = "off";
                    }
                    if (lightThreeValue.equals("0")) {

                        lightThreeValue = "on";
                    } else {
                        lightThreeValue = "off";
                    }
                    if (lightFourValue.equals("0")) {

                        lightFourValue = "on";
                    } else {
                        lightFourValue = "off";
                    }


                    //heat setting for scroller

                    if(motorValue .equals("0")) {
                        heatText = "Heating is off";
                    }
                    else if(motorValue .equals("60")){
                        heatText = "Heating is on at setting one " + "(" + motorValue + ")";
                    }
                    else if(motorValue .equals("120")){
                        heatText = "Heating is on at setting two " + "(" + motorValue + ")";
                    }
                    else if(motorValue .equals("180")){
                        heatText = "Heating is on at setting max " + "(" + motorValue + ")";
                    }
                    else{
                        heatText = "Heating is at custom setting " +motorValue;
                }
                    //convert ac value string into double
                    Double acInt = Double.parseDouble(ACvalue);
                    //round up to 1 dp
                    DecimalFormat df = new DecimalFormat("0");
                    //to make 70n to 100n
                    acInt = acInt * 1.4285714285;

                    if(ACvalue .equals("70")) {
                        acText = "Air conditioning is off";
                    }
                    else {
                        acText = "Air conditioning is on at " + df.format(acInt) + "%";

                    }
                    scrollSlider();


                    loadCount = 2;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void sendToHome(){
        Intent intent = new Intent(firstActivity.this,MainActivity.class);
        startActivity(intent); //bring up login screen
        finish(); //not allow user to go back by pressing back button
    }

}

package com.example.androidhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

public class portalActivity extends AppCompatActivity {
    //sliders
    private SliderLayout lightSlider;
    private SliderLayout acSlider;
    private SliderLayout heatSlider;
    private SliderLayout dbSlider;
    private SliderLayout setupSlider;
    private SliderLayout logoutSlider;
    //for logout
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);
        //for logout
        mAuth = FirebaseAuth.getInstance();

        //sliders
        lightSlider = findViewById(R.id.slider_lights);
        acSlider = findViewById(R.id.slider_ac);
        heatSlider = findViewById(R.id.slider_heat);
        dbSlider = findViewById(R.id.slider_db);
        setupSlider = findViewById(R.id.slider_setup);
        logoutSlider = findViewById(R.id.slider_logout);
        //var, img, text
        makeSlider(lightSlider, "https://cnet4.cbsistatic.com/img/AXwQBJb1aVW35t86aTpUZFoote8=/940x0/2017/09/20/32a94ec6-03a1-4225-b799-cf2e4f3d127f/cree-floodlight-4.jpg", "Lights");
        lightSwitch();

        makeSlider(acSlider, "https://www.handymancraftywoman.com/wp-content/uploads/2019/12/my-super-ha-feature-rac-3_v1.jpg", "Air Conditioning");
        acSwitch();

        makeSlider(heatSlider, "https://blog.fantasticservices.com/wp-content/uploads/2019/12/central-heating-inhibitor-radiator.jpg", "Heating");
        heatSwitch();

        makeSlider(dbSlider, "https://www.wrike.com/blog/content/uploads/2017/05/rawpixel-633847-unsplash-738x518.jpg", "Calendar");
        calendarSwitch();

        makeSlider(setupSlider, "https://www.southwestfarmer.co.uk/resources/images/7329856?type=responsive-gallery-fullscreen", "Setup");
        setupSwitch();

        makeSlider(logoutSlider, "https://images.pexels.com/photos/277559/pexels-photo-277559.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500", "Logout");
        logoutSwitch();


    }
    //make slider method
    public void makeSlider(SliderLayout sliderName, String sliderImg, String sliderTxt){
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();
        //Light scroller
        listUrl.add(sliderImg);
        listName.add(sliderTxt);

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
                    .setProgressBarVisible(false);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", listName.get(i));
            sliderName.addSlider(sliderView);
        }

        // set Slider Transition Animation
        sliderName.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderName.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);

        sliderName.setCustomAnimation(new DescriptionAnimation());

        sliderName.setDuration(10000000);
        sliderName.stopCyclingWhenTouch(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance() .getCurrentUser();
        //if user is not logged in
        if(currentUser == null){
            sendToLogin();
        }
    }

    public void lightSwitch()
    {
        lightSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            sendToLights();
                            break;
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void acSwitch()
    {
        acSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        sendToAc();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void heatSwitch()
    {
        heatSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        sendToHeating();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void calendarSwitch()
    {
        dbSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        sendToCalendar();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void setupSwitch()
    {
        setupSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        sendToSetup();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void logoutSwitch()
    {
        logoutSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        sendToLogin();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void sendToLights(){
        Intent lightIntent = new Intent(portalActivity.this,MainActivity.class);
        startActivity(lightIntent); //bring up lights
    }
    public void sendToAc(){
        Intent acIntent = new Intent(portalActivity.this,AcActivity.class);
        startActivity(acIntent); //bring up AC
    }
    public void sendToHeating(){
        Intent heatIntent = new Intent(portalActivity.this,heatingActivity.class);
        startActivity(heatIntent); //bring up Heat
    }
    public void sendToCalendar(){
        Intent calendarActivity = new Intent(portalActivity.this,firstActivity.class);
        startActivity(calendarActivity); //bring up calendar
    }
    public void sendToSetup(){
        Intent setupIntent = new Intent(portalActivity.this,SetupActivity.class);
        startActivity(setupIntent); //bring up setup
    }
    private void logout(){
        //firebase sign out
        mAuth.signOut();
        //redirect to login
        sendToLogin();
    }
    public void sendToLogin(){
        Intent intent = new Intent(portalActivity.this, LoginActivity.class);
        startActivity(intent); //bring up login screen
        finish(); //not allow user to go back by pressing back button
    }
}

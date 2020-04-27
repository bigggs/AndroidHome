

#define WIFI_SSID "Gang Internet 2"
#define WIFI_PASSWORD "leonidas1"
#define FIREBASE_HOST "androidhome-6ae71.firebaseio.com"
#define FIREBASE_AUTH "uQx8eWsqfBoaCbpHA6AGm1H3BCmpMWA6LR13fhvo"
#include <ESP8266WiFi.h>                                                // esp8266 library
#include <FirebaseArduino.h>                                             // firebase library
#include <Servo.h>                                                      //motor library
#include<stdio.h>

//motor
Servo servorad1;

//lights
int light_one;                                                  
int light_two;                                                     
int light_three;                                                     
int light_four;   

int luxOne;
int luxTwo;
int luxThree;
int luxFour;

//motor                                                 
int rad;

//fan
int ACsetting;  




void setup() {

//set range and frequency
 analogWriteRange(100); //instead of 0 - 1024
   analogWriteFreq(10000);
   
  //LED pins
    pinMode(D0, OUTPUT);   //YELLOW LED  
    pinMode(D1, OUTPUT);   //BLUE LED
    pinMode(D2, OUTPUT);   //GREEN LED
    pinMode(D3, OUTPUT);   //WHITE LED



    
  Serial.begin(9600);
   //connect to wifi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  //sucess
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());                                                      //print local IP address
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);                                       // connect to firebase
  Serial.print("Connected to ");
  Serial.println(FIREBASE_HOST);

  //Set inital state (0 ON 1 OFF)
  Serial.println("Setting Initial states");
  Serial.println("...");
  

  Firebase.setInt("LIGHT_ONE", 1);                                         
  Firebase.setInt("LIGHT_TWO", 1);                                         
  Firebase.setInt("LIGHT_THREE", 1);                                         
  Firebase.setInt("LIGHT_FOUR", 1);  
  Firebase.setInt("lux1", 100); 
                                       
                                        

}


void loop() {
  //Loop get info from db and assign it locally

    rad = Firebase.getInt("motor1");  
    ACsetting = Firebase.getInt("AC"); 

  light_one = Firebase.getInt("LIGHT_ONE");  
  luxOne = Firebase.getInt("lux1");      
                               
  light_two = Firebase.getInt("LIGHT_TWO"); 
    luxTwo = Firebase.getInt("lux2");                                   
                                  
  light_three = Firebase.getInt("LIGHT_THREE");  
    luxThree = Firebase.getInt("lux3");                                   
                                    
  light_four = Firebase.getInt("LIGHT_FOUR");
    luxFour = Firebase.getInt("lux4");                                   


          //FAN
    analogWrite(D4, ACsetting);
    Serial.print("AC SPEED: ");
    Serial.print(ACsetting);
    Serial.println("%");
          //MOTOR
    servorad1.attach(14);
    servorad1.write(rad);
    Serial.print("Motor at angle: ");
    Serial.println(rad);
                                    
           //LIGHTS
     
  if (light_one == 0) {                                                          // compare the input of led status received from firebase
    Serial.print("Light ONE turned ON Brightness: ");   
    Serial.print(luxOne);
    Serial.println("%");                     
        analogWrite(D0, luxOne);   


    
  }
  else   if (light_one == 1) {                                                  // compare the input of led status received from firebase
    Serial.println("Light ONE turned OFF (D0)");                         
    analogWrite(D0, LOW);  
        
  }
    if (light_two == 0) {                                                          // compare the input of led status received from firebase
    Serial.print("Light TWO turned ON Brightness: ");   
    Serial.print(luxTwo);
    Serial.println("%");                     
    analogWrite(D1, luxTwo);   
    }    
      else   if (light_two == 1) {                                                  // compare the input of led status received from firebase
    Serial.println("Light TWO turned OFF (D1)");                         
    digitalWrite(D1, LOW);                                                         // make external led OFF
    
  } 
    if (light_three == 0) {                                                          // compare the input of led status received from firebase
    Serial.print("Light THREE turned ON Brightness: ");   
    Serial.print(luxThree);
    Serial.println("%");                     
    analogWrite(D2, luxThree);                                                        // make external led ON
    

    
  }
  else   if (light_three == 1) {                                                  // compare the input of led status received from firebase
    Serial.println("Light THREE turned OFF (D2)");                         
    digitalWrite(D2, LOW);                                                         // make external led OFF
    
  }
    if (light_four == 0) {                                                          // compare the input of led status received from firebase
    Serial.print("Light FOUR turned ON Brightness: ");   
    Serial.print(luxFour);
    Serial.println("%");                     
    analogWrite(D3, luxFour);                                                             // make external led ON
    

    
  }
  else if (light_four == 1) {                                                  // compare the input of led status received from firebase
    Serial.println("Light FOUR turned OFF (D3)");                         
    digitalWrite(D3, LOW);                                                         // make external led OFF
    
  }
    

  else {
    Serial.println("Error");
  }

}

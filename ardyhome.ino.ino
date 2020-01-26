

#define WIFI_SSID "////"
#define WIFI_PASSWORD "///"
#define FIREBASE_HOST "///"
#define FIREBASE_AUTH "////"
/* Controlling LED using Firebase console by CircuitDigest(www.circuitdigest.com) */
#include <ESP8266WiFi.h>                                                // esp8266 library
#include <FirebaseArduino.h>                                             // firebase library

String light_one = "";                                                     
String light_two = "";                                                     
  
String light_three = "";                                                     
String light_four = "";                                                    





void setup() {
  //pins being used, defined as OUTPUT
    pinMode(D0, OUTPUT);   //YELLOW LED  
    pinMode(D1, OUTPUT);   //BLUE LED
    pinMode(D2, OUTPUT);   //GREEN LED
    pinMode(D3, OUTPUT);   //WHITE LED



    
  Serial.begin(9600);
  delay(1000);
          
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);                                      
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());                                                     
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);                                       

  //Set inital state (0 ON 1 OFF)
  Firebase.setString("LIGHT_ONE", "1");                                         
  Firebase.setString("LIGHT_TWO", "1");                                         
  Firebase.setString("LIGHT_THREE", "1");                                         
  Firebase.setString("LIGHT_FOUR", "1");                                          

}

void loop() {
  //Loop getString from firebase and assign it to variables
  light_one = Firebase.getString("LIGHT_ONE");                                     
  light_two = Firebase.getString("LIGHT_TWO");                                    
  light_three = Firebase.getString("LIGHT_THREE");                                      
  light_four = Firebase.getString("LIGHT_FOUR");                                     

  if (light_one == "0") {                                                         
    Serial.println("Light ONE turned ON (D0)");                         
    digitalWrite(D0, HIGH);                                                        
    

    
  }
  else   if (light_one == "1") {                                                 
    Serial.println("Light ONE turned OFF (D0)");                         
    digitalWrite(D0, LOW);                                                        
    
  }
    if (light_two == "0") {                                                         
    Serial.println("Light TWO turned ON (D1)");                         
    digitalWrite(D1, HIGH);   
    }    
      else   if (light_two == "1") {                                                  
    Serial.println("Light TWO turned OFF (D1)");                         
    digitalWrite(D1, LOW);                                                         
    
  }// make external led ON
    if (light_three == "0") {                                                          
    Serial.println("Light THREE turned ON (D2)");                         
    digitalWrite(D2, HIGH);                                                         
    

    
  }
  else   if (light_three == "1") {                                                  
    Serial.println("Light THREE turned OFF (D2)");                         
    digitalWrite(D2, LOW);                                                        
    
  }
    if (light_four == "0") {                                                          
    Serial.println("Light FOUR turned ON (D3)");                         
    digitalWrite(D3, HIGH);                                                        
    

    
  }
  else   if (light_four == "1") {                                                  
    Serial.println("Light FOUR turned OFF (D3)");                         
    digitalWrite(D3, LOW);                                                         
  }
    




    
   

  else {
    Serial.println("Error");
  }
}

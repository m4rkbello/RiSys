#if defined(ESP32)
#include <WiFi.h>
#include <FirebaseESP32.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#endif 

#include <addons/TokenHelper.h> 
#include <addons/RTDBHelper.h> 
#define API_KEY "AIzaSyAMe193JtamVePNjIiMRvvnGANwhOsNSX0" 
#define DATABASE_URL "https://prototypeproject-eeb91.firebaseio.com/"  
#define USER_EMAIL "arduino@gmail.com"
#define USER_PASSWORD "arduino123"  
FirebaseData fbdo; 
FirebaseAuth auth;
FirebaseConfig config; 

#define FIREBASE_HOST "prototypeproject-eeb91.firebaseio.com"
#define FIREBASE_AUTH "UCZ5lFED8KxcprRhIHzt1uk4ac2OEmhB1GfdTp6S"
#define WIFI_SSID "SKYfiber14AD" //name
#define WIFI_PASSWORD "jayceliampuzon" //pass


 
//3 secsduration 
#define flow_meter_sensor  D5 
const int us_trig_pin = 16;
const int us_echo_pin = 5;
 
const int ledRed = 0;  
const int wl_a_pin = A0; 


long currentMillis = 0;
long previousMillis = 0;
int interval = 1000; 
float calibrationFactor = 4.5;
volatile byte pulseCount;
byte pulse1Sec = 0;
float flowRate;
unsigned int flowMilliLitres;
unsigned long totalMilliLitres;
void IRAM_ATTR pulseCounter()
{
  pulseCount++;
}
void setup() {
  Serial.begin(115200); 
  
//  US config
  pinMode(us_trig_pin,OUTPUT);
  pinMode(us_echo_pin,INPUT);
   
  pinMode(ledRed,OUTPUT); 

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) { 
    Serial.print(".");
    digitalWrite(ledRed,LOW);
    delay(500);
    digitalWrite(ledRed,HIGH);
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION); 
  config.api_key = API_KEY; 
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD; 
  config.database_url = DATABASE_URL; 
  config.token_status_callback = tokenStatusCallback; 
  Firebase.begin(&config, &auth); 
  Firebase.reconnectWiFi(true);
  Serial.print("Starting Firebase");
  for(int i = 0; i < 1; i++){
    Serial.print(".");
    digitalWrite(ledRed,LOW);
    delay(500);
    digitalWrite(ledRed,HIGH);
    delay(500);
  } 
      
  Serial.println("started");   
  digitalWrite(ledRed,LOW);
   
  //flow meter init
  pinMode(flow_meter_sensor, INPUT_PULLUP);
  pulseCount = 0;
  flowRate = 0.0;
  flowMilliLitres = 0;
  totalMilliLitres = 0;
  previousMillis = 0;
  attachInterrupt(digitalPinToInterrupt(flow_meter_sensor), pulseCounter, FALLING);
}

void loop() { 
  flowMeter();  
//  calWl(); 
//  calUs(); 
}

//long duration;
//int distance;
//void calUs(){ 
//  // Clears the us_trig_pin
//  digitalWrite(us_trig_pin, LOW);
//  delayMicroseconds(2);
//  // Sets the us_trig_pin on HIGH state for 10 micro seconds
//  digitalWrite(us_trig_pin, HIGH);
//  delayMicroseconds(10);
//  digitalWrite(us_trig_pin, LOW);
//  // Reads the us_echo_pin, returns the sound wave travel time in microseconds
//  duration = pulseIn(us_echo_pin, HIGH);
//  // Calculating the distance
//  distance= duration*0.034/2;
//  // Prints the distance on the Serial Monitor
//  Serial.print("Distance: ");
//  Serial.println(distance);
//  if(distance <= 8){
//    isUSDanger = true;
//  }
//  Firebase.setFloat("distance", distance); 
//}

int level = 1;
void calWl(){
  int resval = analogRead(wl_a_pin); 
  Serial.println(resval); 
  Firebase.setFloat(fbdo, F("/waterIrrigation/waterLevel"), resval);
}
 
void flowMeter(){ 
  currentMillis = millis();
  if (currentMillis - previousMillis > interval) {
    pulse1Sec = pulseCount;
    pulseCount = 0;
    flowRate = ((1000.0 / (millis() - previousMillis)) * pulse1Sec) / calibrationFactor;
    previousMillis = millis();
    flowMilliLitres = (flowRate / 60) * 1000;
    totalMilliLitres += flowMilliLitres;
    // Print the flow rate for this second in litres / minute
    Serial.print("Flow rate: ");
    Serial.print(int(flowRate));  // Print the integer part of the variable
    Firebase.setFloat(fbdo, F("/waterIrrigation/flRate"), flowRate);
    Serial.print("L/min");
    Serial.print("\t");       // Print tab space
    // Print the cumulative total of litres flowed since starting
    Serial.print("Output Liquid Quantity: ");
    Serial.print(totalMilliLitres);   
    Firebase.setFloat(fbdo, F("/waterIrrigation/flmL"), totalMilliLitres);
    Serial.print("mL / ");
    Serial.print(totalMilliLitres / 1000); 
    Firebase.setFloat(fbdo, F("/waterIrrigation/flL"), totalMilliLitres/1000);
    Serial.println("L"); 
  }
}

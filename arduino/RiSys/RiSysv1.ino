#include <Servo.h> 

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
#define WIFI_SSID "WirelessFidelity" //name
#define WIFI_PASSWORD "REVAN1130" //pass

Servo mainG;  
Servo gateA;  
Servo gateB;   
Servo gateC;   

long sendDataCurrentMillis = 0;   
long sendDataPreviousMillis = 0; 

int led1 = 15; 
int led2 = 5; 
int servoPositionClose = 0;      // servo degree
int servoPositionOpen = 180;      // servo degree

int posMain = 180;   
int posGateA = 180;  
int posGateB = 0;  
int posGateC = 0;     

bool isMain = true;
bool isGateA = true;
bool isGateB = true;
bool isGateC = true;

bool isMainRun = true;
bool isGateARun = true;
bool isGateBRun = true;
bool isGateCRun = true;


#define water_level_sensor  A0 

#define flow_meter_sensor  D7 
long currentMillis = 0;
long previousMillis = 0; 
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
  mainG.attach(16);  
  gateA.attach(0);   
  gateB.attach(14);   
  gateC.attach(12);  
    
  int pos = 0;  
  for (pos = 180; pos >= 0 ; pos -= 10) {  
    mainG.write(pos); 
    gateA.write(pos);  
    gateB.write(pos); 
    gateC.write(pos);        
    delay(15);                   
  }  

  pinMode(led1,OUTPUT); 
  pinMode(led2,OUTPUT); 
  digitalWrite(led2,HIGH);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) { 
    Serial.print(".");
    digitalWrite(led1,LOW);
    delay(500);
    digitalWrite(led1,HIGH);
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
    digitalWrite(led1,LOW);
    delay(500);
    digitalWrite(led1,HIGH);
    delay(500);
  } 
      
  //flow meter init
  pinMode(flow_meter_sensor, INPUT_PULLUP);
  pulseCount = 0;
  flowRate = 0.0;
  flowMilliLitres = 0;
  totalMilliLitres = 0;
  previousMillis = 0;
  attachInterrupt(digitalPinToInterrupt(flow_meter_sensor), pulseCounter, FALLING);
  
  Serial.println("started");   
  digitalWrite(led1,LOW);
} 
bool isRun = false;
void loop() {  
  loopWaterLevel();
  loopFlowMeter();   
  loopGetData(); 
} 

void loopGetData(){
  sendDataCurrentMillis = millis();
  if (sendDataCurrentMillis - sendDataPreviousMillis > 1000) { 
    sendDataPreviousMillis = millis();
    int fMainGate = 0; Firebase.getInt(fbdo, F("/waterIrrigation/mainGate"),&fMainGate);
    int fGateA  = 0; Firebase.getInt(fbdo, F("/waterIrrigation/gateA"),&fGateA);
    int fGateB  = 0; Firebase.getInt(fbdo, F("/waterIrrigation/gateB"),&fGateB); 
    int fGateC  = 0; Firebase.getInt(fbdo, F("/waterIrrigation/gateC"),&fGateC);  
    Serial.printf("Get MainGate... %s\n",String(fMainGate).c_str()); 
    Serial.printf("Get GateA... %s\n",String(fGateA).c_str()); 
    Serial.printf("Get GateB... %s\n",String(fGateB).c_str()); 
    Serial.printf("Get GateC... %s\n",String(fGateC).c_str());

    isMain = fMainGate == 1 ? true : false;
    isGateA = fGateA == 1 ? true : false;
    isGateB = fGateB == 1 ? true : false;
    isGateC = fGateC == 1 ? true : false;  

    if(isRun){
      digitalWrite(led1,HIGH);
      if(isMain != isMainRun){
        if(fMainGate == 1 ? true : false){ 
          for (posMain = 180; posMain >= 0; posMain -= 5) {   
            mainG.write(posMain);               
            delay(10);                       
          } 
        }else{  
          for (posMain = 0; posMain <= 180; posMain += 5) { 
            mainG.write(posMain);          
            delay(10);                   
          }
        } 
        isMainRun = isMain;
      }

      if(isGateA != isGateARun){
        if(fGateA == 1 ? true : false){ 
            for (posGateA = 180; posGateA >= 0; posGateA -= 10) { 
              gateA.write(posGateA);          
              delay(15);        
          } 
        }else{  
             for (posGateA = 0; posGateA <= 180; posGateA += 10) {   
              gateA.write(posGateA);               
              delay(15);                  
          }
        }
        isGateARun = isGateA;
      }  


      if(isGateB != isGateBRun){
        if(fGateB == 1 ? true : false){ 
          for (posGateB = 0; posGateB <= 180; posGateB += 10) {   
            gateB.write(posGateB);               
            delay(15);                       
          } 
        }else{  
          for (posGateB = 180; posGateB >= 0; posGateB -= 10) { 
            gateB.write(posGateB);          
            delay(15);                   
          }
        }
        isGateBRun = isGateB;
      }  
      
      
      if(isGateC != isGateCRun){
        if(fGateC == 1 ? true : false){ 
          for (posGateC = 0; posGateC <= 180; posGateC += 10) {   
            gateC.write(posGateC);               
            delay(15);                       
          } 
        }else{  
          for (posGateC = 180; posGateC >= 0; posGateC -= 10) { 
            gateC.write(posGateC);          
            delay(15);                   
          }
        }    
        isGateCRun = isGateC;
      }
    }
    isRun = true;

    digitalWrite(led1,LOW); 
    Serial.println("========");

    
    int fReset = 0; Firebase.getInt(fbdo, F("/waterIrrigationReset/isReset"),&fReset);
    bool isReset = fReset == 1 ? true : false;
    if(isReset){ 
        resetFlowMeter();
        Firebase.setInt(fbdo, F("/waterIrrigationReset/isReset"), 0); 
    }  
  } 
}


void loopFlowMeter(){ 
  currentMillis = millis();
  if (currentMillis - previousMillis > 1000) {
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

void resetFlowMeter(){
    previousMillis = 0; 
    pulse1Sec = 0;
    totalMilliLitres = 0;
    calibrationFactor = 4.5;
    pulseCount = 0; 
    flowRate = 0.0;
    flowMilliLitres = 0;  
    attachInterrupt(digitalPinToInterrupt(flow_meter_sensor), pulseCounter, FALLING);
}

void loopWaterLevel(){ 
  int waterLevel = analogRead(water_level_sensor); 
  
  Firebase.setInt(fbdo, F("/waterIrrigation/waterLevel"), waterLevel); 
}

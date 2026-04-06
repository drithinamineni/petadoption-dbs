# 🐾 Pet Adoption System

A JavaFX desktop application connected to Oracle Database 23c 
that manages pet adoptions, adopters, and adoption requests.

## Features
- View all available pets
- View registered adopters
- Submit adoption requests
- Approve requests and mark pets as adopted
- Medical Records for each pet
- Bar graph analytics showing which breed and what age are most likely to be adopted

## Technologies Used
- Java 17
- JavaFX 17
- Oracle Database 23c Free
- JDBC (ojdbc17)

## Setup Instructions

### 1. Install Requirements
- Java JDK 17
- JavaFX SDK 17
- Oracle Database 23c Free
- ojdbc17.jar

## setup structure
```
petadoption/
 -javafx-sdk-17.0.18/
   -src/
     -main.java
     - DBConnection.java
     - adopter.java
     - adoptionRequest.java
     - pet.java
     - dbs.sql
     - ojdbc17.jar
   -lib/
     - ojdbc17.jar
```



### 2. Setup Database
Open SQL Plus and run:
sqlplus system/yourpassword@localhost:1521/XEPDB1
@dbs.sql

### 3. Compile
cd /Users/drithi/Desktop/petadoption/src 
javac --module-path /Users/drithi/Downloads/javafx-sdk-17.0.18/lib \
      --add-modules javafx.controls,javafx.fxml \
      -cp .:ojdbc17.jar \
      DBConnection.java pet.java adopter.java adoptionRequest.java medicalRecord.java main.java
      
### 4. Run
java --module-path /Users/drithi/Downloads/javafx-sdk-17.0.18/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp .:ojdbc17.jar \
     main

## Database Schema
- Shelter
- Pet
- Adopter
- Adoption_Request
- Medical_Record

## Author
Drithi

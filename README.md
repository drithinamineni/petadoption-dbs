# 🐾 Pet Adoption System

A JavaFX desktop application connected to Oracle Database 23c 
that manages pet adoptions, adopters, and adoption requests.

## Features
- View all available pets
- View registered adopters
- Submit adoption requests
- Approve requests and mark pets as adopted

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

### 2. Setup Database
Open SQL Plus and run:
sqlplus system/yourpassword@localhost:1521/FREEPDB1
@dbs.sql

### 3. Compile
javac --module-path /path/to/javafx/lib \
      --add-modules javafx.controls,javafx.fxml \
      -cp .:ojdbc17.jar \
      DBConnection.java pet.java adopter.java adoptionRequest.java main.java

### 4. Run
java --module-path /path/to/javafx/lib \
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

CREATE TABLE Shelter (
    shelter_id NUMBER PRIMARY KEY,
    name VARCHAR2(50) not null,
    location VARCHAR2(100),
    contact VARCHAR2(15)
);

CREATE TABLE Pet (
    pet_id NUMBER PRIMARY KEY,
    name VARCHAR2(50) not null,
    breed VARCHAR2(50),
    age NUMBER,
    gender VARCHAR2(10),
    health_status VARCHAR2(50),
    vaccination_status VARCHAR2(50),
    shelter_id NUMBER,
    status VARCHAR2(20) CHECK (status IN ('Available', 'Adopted')),
    FOREIGN KEY (shelter_id) REFERENCES Shelter(shelter_id) on delete cascade
);

--Adopter Table
CREATE TABLE Adopter (
    adopter_id NUMBER PRIMARY KEY,
    name VARCHAR2(50) not null,
    phone VARCHAR2(15),
    address VARCHAR2(100)
);

--Adoption Request Table
CREATE TABLE Adoption_Request (
    request_id NUMBER PRIMARY KEY,
    adopter_id NUMBER,
    pet_id NUMBER,
    request_date DATE,
    status VARCHAR2(20) CHECK (status IN ('Pending', 'Approved')),
    FOREIGN KEY (adopter_id) REFERENCES Adopter(adopter_id) on delete cascade,
    FOREIGN KEY (pet_id) REFERENCES Pet(pet_id) on delete cascade
);

-- Medical Records Table
CREATE TABLE Medical_Record (
    record_id NUMBER PRIMARY KEY,
    pet_id NUMBER,
    treatment VARCHAR2(100),
    treatment_date DATE,
    FOREIGN KEY (pet_id) REFERENCES Pet(pet_id)on delete cascade
);

INSERT INTO Shelter VALUES (1, 'Happy Tails', 'Bangalore', '9876543210');
INSERT INTO Shelter VALUES (2, 'Safe Paws', 'Mangalore', '9123456780');
INSERT INTO Shelter VALUES (3, 'Paws Haven', 'Udupi', '9012345678');

INSERT INTO Pet VALUES (101, 'Tommy', 'Labrador', 3, 'Male', 'Healthy', 'Vaccinated', 1, 'Available');
INSERT INTO Pet VALUES (102, 'Kitty', 'Persian Cat', 2, 'Female', 'Injured', 'Not Vaccinated', 2, 'Available');
INSERT INTO Pet VALUES (103, 'Bruno', 'German Shepherd', 4, 'Male', 'Healthy', 'Vaccinated', 1, 'Available');
INSERT INTO Pet VALUES (104, 'Lucy', 'Beagle', 1, 'Female', 'Healthy', 'Vaccinated', 3, 'Available');
INSERT INTO Pet VALUES (105, 'Max', 'Bulldog', 5, 'Male', 'Sick', 'Vaccinated', 2, 'Available');
INSERT INTO Pet VALUES (106, 'Bella', 'Pug', 2, 'Female', 'Healthy', 'Vaccinated', 1, 'Available');
INSERT INTO Pet VALUES (107, 'Charlie', 'Golden Retriever', 3, 'Male', 'Healthy', 'Vaccinated', 3, 'Available');
INSERT INTO Pet VALUES (108, 'Luna', 'Siamese Cat', 1, 'Female', 'Healthy', 'Not Vaccinated', 2, 'Available');
INSERT INTO Pet VALUES (109, 'Rocky', 'Doberman', 6, 'Male', 'Injured', 'Vaccinated', 3, 'Available');
INSERT INTO Pet VALUES (110, 'Milo', 'Indie Dog', 2, 'Male', 'Healthy', 'Vaccinated', 1, 'Available');
INSERT INTO Pet VALUES (111, 'Buddy', 'Labrador', 4, 'Male', 'Healthy', 'Vaccinated', 2, 'Available');
INSERT INTO Pet VALUES (112, 'Daisy', 'Beagle', 2, 'Female', 'Healthy', 'Vaccinated', 1, 'Available');
INSERT INTO Pet VALUES (113, 'Simba', 'Persian Cat', 3, 'Male', 'Healthy', 'Vaccinated', 3, 'Available');
INSERT INTO Pet VALUES (114, 'Coco', 'Pug', 1, 'Female', 'Healthy', 'Not Vaccinated', 2, 'Available');
INSERT INTO Pet VALUES (115, 'Leo', 'German Shepherd', 5, 'Male', 'Injured', 'Vaccinated', 1, 'Available');
INSERT INTO Pet VALUES (116, 'Molly', 'Labrador', 2, 'Female', 'Healthy', 'Vaccinated', 3, 'Available');

INSERT INTO Adopter VALUES (201, 'Rahul', '9999999999', 'Udupi');
INSERT INTO Adopter VALUES (202, 'Sneha', '8888888888', 'Manipal');
INSERT INTO Adopter VALUES (203, 'Arjun', '7777777777', 'Mangalore');

INSERT INTO Adoption_Request VALUES (301, 201, 101, SYSDATE, 'Pending');
INSERT INTO Adoption_Request VALUES (302, 202, 102, SYSDATE, 'Pending');
INSERT INTO Adoption_Request VALUES (303, 203, 104, SYSDATE, 'Pending');

INSERT INTO Medical_Record VALUES (401, 101, 'Vaccination - Rabies', SYSDATE);
INSERT INTO Medical_Record VALUES (402, 102, 'Leg Injury Treatment', SYSDATE);
INSERT INTO Medical_Record VALUES (403, 105, 'Fever Medication', SYSDATE);
INSERT INTO Medical_Record VALUES (404, 109, 'Wound Dressing', SYSDATE);

CREATE OR REPLACE PROCEDURE adopt_pet (
    p_pet_id NUMBER
) AS
BEGIN
    UPDATE Pet
    SET status = 'Adopted'
    WHERE pet_id = p_pet_id;

    COMMIT;
END;
/
BEGIN
    adopt_pet(101);
END;
/


CREATE OR REPLACE FUNCTION count_pets (
    s_id NUMBER
) RETURN NUMBER
IS
    total NUMBER;
BEGIN
    SELECT COUNT(*) INTO total
    FROM Pet
    WHERE shelter_id = s_id;

    RETURN total;
END;
/

SELECT count_pets(1) FROM dual;

CREATE OR REPLACE TRIGGER update_pet_status
AFTER UPDATE ON Adoption_Request
FOR EACH ROW
WHEN (NEW.status = 'Approved')
BEGIN
    UPDATE Pet
    SET status = 'Adopted'
    WHERE pet_id = :NEW.pet_id;
END;
/





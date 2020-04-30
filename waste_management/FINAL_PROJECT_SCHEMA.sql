DROP TABLE If EXISTS Waste_Type;
DROP TABLE If EXISTS Company;
DROP TABLE If EXISTS Site;
DROP TABLE If EXISTS Pickup;


CREATE TABLE Waste_Type (
	id INTEGER PRIMARY KEY AUTOINCREMENT, 
	name VARCHAR(32) NOT NULL
); 


CREATE TABLE Company (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(32) NOT NULL,
	address VARCHAR(128) NOT NULL,
	description TEXT
);

CREATE TABLE Site (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(32) NOT NULL,
	address VARCHAR(128) NOT NULL
);

CREATE TABLE Pickup (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	weight DECIMAL(8,3),
	date DATE,
	site_Id  INTEGER,
	waste_type_Id INTEGER,
	company_id INTEGER,
	smalldate CHAR(6),

	CONSTRAINT fk_column FOREIGN KEY (site_Id) REFERENCES Site (id),
	CONSTRAINT fk_column FOREIGN KEY (company_id) REFERENCES Company (id),
	CONSTRAINT fk_column FOREIGN KEY (waste_type_Id) REFERENCES Waste_Type (id)
);


INSERT INTO Company (name, address) VALUES 
	("ACE", "2274 Technology Dr, West Valley City, UT 84119"),
	("METech", "2350 W Bridger Road, Salt Lake City, UT 84101"),
	("METech Recycling", "6200 Engle Way, Gilroy, CA 95020");

INSERT INTO Site (name, address) VALUES
	("Westminster//RYC-Recycling", "1700 S 1300 E"),
	("Westminster//East", "1700 S 1300 E"),
	("Westminster//West", "1700 S 1300 E"),
	("Westminster//Hogle", "1700 S 1300 E"),
	("Garfield Home", "1173 Garfield Ave");

INSERT INTO Waste_Type (name) VALUES
	("Recycling"),
	("Trash"),
	("Cardboard"),
	("C&D"),
	("Food"),
	("E-Waste");

INSERT INTO Pickup (weight, date, site_Id, waste_type_Id, company_id, smalldate) VALUES
	(300, '2015-09-15', 1, 1, 1, 201509),
	(2005, '2018-12-31', 1, 1, 1, 201812),
	(6900, '1996-08-11', 3, 2, 1, 199608),
	(1200, '2015-09-23', 1, 1, 1, 201509),
	(640, '2018-12-04', 1, 1, 1, 201812),
	(3500, '1996-08-09', 3, 2, 1, 199608);

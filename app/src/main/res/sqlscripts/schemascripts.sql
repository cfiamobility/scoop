/*
 *	Description: 	All the sql scripts for the database for the SCOOP mobile application
 *	File Name:		schemascripts.sql
 *	Created by:		Mobility Team, IBSDB, CFIA
 *	Created date:	2019-03-13 (YYYY-MM-DD)
 *	Modifications:	
*/

/*----------------------------------------START OF FUNCTIONS, TRIGGERS----------------------------------------*/

/* Install the uuid generator*/
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

/* Auto Update Timestamp Function */
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

/* Update Time Trigger */]
CREATE TRIGGER update_customer_modtime BEFORE UPDATE ON customer FOR EACH ROW EXECUTE PROCEDURE  update_modified_column();


/*----------------------------------------END OF FUNCTIONS AND TRIGGERS----------------------------------------*/


/*------------------------------------------------------------START OF TABLES------------------------------------------------------------*/

/* This table contains the names (in French and English) of the divisions of CFIA */
CREATE TABLE divisions(
	divisionid serial PRIMARY KEY,
	division_en varchar(255),
	division_fr varchar(255),
	createddate TIMESTAMPTZ DEFAULT NOW(),
	modifieddated TIMESTAMPTZ DEFAULT NOW()
);

/* This table contains all the information relevant to the users */
CREATE TABLE users(
	userid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
	firstname varchar(255),
	lastname varchar(255),
	email varchar(255),
	password varchar(255),
	dateofbirth integer,
	gender integer,
	buildingid integer,
	profileimage varchar(255),
	address varchar(255),
	city varchar(255),
	province varchar(2),
	postalcode varchar(6),
	createddate TIMESTAMPTZ DEFAULT NOW(),
	modifieddated TIMESTAMPTZ DEFAULT NOW(),
	modifiedby integer
);

/* This table contains the positions and divisions the associated user works for */
CREATE TABLE roles(
	roleid serial PRIMARY KEY,
	userid uuid REFERENCES users(userid),
	divisionid integer REFERENCES divisions(divisionid),
	positionname varchar(255)
);

/* This table contains the names (in French and English) of the buildings that CFIA servants work in/at */
CREATE TABLE buildings(
	buildingid serial PRIMARY KEY,
	buildingname_en varchar(255),
	buildingname_fr varchar(255),
	address varchar(255),
	city varchar(255),
	province varchar(2),
	postalcode varchar(6)
);

/* This table contains the messages and who they are sent by */
CREATE TABLE messages(
	messageid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
	userid uuid REFERENCES users(userid),
	messagetype integer
);

/* This table contains the url of social media associated with a user */
CREATE TABLE socialmedia (
	socialmediaid serial PRIMARY KEY,
	userid uuid REFERENCES users(userid),
	url varchar(255)
);

/* This table contains all the notifications that are sent/received */
CREATE TABLE notifications (
	notificationid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
	messageid uuid REFERENCES messages(messageid),
	userid uuid REFERENCES users(userid),
	notificationstatus integer,
	createddate TIMESTAMPTZ DEFAULT NOW(),
	modifieddated TIMESTAMPTZ DEFAULT NOW()
)

/* This table contains all the saved posts by each individual user */
CREATE TABLE savedposts (
	savedpostid serial PRIMARY KEY,
	messageid uuid REFERENCES messages(messageid),
	userid uuid REFERENCES users(userid),
	activestatus integer
);

/* This table contains all the previous posts searched by each individual user */
CREATE TABLE searchhistory (
	searchhistoryid serial PRIMARY KEY,
	messageid uuid REFERENCES messages(messageid),
	userid uuid REFERENCES users(userid),
	activestatus integer,
	createddate TIMESTAMPTZ DEFAULT NOW(),
	modifieddate TIMESTAMPTZ DEFAULT NOW()
);

/* This table contains all the reports made against posts */
CREATE TABLE reports(
	reportid serial PRIMARY KEY,
	messageid uuid REFERENCES messages(messageid),
	userid uuid REFERENCES users(userid),
	reason varchar(255)
);

/* This table contains all the posts (scoops or comments) of each indiviual user */
CREATE TABLE posts (
	postid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
	messageid uuid REFERENCES messages(messageid),
	activestatus integer,
	posttype integer,
	postmessage varchar(255),
	postimage varchar(255),
	posttitle varchar(255),
	createddate TIMESTAMPTZ DEFAULT NOW(),
	modifieddate TIMESTAMPTZ DEFAULT NOW()
);

/* This table contains all the likes made within th app */
CREATE TABLE likes (
	likeid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
	liketype integer,
	createddate TIMESTAMPTZ DEFAULT NOW(),
	modifieddate TIMESTAMPTZ DEFAULT NOW()
)

/*------------------------------------------------------------END OF TABLES------------------------------------------------------------*/


/*------------------------------------------------------------START OF TEST INSERTS------------------------------------------------------------*/

INSERT INTO users VALUES (DEFAULT, 'timmy', 'truong', 'timmy.truong@canada.ca', '123456', 07012000, 0, 0, 'image', '133 Trail Boulevard', 'Springwater TWP', 'ON', 'L9X0S7', DEFAULT, DEFAULT, 0); 

INSERT INTO divisions VALUES (DEFAULT, 'Innovation, Business and Service Development Branch', 'Direction de l''innovation, des analytiques et des solutions numériques', DEFAULT, DEFAULT);

INSERT INTO roles(userid, divisionid, positionname)
SELECT users.userid, divisions.divisionid, 'Mobile Application Developer'
FROM users, divisions
WHERE users.userid = 'a593e33b-e544-4f8b-ad50-7132fdc937f3' OR divisions.divisionid = 1;


/*------------------------------------------------------------END OF TEST TABLES------------------------------------------------------------*/

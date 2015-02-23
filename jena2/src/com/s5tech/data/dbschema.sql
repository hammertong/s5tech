--
-- HSQLDB TINYDB INITIALIZATION
--
-- CREATE SEQUENCE MSGID_SEQUENCE AS BIGINT START WITH 1 INCREMENT BY 1 CYCLE;

CREATE TABLE esls (
	esl VARCHAR(16) PRIMARY KEY, 
	coordinatorMac VARCHAR(16), 
	installationKey VARBINARY(8),
	eslType VARCHAR(18),
	eslShortAddress INTEGER,
	alarmMode INTEGER
);

CREATE TABLE eslTypes (
	eslType VARCHAR(12) PRIMARY KEY, 
	networkEquivalentType INTEGER 
);

CREATE TEXT TABLE displayTypes (
	displayType INTEGER PRIMARY KEY,
	isDotMatrix INTEGER
);

CREATE TABLE pricesList (
	esl VARCHAR(16) PRIMARY KEY, 
	processed INTEGER,
	activeUpdateId BIGINT, 
	activeRefId VARCHAR(32),
	activePublishingDate TIMESTAMP, 
	activeHash BIGINT, 
	activePrice VARCHAR(2048),
	pendingUpdateId BIGINT, 
	pendingRefId VARCHAR(32),
	pendingPublishingDate TIMESTAMP, 
	pendingHash BIGINT, 
	pendingPrice VARCHAR(2048)
);

INSERT INTO eslTypes (eslType, networkEquivalentType) values ('DM0567YT', 1);
INSERT INTO eslTypes (eslType, networkEquivalentType) values ('DM0558YT', 2);
INSERT INTO eslTypes (eslType, networkEquivalentType) values ('DM0568YT', 3);
INSERT INTO eslTypes (eslType, networkEquivalentType) values ('EG020AS012', 4);
INSERT INTO eslTypes (eslType, networkEquivalentType) values ('ESL50', 5);
INSERT INTO eslTypes (eslType, networkEquivalentType) values ('ESL70', 6);


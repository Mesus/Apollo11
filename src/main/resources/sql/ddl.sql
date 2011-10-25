CREATE TABLE IF NOT EXISTS EMPLOYEE (
	name 	varchar(100) not null primary key
);

CREATE TABLE IF NOT EXISTS MONTH (
	month_name varchar(10) not null primary key
				check(month_name in ('Januar', 'Februar', 'Mars', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Desember')),
	month_number int not null unique
);

CREATE TABLE IF NOT EXISTS ACTIVITY (
	activity_name varchar(100) not null,
	employee_name varchar(100) not null,
	month_name varchar(10) not null,
	the_year int not null,
	FOREIGN KEY (activity_name)
		REFERENCES ACTIVITY_TYPE (activity_name),
	FOREIGN KEY (employee_name)
		REFERENCES EMPLOYEE (name),
	FOREIGN KEY (month_name)
		REFERENCES MONTH (month_name),
	primary key (activity_name, employee_name, month_name, the_year)
);

CREATE TABLE IF NOT EXISTS ACTIVITY_TYPE (
	activity_name varchar(100) not null,
	act_type varchar(100) not null,
	isnumeric boolean,
	isvisible boolean,
	primary key(activity_name, act_type)
);


CREATE TABLE IF NOT EXISTS USER (
    email VARCHAR(100) NOT NULL PRIMARY KEY,
    userrole VARCHAR(100) NOT NULL,
    username VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS USER_REQUESTS (
    email VARCHAR(100) NOT NULL PRIMARY KEY,
    username VARCHAR(100)
);


INSERT IGNORE INTO USER VALUES('glenn.bech@gmail.com', 'Admin', 'Glenn Bech');
INSERT IGNORE INTO USER VALUES('gurilunnan@gmail.com', 'Admin', 'Guri Lunnan');

INSERT IGNORE INTO MONTH VALUES ('Januar', 1);
INSERT IGNORE INTO MONTH VALUES ('Februar', 2);
INSERT IGNORE INTO MONTH VALUES ('Mars', 3);
INSERT IGNORE INTO MONTH VALUES ('April', 4);
INSERT IGNORE INTO MONTH VALUES ('Mai', 5);
INSERT IGNORE INTO MONTH VALUES ('Juni', 6);
INSERT IGNORE INTO MONTH VALUES ('Juli', 7);
INSERT IGNORE INTO MONTH VALUES ('August', 8);
INSERT IGNORE INTO MONTH VALUES ('September', 9);
INSERT IGNORE INTO MONTH VALUES ('Oktober', 10);
INSERT IGNORE INTO MONTH VALUES ('November', 11);
INSERT IGNORE INTO MONTH VALUES ('Desember', 12);

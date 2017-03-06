create database if not exists social_data;

use social_data;

drop table if exists Friends;
drop table if exists RequestStatus;
drop table if exists Users;


/* Users Table */
create table Users (
   username    varchar(32) NOT NULL,
   password    varchar(32) NOT NULL,
   birthday    varchar(32),
   first_name  varchar(32),
   last_name   varchar(32),
   gender      varchar(32),
   login       int NOT NULL,
   primary key (username));

insert into Users values ('Smileyman5', 'password', '03/16/1996', 'Alex', 'Kouthoofd', 'male', 0);
insert into Users values ('JonMan', 'password', '02/18/1995', 'Jon', 'Baker', 'male', 0);
insert into Users values ('Dummy', 'password', '12/12/1990', 'Dum', 'Me', 'male', 0);
insert into Users values ('JesGirl', 'password', '06/08/1993', 'Jess', 'Smith', 'female', 0);
insert into Users values ('DaisyDuke', 'password', '11/21/1995', 'Daisy', 'Duke', 'female', 0);

SELECT * FROM Users;

/* Request Status Table */
create table RequestStatus (
  status      varchar(32),
  primary key (status));

INSERT INTO RequestStatus VALUES ('Pending');
INSERT INTO RequestStatus VALUES ('Confirmed');


/* Friends Table */
create table Friends (
   username    varchar(32),
   friend      varchar(32),
   status      varchar(32),
   FOREIGN KEY (username) REFERENCES Users(username),
   FOREIGN KEY (friend) REFERENCES Users(username),
   FOREIGN KEY (status) REFERENCES RequestStatus(status),
   primary key (username, friend));

insert into Friends values ('Smileyman5', 'JonMan', 'Confirmed');
insert into Friends values ('JonMan', 'Smileyman5', 'Confirmed');
insert into Friends values ('Dummy', 'JesGirl', 'Confirmed');
insert into Friends values ('JesGirl', 'Dummy', 'Confirmed');
insert into Friends values ('Smileyman5', 'JesGirl', 'Pending');
insert into Friends values ('JonMan', 'DaisyDuke', 'Pending');

select * from Friends;
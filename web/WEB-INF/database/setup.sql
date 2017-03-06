create database if not exists social_data;

use social_data;

drop table if exists Friends;
drop table if exists RequestStatus;
drop table if exists Users;

/* Users Table */
create table Users (
   id          int NOT NULL AUTO_INCREMENT,
   username    varchar(32) NOT NULL,
   password    varchar(32) NOT NULL,
   birthday    varchar(32),
   first_name  varchar(32),
   last_name   varchar(32),
   gender      varchar(32),
   login       int NOT NULL,
   primary key (id));

insert into Users values (NULL, 'Smileyman5', 'password', '03/16/1996', 'Alex', 'Kouthoofd', 'male', 0);
insert into Users values (NULL, 'JonMan', 'password', '02/18/1995', 'Jon', 'Baker', 'male', 0);
insert into Users values (NULL, 'Dummy', 'password', '12/12/1990', 'Dum', 'Me', 'male', 0);
insert into Users values (NULL, 'JesGirl', 'password', '06/08/1993', 'Jess', 'Smith', 'female', 0);
insert into Users values (NULL, 'DaisyDuke', 'password', '11/21/1995', 'Daisy', 'Duke', 'female', 0);

SELECT * FROM Users;

/* Request Status Table */
create table RequestStatus (
  status      varchar(32),
  primary key (status));

INSERT INTO RequestStatus VALUES ('Pending');
INSERT INTO RequestStatus VALUES ('Confirmed');


/* Friends Table */
create table Friends (
   id          int not null AUTO_INCREMENT,
   username    varchar(32),
   friend      varchar(32),
   status      varchar(32),
   FOREIGN KEY (username) REFERENCES Users(username),
   FOREIGN KEY (friend) REFERENCES Users(username),
   FOREIGN KEY (status) REFERENCES RequestStatus(status),
   primary key (id));

insert into Friends values (NULL, 'Smileyman5', 'JonMan', 'Confirmed');
insert into Friends values (NULL, 'JonMan', 'Smileyman5', 'Confirmed');
insert into Friends values (NULL, 'Dummy', 'JesGirl', 'Confirmed');
insert into Friends values (NULL, 'JesGirl', 'Dummy', 'Confirmed');
insert into Friends values (NULL, 'Smileyman5', 'JesGirl', 'Pending');
insert into Friends values (NULL, 'JonMan', 'DaisyDuke', 'Pending');

select * from Friends;
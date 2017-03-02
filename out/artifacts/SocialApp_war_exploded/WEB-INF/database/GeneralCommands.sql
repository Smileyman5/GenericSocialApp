select * from friends;
select * from friends where username='Smileyman5';
insert into Friends values ('Smileyman5', 'DaisyDuke', 'Pending');
insert into Friends values ('Daisy', 'Smileyman5', 'Pending');
update Friends set status='Confirmed' where username='DaisyDuke' and friend='JonMan';
update Friends set status='Confirmed' where username='JonMan' and friend='DaisyDuke';
DELETE from Friends where username='DaisyDuke' and friend='JonMan';
DELETE from Friends where username='JonMan' and friend='DaisyDuke';

select * from friends;
select * from friends where username='Smileyman5';

insert into Friends values ('Smileyman5', 'DaisyDuke', 'Pending');

update Friends set status='Confirmed' where username='DaisyDuke' and friend='JonMan';
insert into Friends values ('JonMan', 'DaisyDuke', 'Confirmed');

DELETE from Friends where username='DaisyDuke' and friend='JonMan';
DELETE from Friends where username='JonMan' and friend='DaisyDuke';

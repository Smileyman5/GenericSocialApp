select * from friends;
select * from friends where username='Smileyman5';

insert into Friends values (NULL, 1, 5, 'Pending'); /* Smileyman5 - DaisyDuke */

update Friends set status='Confirmed' where username='DaisyDuke' and friend='JonMan';
insert into Friends values (NULL, 2, 5, 'Confirmed');

DELETE from Friends where username='DaisyDuke' and friend='JonMan';
DELETE from Friends where username='JonMan' and friend='DaisyDuke';

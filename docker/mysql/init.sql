use mysql;

create database fires default character set utf8;
create user fires_user identified by 'fires1234';
create user fires_user@localhost identified by 'fires1234';

grant all privileges on fires.* to fires_user@localhost;
grant all privileges on fires.* to fires_user@'%';

flush privileges;

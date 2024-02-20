# stock_manage_backend
Stock manage backend


local 개발 환경 설정
1. docker 설치
   https://www.docker.com/products/docker-desktop/
1. mysql
   i. mysql8.0 downlad
     https://dev.mysql.com/downloads/mysql/
   2, Terminal에서
     ./mysql/bin/mysql
     아래 명령어 실행
        use mysql;

        create database fires default character set utf8;
        create user fires_user identified by 'fires1234';
        create user fires_user@localhost identified by 'fires1234';

        grant all privileges on fires.* to fires_user@localhost;
        grant all privileges on fires.* to fires_user@'%';

        flush privileges;

   ii. Intellij에서 /docker/docker-compse.yml 실행



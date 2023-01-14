CREATE USER 'admin'@'localhost' IDENTIFIED BY 'adminadmin';

CREATE USER 'admin'@'%' IDENTIFIED BY 'adminadmin';

GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%' WITH GRANT OPTION;

 flush privileges;

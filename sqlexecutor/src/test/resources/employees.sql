CREATE TABLE employee(
    id int NOT NULL PRIMARY KEY auto_increment,
    firstname varchar(255),
    lastname varchar(255),
    salary double,
    hireddate date,
);
 
CREATE TABLE email(
    id int NOT NULL PRIMARY KEY auto_increment,
    employeeid int,
    address varchar(255)
);
 
INSERT INTO employee (firstname,lastname,salary,hireddate)
  VALUES ('John', 'Doe', 10000.10, to_date('01-01-2001','dd-mm-yyyy'));

INSERT INTO email (employeeid,address)
  VALUES (1, 'john@baeldung.com');

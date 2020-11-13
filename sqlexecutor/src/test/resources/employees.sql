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
 
INSERT INTO employee (id, firstname,lastname,salary,hireddate)
  VALUES (1, 'John', 'Doe', 10000.10, to_date('01-01-2001','dd-mm-yyyy'));
  
INSERT INTO employee (id, firstname,lastname,salary,hireddate)
  VALUES (2, 'John', 'Doe', 10000.10, to_date('01-01-2001','dd-mm-yyyy'));

INSERT INTO email (employeeid,address)
  VALUES (1, 'john@baeldung.com');
  
INSERT INTO email (employeeid,address)
  VALUES (2, 'john@baeldung.com');
  
INSERT INTO email (employeeid,address)
  VALUES (3, 'john@baeldung.com');

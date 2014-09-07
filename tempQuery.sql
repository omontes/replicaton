CREATE DATABASE replication;
USE replication;
CREATE TABLE DEPARTMENT ( Dname varchar(15) NOT NULL, Dnumber int NOT NULL, Mgr_ssn char(9) NOT NULL, Mgr_start_date date );
CREATE TABLE DEPT_LOCATIONS ( Dnumber int NOT NULL, Dlocation varchar(15) NOT NULL );
CREATE TABLE EMPLOYEE ( Fname varchar(15) NOT NULL, Minit char(1), Lname varchar(15) NOT NULL, Ssn char(9) NOT NULL, Bdate date, Address varchar(30), Sex char(1), Salary decimal, Super_ssn char(9), Dno int NOT NULL );
CREATE TABLE PROJECT ( Pname varchar(15) NOT NULL, Pnumber int NOT NULL, Plocation varchar(15), Dnum int NOT NULL );

# MySQL-Browser

This project will help you in how you can use JDBC to build your own browser

-- Create schema and table to run the application

create database dbbrowser;
create table connections(id int(10) primary key auto_increment , con_name varchar(10), hostname varchar(10), port_no varchar(10), username varchar(10), password varchar(10));

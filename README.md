# MySQL-Browser

This project will help you in how you can use JDBC to build your own browser

-- Create schema and table to run the application

create database dbbrowser;
create table connections(id int(10) primary key auto_increment , con_name varchar(10), hostname varchar(10), port_no varchar(10), username varchar(10), password varchar(10));

Add the attached jar in build path, download and unzip tomcat server file
Tomcat server and required jars
apache-tomcat-8.0.51-windows-x64.zip
mysql-connector-java-8.0.23.jar


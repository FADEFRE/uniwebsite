# swtp-2023-12

## Setup Database Connection

install Java SE Development Kit 17.0.8:
https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html


install postgre database and remeber password!
https://www.postgresql.org/download/


install pg4Admin
https://www.pgadmin.org/download/


open pg4Admin click on servers, create new database


edit application.properties in /src/main/recources/:
spring.datasource.url=jdbc:postgresql://localhost:5432/<your_database_name>
spring.datasource.username=postgres
spring.datasource.password=<your_password>


// this line says the database schema will always be created after starting and deleted after stopping // other config possible
spring.jpa.hibernate.ddl-auto=create-drop


    !! Always stop programm with CTRL+C inside your IDE terminal/console, otherwise DB-tables won't be dropped !!

## Login Daten 

Studienbüro:

    username: studyoffice
    password: abc123

Prüfungsausschuss:

    username: pav
    password: pav123

Admin:

    username: admin
    password: admin

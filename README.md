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

## API ENDPOINTS

http://localhost:8090/modules-leipzig
 -> GET: all internal modules leipzig

http://localhost:8090/applications

 -> POST: post new application
            String moduleName;
            String course;
            String university;
            Integer points;
            String pointSystem;
            MultipartFile description;
            String comment;     

http://localhost:8090/pdf-documents/{id}

 -> GET: get single pdf file

## Add your files

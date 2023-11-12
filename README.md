
## Name
module crediting (uni leipzig)

## Information for Backend

1. install Java SE Development Kit 17.0.8:
https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

2. install postgre database and remeber password!
https://www.postgresql.org/download/

3. install pg4Admin
https://www.pgadmin.org/download/

4. open pg4Admin click on servers, create new database

5. edit application.properties in /src/main/recources/: 
spring.datasource.url=jdbc:postgresql://localhost:5432/<your_database_name>
spring.datasource.username=postgres
spring.datasource.password=<your_password>

// this line says the database schema will always be created after starting and deleted after stopping // other config possible
spring.jpa.hibernate.ddl-auto=create-drop




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

## API ENDPOINTS

http://localhost:8090/courses-leipzig
 -> GET: all internal courses leipzig with their modules

http://localhost:8090/modules-leipzig
 -> GET: all internal modules leipzig

http://localhost:8090/applications

 -> POST: post new application   Content-Type: multipart/form-data
    
    - courseLeipzig: B.Sc. Informatik
    - moduleBlockCreateDTOList[0].moduleName: ...
    - moduleBlockCreateDTOList[0].university: ...
    - moduleBlockCreateDTOList[0].points: ...
    - moduleBlockCreateDTOList[0].pointSystem: ...
    - moduleBlockCreateDTOList[0].description: ...
    - moduleBlockCreateDTOList[0].moduleNameLeipzig[0]: Berechenbarkeit (!!! important name must be in database !!!)
    - moduleBlockCreateDTOList[0].moduleNameLeipzig[1]:
    - moduleBlockCreateDTOList[0].commentApplicant: ...
    - moduleBlockCreateDTOList[1].moduleName: ...
    ...

http://localhost:8090/applications
 -> GET: getAllApplications requestparam: limit (default = 10), optional status,
    enum status:
        - OFFEN
        - IN_BEARBEITUNG
        - ABGESCHLOSSEN

http://localhost:8090/applications/{id}
 -> GET: getApplicationById (full applicaiton for sutdienbuero and pav)
    (pdf id is included: use for retrieving pdf data over seperate api endpoint)

http://localhost:8090/applications/{id}/exists
 -> GET: boolean if application with id exists

http://localhost:8090/applications/student/{id}
 -> GET: getApplicationById (limited info)
    (pdf id is included: use for retrieving pdf data over seperate api endpoint)

http://localhost:8090/pdf-documents/{id}

 -> GET: get single pdf file

## Add your files

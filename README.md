# Summary of this Readme

 1. Setup and settings
     - Setup backend and database
     - Other application.properties settings
        - spring.jpa.hibernate.ddl-auto
        - server.port
     - Dataloader settings in application-(dev/prod).properties
         - app.config.data.adminUsername/Password
         - app.config.data.loadTestData
     - Test Data
        - users
        - random applications
        - random external modules
        - internal courses and modules

 2. Javadoc generation

 3. Api-Endpoints -> TODO
    - List of Views.class 
    - Application: "ApplicationController" -> /api/applications
        - GET / POST / PUT
    - Authentication: "AuthController" -> /api/auth
        - POST
    - Studiengaenge in Leipzig: "CourseLeipzigController" -> /api/courses-leipzig
        - GET / POST / PUT / DELETE
    - Handling of JSON files: "JsonFileController" -> /file/json
        - GET / POST
    - Module in Leipzig: "ModuleLeipzigController" -> /api/modules-leipzig
        - GET / POST / PUT / DELETE
    - ModulesConnection: "ModulesConnectionController" -> /api/modules-connection
        - GET
    - Handling of PDF files: "PdfDocumentContoller" -> /file/pdf-documents
        - GET
    - User: "UserController" -> /api/user
        - GET / POST / PUT


# Setup and settings
## Setup backend and database

JDK version 17.0.8: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

PostgreSQL: https://www.postgresql.org/download/

pgAdmin 4: https://www.pgadmin.org/download/


1. Create new Database in pgAdmin
2. Edit application-dev.properties in backend\modulecrediting\src\main\recources\ :

    - spring.datasource.url=jdbc:postgresql://localhost:5432/<your_database_name>
    - spring.datasource.username=<your_username> (default: postgres)
    - spring.datasource.password=<your_password>

3. Edit application.properties in backend\modulecrediting\src\main\recources\ :

    - spring.profiles.active= dev / prod


## Other application.properties settings:
### spring.jpa.hibernate.ddl-auto= create-drop / update
This will tell jpa what to do on startup and shutdown.

 - create-drop: The (existing) database will be dropped on startup and shutdown and the tables will be newly generated
 - update: The existing database will not be dropped, but rather be updated 

create-drop is recommended for development purposes

### server.port = (default: 8090)
This is the port on which the backend will run. If this is changed, it needs to be changed in the url-config.js aswell      
frontend\modulecrediting\src\config\url-config.js


## Dataloader settings in application-(dev/prod).properties:
### app.config.data.adminUsername/Password:
Everytime the backend service is started, the dataloader will check if at least one admin-user exists. 
If it does not, the dataloader will create an admin-user with these parameters.

### app.config.data.loadTestData= true / false
On startup the dataloader will check if it should run the "TestDataLoader.java" class.
The TestDataLoader class reads in test_data.json from the backend ressources folder.

## Test Data
### The testData currently includes:
#### 1. Parameters to create these "users":

 - Admin:
    - username: admin
    - password: admin
    - role: admin

 - Studienbuero:
    - username: studyoffice
    - password: abc123
    - role: study

 - Pruefungsausschuss:
    - username: pav
    - password: pav123
    - role: chair

#### 2. Parameters to create "randApplications":

 - all these parameters should be a number of how many of this type of "application" should be created.
 - current fields are: new, study-office, formfehler, pav, closed

#### 3. Parameters to create "randExternalModules":

 - all these parameters should be JSON array literals, from which the testDataLoader will randomly select a value to use.
 - current fields are: name, externalCourse, uni, points, pointSystem, comment

#### 4. Internal Courses and Modules -> "courses":

 - these are all the courses and modules that will be written into the database when "app.config.data.loadTestData" is set to true.
 - duplicates will not be written multiple times
 - BUT if modules appear in multiple courses, they will be added to all of these courses

.


# JAVADOC GENERATION

 - to (re)generate the Javadoc for this project run these commands in \backend\modulecrediting :
    
    - mvn clean (optional)
    - mvn install

    - alternatively "mvn javadoc:javadoc" can be run aswell

 - NOTE: in production the first two commands are executed everytime the pipeline makes a new build of the backend

 - afterwards the documentation can be found under \backend\modulecrediting\target\apidocs\index.html      
 - it is recommended to open the file with a browser and not in an IDE

.


# API ENDPOINTS
## Overview of Views: "\model\Views.java"

In some cases Views are used, instead of DTOs, to generate ResponseData

 - ApplicationLoginOverview
    - TODO: explanation
 - ApplicationLogin
    - TODO: explanation
 - ApplicationStudent
    - TODO: explanation
 - CoursesWithModules
    - TODO: explanation
 - ModulesWithoutCourse
    - TODO: explanation
 - RelatedModulesConnection
    - TODO: explanation

## Application: "ApplicationController"
### GET - Requests:

#### > /api/applications
 - returns a "List" of all "Applications"
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
 - views: 
    - ApplicationLoginOverview
- http://localhost:8090/api/applications

#### > /api/applications/{id}
 - pathvariable
    - {id} ->  id of the requested application
 - returns corresponding "Application"
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
 - views: 
    - ApplicationLoginOverview
- http://localhost:8090/api/applications/{id}



http://localhost:8090/api/applications/original -> returns a "List" of all "OriginalApplications"   

    [
        {
            "id": id,
            "fullStatus": NEU / FORMFEHLER / STUDIENBÜRO / PRÜFUNGSAUSSCHUSS / ABGESCHLOSSEN,

            "creationDate": creationDate,
            "lastEditedDate": null / lastEditedDate,
            "decisionDate": null / decisionDate,

            "courseLeipzig": { "name": name },
            "modulesConnections": [
                {
                    "externalModules": [
                        {
                            "name": name,
                            "university": university
                        },
                        ...
                    ]
                },
                ...
            ]
        },
        ...
    ]
.

http://localhost:8090/api/applications/{id} -> needs {id}, returns corresponding "Application"    
"Views.ApplicationLogin.class"      
http://localhost:8090/api/applications/{id}/original -> needs {id}, returns corresponding "OriginalApplications"  

    "id": id,
    "fullStatus": NEU / FORMFEHLER / STUDIENBÜRO / PRÜFUNGSAUSSCHUSS / ABGESCHLOSSEN,

    "creationDate": creationDate,
    "lastEditedDate": null / lastEditedDate,
    "decisionDate": null / decisionDate,

    "courseLeipzig": { "name": name },
    "modulesConnections": [
        {
            "id": id,
            "decisionFinal": accepted / asExamCertificate / denied / unedited,
            "commentDecision": "" / comment,
            "decisionSuggestion": accepted / asExamCertificate / denied / unedited,
            "commentStudyOffice": "" / comment,
            "formalRejection": true / false,
            "formalRejectionComment": "" / comment,
            "commentApplicant": "" / comment,
            "externalModules": [
                {
                    "id": id,
                    "name": name,
                    "points": points,
                    "pointSystem": pointSystem,
                    "university": university,
                    "pdfDocument": {
                        "id": id,
                        "name": name
                    }
                },
                ...
            ]
        },
        ...
    ]
.

http://localhost:8090/api/applications/{id}/exists -> needs {id}, returns "Boolean"

    true / false
.

http://localhost:8090/api/applications/{id}/update-status-allowed -> needs {id}, returns "EnumStatusChange"

    NOT_ALLOWED / PASSON / REJECT
.

http://localhost:8090/api/applications/student/{id} -> needs {id}, returns corresponding "Application"      
"Views.ApplicationStudent.class"

    NEEDS FIXING IN BACKEND FIRST
.


### POST - Requests:

http://localhost:8090/api/applications -> needs "ApplicationCreateDTO", returns "id" of the created application

    id
.


### PUT - Requests:

http://localhost:8090/api/applications/{id}/update-status -> needs {id}, returns "EnumApplicationStatus"

    NEU / FORMFEHLER / STUDIENBÜRO / PRÜFUNGSAUSSCHUSS / ABGESCHLOSSEN
.

http://localhost:8090/api/applications/standard/{id} -> needs {id} "ApplicationUpdateDTO" "BindingResult?", returns "id" of updated application

    id
.

Requires Role: "ROLE_STUDY":     
http://localhost:8090/api/applications/study-office/{id} -> needs {id} "ApplicationUpdateDTO" "BindingResult?", returns "id" of updated application

    id
.

Requires Role: "ROLE_CHAIR":  
http://localhost:8090/api/applications/chairman/{id} -> needs {id} "ApplicationUpdateDTO" "BindingResult?", returns "id" of updated application

    id
.


.

## Authentication: "AuthController"
### POST - Requests:

http://localhost:8090/api/auth/login -> needs "LoginRequest", optional "authCookie" "refreshCookie", returns "LoginResponse"

    LoginRequest: {
        "username": username,
        "password": password
    }

    LoginResponse: {
        "SuccessFailure": SUCCESS / FAILURE,
        "message": message
    }
.

http://localhost:8090/api/auth/login -> needs "refreshCookie", optional "authCookie", returns "LoginResponse"

    LoginResponse: {
        "SuccessFailure": SUCCESS / FAILURE,
        "message": message
    }
.

http://localhost:8090/api/auth/login -> returns "LogoutResponse"

    LogoutResponse: {
        "SuccessFailure": SUCCESS / FAILURE,
        "message": message
    }
.

Requires Role: "ROLE_ADMIN":        
http://localhost:8090/api/auth/register -> needs "RegisterRequest", returns "message"

    RegisterRequest: {
        "username": username,
        "password": password,
        "passwordConfirm": passwordConfirm,
        "role": role
    }


    HttpStatus.OK           -> User registered successfully!
    HttpStatus.BAD_REQUEST  -> Username already exists!
.


.

## Studiengänge in Leipzig: "CourseLeipzigController"
### GET - Requests:

http://localhost:8090/api/courses-leipzig -> returns a "List" of all "CourseLeipzig"
"Views.coursesWithModules.class"

    [
        {
            "name": name,
            "isActive": true / false,
            "modulesLeipzigCourse": [
                {
                    "name": name,
                    "code": code,
                    "isActive": true / false
                },
                ...
            ]
        },
        ...
    ]
.

http://localhost:8090/api/courses-leipzig/{name} -> needs {name}, returns corresponding "CourseLeipzig"


    "name": name,
    "isActive": true / false,
    "modulesLeipzigCourse": [
        {
            "name": name,
            "code": code,
            "isActive": true / false
        },
        ...
    ]
.

http://localhost:8090/api/courses-leipzig/{name}/state -> needs {name}, returns "isActive"      
Requires Role: "ROLE_STUDY" or "ROLE_CHAIR"

    true / false
.


### POST - Requests:

http://localhost:8090/api/courses-leipzig/create -> needs "CourseLeipzigCreateDTO", returns "name" of created CourseLeipzig         
Requires Role: "ROLE_STUDY" or "ROLE_CHAIR"

    id
.


### PUT - Requests:

http://localhost:8090/api/courses-leipzig/{name}/edit -> needs {name} "EditCourseDTO", returns "Boolean"            
Requires Role: "ROLE_STUDY" or "ROLE_CHAIR"     

This Requests edits CourseLeipzig <-> ModuleLeipzig relations

    true / false
.


### DELETE - Requests:

http://localhost:8090/api/courses-leipzig/{name}/delete -> needs {name}, returns            
Requires Role: "ROLE_STUDY" or "ROLE_CHAIR"

    DELETED / DEACTIVATED
.


.

## Module in Leipzig: "ModuleLeipzigController"
### GET - Requests:

http://localhost:8090/api/modules-leipzig -> returns a "List" of all "ModuleLeipzig"    
"Views.modulesWithoutCourse.class"

    [
        {
            "name": name,
            "code": code,
            "isActive": true / false
        },
        ...
    ]
.

http://localhost:8090/api/modules-leipzig/{name} -> needs {name}, returns corresponding "ModuleLeipzig"


    "name": name,
    "code": code,
    "isActive": true / false
.

http://localhost:8090/api/modules-leipzig/{name}/state -> needs {name}, returns "isActive"      
Requires Role: "ROLE_STUDY" or "ROLE_CHAIR"

    true / false
.


### POST - Requests:

http://localhost:8090/api/modules-leipzig/create -> needs "ModuleLeipzigCreateDTO", returns "name" of created CourseLeipzig         
Requires Role: "ROLE_STUDY" or "ROLE_CHAIR"

    id
.


### PUT - Requests:

http://localhost:8090/api/modules-leipzig/{name}/edit -> needs {name} "ModuleLeipzigUpdateDTO", returns "Boolean"   
Requires Role: "ROLE_STUDY" or "ROLE_CHAIR"

    true / false
.


### DELETE - Requests:

http://localhost:8090/api/modules-leipzig/{name}/delete -> needs {name}, returns        
Requires Role: "ROLE_STUDY" or "ROLE_CHAIR"

    DELETED / DEACTIVATED
.

.

## ModulesConnection: "ModulesConnectionController"
### GET - Requests:

http://localhost:8090/api/modules-connection/{id}/related -> needs {id}, returns "List" of related "ModulesConnection"

    [
        {
            "id": id,
            "decisionFinal": accepted / asExamCertificate / denied / unedited,
            "externalModules": [
                {
                    "name": name,
                    "university": university
                },
                ...
            ],
            "modulesLeipzig": [
                {
                    "name": name
                },
                ...
            ],
            "application": {
                "id": id,
                "decisionDate": "decisionDate",
                "courseLeipzig": {
                    "name": name
                }
            }
        },
        ...
    ]
.

.

## PDF Dokumente: "PdfDocumentContoller"
### GET - Requests:

http://localhost:8090/file/pdf-documents/{id} -> needs pdf {id}, returns "PDF"

    name.pdf
.

http://localhost:8090/file/pdf-documents/application/{id} -> needs application {id}, returns new generated "PDF" of corresponding application

    antrag.pdf
.

.

## User: "UserController"
### GET - Requests:

http://localhost:8090/api/user/me -> returns "UserSummary" 

    "userId": userId,
    "username": username
.

http://localhost:8090/api/user/{id}/role -> needs user {id}, returns "Role" of user

    HttpStatus.OK           -> Anonymous user / ROLE_STUDY / ROLE_CHAIR / ROLE_ADMIN , 
    HttpStatus.NOT_FOUND  -> User doesnt exists! 
.

.

.

.

.




## OLD README:

.
http://localhost:8090/api/courses-leipzig
 -> GET: all internal courses leipzig with their modules

http://localhost:8090/api/modules-leipzig
 -> GET: all internal modules leipzig

http://localhost:8090/api/applications

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

http://localhost:8090/api/applications
 -> GET: getAllApplications requestparam: limit (default = 10), optional status,
    enum status:
        - OFFEN
        - IN_BEARBEITUNG
        - ABGESCHLOSSEN

http://localhost:8090/api/applications/{id}
 -> GET: getApplicationById (full applicaiton for sutdienbuero and pav)
    (pdf id is included: use for retrieving pdf data over seperate api endpoint)

http://localhost:8090/api/applications/{id}
 -> PUT: update applicaiton
    - moduleBlockUpdateDTOList[0].moduleName: ...
    - moduleBlockUpdateDTOList[0].university: ...
    - moduleBlockUpdateDTOList[0].points: ...
    - moduleBlockUpdateDTOList[0].pointSystem: ...
    - moduleBlockUpdateDTOList[0].moduleNameLeipzig[0]: Berechenbarkeit (!!! important name must be in database !!!)
    - moduleBlockUpdateDTOList[0].moduleNameLeipzig[1]: ...
    - moduleBlockUpdateDTOList[0].decision: {ANGENOMMEN,VERAENDERT_ANGENOMMEN,ABGELEHNT,UNBEARBEITET}
    - moduleBlockUpdateDTOList[0].comment: ...
    - moduleBlockUpdateDTOList[1].moduleName: ...

http://localhost:8090/api/applications/{id}/exists
 -> GET: boolean if application with id exists

http://localhost:8090/api/applications/student/{id}
 -> GET: getApplicationById (limited info)
    (pdf id is included: use for retrieving pdf data over seperate api endpoint)

http://localhost:8090/file/pdf-documents/{id}

 -> GET: get single pdf file

http://localhost:8090/file/pdf-documents/application/${id}
 -> GET: generated PDF Document with application Data


http://localhost:8090/api/modules-connection/{id}/related

-> GET: all related modules-connections of a module connection 



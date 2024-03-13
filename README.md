# Summary of this Readme

1. **[Setup and settings](#1-setup-and-settings)**
    1. [Setup backend and database (development)](#11-setup-backend-and-database-development)
    2. [Setup frontend (development)]()
    3. [Dataloader settings in *.properties files](#13-dataloader-settings-in-application-devprodproperties)
         - [app.config.data.adminUsername/Password](#appconfigdataadminusernamepassword)
         - [app.config.data.loadTestData](#appconfigdataloadtestdata-true--false)
    4. [Other application.properties settings](#14-other-applicationproperties-settings)
        - [spring.jpa.hibernate.ddl-auto](#springjpahibernateddl-auto-create-drop--update)
        - [server.port](#serverport--default-8090)
    5. [Test Data](#15-test-data)
        - [users](#1-parameters-to-create-these-users)
        - [random applications](#2-parameters-to-create-randapplications)
        - [random external modules](#3-parameters-to-create-randexternalmodules)
        - [internal courses and modules](#4-internal-courses-and-modules---courses)
2. **[Javadoc](#2-javadoc-generation)**
3. **[Api-Endpoints](#3-api-endpoints)**
    1. [List of Views.class](#31-overview-of-views-modelviewsjava)
    2. [Application: "ApplicationController" -> /api/applications](#32-application-applicationcontroller)
        - [GET-Requests](#get---requests)
        - [POST-Requests](#post---requests)
        - [PUT-Requests](#put---requests)
    3. [Authentication: "AuthController" -> /api/auth](#33-authentication-authcontroller)
        - [POST-Requests](#post---requests-1)
    4. [Studiengaenge in Leipzig: "CourseLeipzigController" -> /api/courses-leipzig](#34-studiengaenge-in-leipzig-courseleipzigcontroller)
        - [GET-Requests](#get---requests-1)
        - [POST-Requests](#post---requests-2)
        - [PUT-Requests](#put---requests-1)
        - [DELETE-Requests](#delete---requests)
    5. [Handling of JSON files: "JsonFileController" -> /file/json](#35-handling-of-json-files-jsonfilecontroller)
        - [GET-Requests](#get---requests-2)
        - [POST-Requests](#post---requests-3)
    6. [Module in Leipzig: "ModuleLeipzigController" -> /api/modules-leipzig](#36-module-in-leipzig-moduleleipzigcontroller)
        - [GET-Requests](#get---requests-3)
        - [POST-Requests](#post---requests-4)
        - [PUT-Requests](#put---requests-2)
        - [DELETE-Requests](#delete---requests-1)
    7. [ModulesConnection: "ModulesConnectionController" -> /api/modules-connection](#37-modulesconnection-modulesconnectioncontroller)
        - [GET-Requests](#get---requests-4)
    8. [Handling of PDF files: "PdfDocumentContoller" -> /file/pdf-documents](#38-handling-of-pdf-files-pdfdocumentcontoller)
        - [GET-Requests](#get---requests-5)
    9. [User: "UserController" -> /api/user](#39-user-usercontroller)
        - [GET-Requests](#get---requests-6)
        - [POST-Requests](#post---requests-5)
        - [PUT-Requests](#put---requests-3)

.  
 <a href="#top">Back to top</a>

# 1. Setup and settings
## 1.1. Setup backend and database (development)

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

 <a href="#top">Back to top</a>

## 1.2. Setup frontend (development)

Node: https://nodejs.org/en/download/

1. After Node is installed, run these commands in frontend\modulecrediting\src\
   - npm install

   - npm run dev

2. After the first install, only "npm run dev" needs to be executed to start the frontend

3. (optional) for dev purposes you can add a "jsconfig.json" file to frontend\modulecrediting\ with following content, to remove some IDE warnings:

         {
            "compilerOptions": {
               "baseUrl": ".",
               "paths": {
                  "@/*": ["./src/*"],
               }
            }
         }


 <a href="#top">Back to top</a>

## 1.3. Dataloader settings in application-(dev/prod).properties:
### > app.config.data.adminUsername/Password:
Everytime the backend service is started, the dataloader will check if at least one admin-user exists. 
If it does not, the dataloader will create an admin-user with these parameters.

### > app.config.data.loadTestData= true / false
On startup the dataloader will check if it should run the "TestDataLoader.java" class.
The TestDataLoader class reads in test_data.json from the backend ressources folder.

 <a href="#top">Back to top</a>

## 1.4. Other application.properties settings:
### > spring.jpa.hibernate.ddl-auto= create-drop / update
This will tell jpa what to do on startup and shutdown.

 - create-drop: The (existing) database will be dropped on startup and shutdown and the tables will be newly generated
 - update: The existing database will not be dropped, but rather be updated 

create-drop is recommended for development purposes

### > server.port = (default: 8090)
This is the port on which the backend will run. If this is changed, it needs to be changed in the url-config.js aswell      
frontend\modulecrediting\src\config\url-config.js

### > app.auth.*TokenExpirationMsec
These set the duration (in milliseconds) of how long the access/refreshToken are valid 

### > app.auth.expireAtMidnight = true / false
When set to "true", the refreshTokenExpirationDate will be changed to midnight after its original expiration set by app.auth.refreshTokenExpirationMsec

 <a href="#top">Back to top</a>

## 1.5. Test Data
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

  
 <a href="#top">Back to top</a>

# 2. JAVADOC GENERATION

 - to (re)generate the Javadoc for this project run these commands in \backend\modulecrediting :
    
    - mvn clean (optional)
    - mvn install

    - alternatively "mvn javadoc:javadoc" can be run aswell

 - NOTE: in production the first two commands are executed everytime the pipeline makes a new build of the backend

 - afterwards the documentation can be found under \backend\modulecrediting\target\apidocs\index.html      
 - it is recommended to open the file with a browser and not in an IDE


 <a href="#top">Back to top</a>

# 3. API ENDPOINTS
## 3.1. Overview of Views: "\model\Views.java"

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

 <a href="#top">Back to top</a>

## 3.2. Application: "ApplicationController"
### _**GET - Requests:**_

#### > **/api/applications**
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
 - returns a "List" of all "Applications" with fields definied by "Views.ApplicationLoginOverview"
 - http://localhost:8090/api/applications

#### > **/api/applications/{id}**
 - pathvariables:
    - {id} ->  id of the requested application
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
 - returns corresponding "Application" with fields definied by "Views.ApplicationLoginOverview"
 - http://localhost:8090/api/applications/{id}

#### > **/api/applications/student/{id}**
 - pathvariables:
    - {id} ->  id of the requested application 
 - returns corresponding "Application" with fields definied by "Views.ApplicationStudent"
 - http://localhost:8090/api/applications/student/{id}

#### > **/api/applications/{id}/exists**
 - pathvariables:
    - {id} ->  id of the requested application 
 - returns Boolean with true if the specified "Application" exists
 - http://localhost:8090/api/applications/{id}/exists

#### > **/api/applications/{id}/update-status-allowed**
 - pathvariables:
    - {id} ->  id of the requested application 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
 - returns "EnumStatusChangeAllowed" 
 - http://localhost:8090/api/applications/{id}/update-status-allowed



 <a href="#top">Back to top</a>

### _**Basic Formdata for "Application"-Requests**_
This basic formdata is used in most "Application"-Requests

    courseLeipzig: ***
    modulesConnectionDTO[0].commentApplicant: ***
    modulesConnectionDTO[0].externalModuleDTO[0].name: ***
    modulesConnectionDTO[0].externalModuleDTO[0].externalCourse: ***
    modulesConnectionDTO[0].externalModuleDTO[0].university: ***
    modulesConnectionDTO[0].externalModuleDTO[0].points: ***
    modulesConnectionDTO[0].externalModuleDTO[0].pointSystem: ***
    modulesConnectionDTO[0].externalModuleDTO[0].description: *** -> multipart/file
    modulesConnectionDTO[0].moduleLeipzigDTO[0].name

 
 <a href="#top">Back to top</a>

### _**POST - Requests:**_

#### > **/api/applications**
 - modelAttribute: "ApplicationDTO" 
    - [Basic Formdata](#basic-formdata-for-application-requests)
 - returns "id" of created "Application" 
 - http://localhost:8090/api/applications


 
 <a href="#top">Back to top</a>

### _**PUT - Requests:**_

#### > **/api/applications/student/{id}**
 - pathvariables:
    - {id} ->  id of the requested application 
 - modelAttribute: "ApplicationDTO" 
    - [Basic Formdata](#basic-formdata-for-application-requests)
 - returns "id" of updated "Application" 
 - http://localhost:8090/api/applications/student/{id}


#### > **/api/applications/{id}/update-status**
 - pathvariables:
    - {id} ->  id of the requested application 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
 - returns "EnumApplicationStatus"
 - http://localhost:8090/api/applications/{id}/update-status


#### > **/api/applications/study-office/{id}**
 - pathvariables:
    - {id} ->  id of the requested application 
 - required role:
    - ROLE_STUDY
 - modelAttribute: "ApplicationDTO"
    - [Basic Formdata](#basic-formdata-for-application-requests) and
    - either:

            modulesConnectionDTO[0].formalRejection: true
            modulesConnectionDTO[0].formalRejectionComment: ***
    - or:

            modulesConnectionDTO[0].formalRejection: false
            modulesConnectionDTO[0].formalRejectionComment: ""
            modulesConnectionDTO[0].decisionSuggestion: *** (optional)
            modulesConnectionDTO[0].commentStudyOffice: *** (optional)
 - returns "id" of updated "Application" 
 - http://localhost:8090/api/applications/study-office/{id}


#### > **/api/applications/chairman/{id}**
 - pathvariables:
    - {id} ->  id of the requested application 
 - required role:
    - ROLE_CHAIR
 - modelAttribute: "ApplicationDTO"
    - [Basic Formdata](#basic-formdata-for-application-requests) and

            modulesConnectionDTO[0].decisionSuggestion: *** (optional)
            modulesConnectionDTO[0].commentStudyOffice: *** (optional)
 - returns "id" of updated "Application" 
 - http://localhost:8090/api/applications/chairman/{id}

  
 <a href="#top">Back to top</a>


## 3.3. Authentication: "AuthController"
### _**POST - Requests:**_

#### > **/api/auth/login**
 - cookies:
    - accessToken (optional)
    - refreshToken (optional)
 - requestBody: "LoginRequest"
    
        username: ***
        password: ***
 - returns "LoginResponse"
 - http://localhost:8090/api/auth/login

#### > **/api/auth/refresh**
 - cookies:
    - accessToken (optional)
    - refreshToken (kind of needed -> without forced logout)
 - returns "LoginResponse"
 - http://localhost:8090/api/auth/refresh

#### > **/api/auth/logout**
 - returns "LogoutResponse"
 - http://localhost:8090/api/auth/logout

 <a href="#top">Back to top</a>


## 3.4. Studiengaenge in Leipzig: "CourseLeipzigController"
### _**GET - Requests:**_

#### > **/api/courses-leipzig**
 - returns "list" of all "CourseLeipzig" 
 - http://localhost:8090/api/courses-leipzig

 <a href="#34-studiengaenge-in-leipzig-courseleipzigcontroller">Back to CourseLeipzigController</a>

### _**POST - Requests:**_

#### > **/api/courses-leipzig**
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - modelAttribute: "CourseLeipzigDTO"

            courseName: ***
 - returns "name" of created "CourseLeipzig" 
 - http://localhost:8090/api/courses-leipzig

 <a href="#34-studiengaenge-in-leipzig-courseleipzigcontroller">Back to CourseLeipzigController</a>

### _**PUT - Requests:**_

#### > **/api/courses-leipzig/{name}**
 - pathvariables:
    - {name} ->  name of the requested "CourseLeipzig" 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - modelAttribute: "CourseLeipzigDTO"

            courseName: ***
 - returns "name" of updated "CourseLeipzig"
 - http://localhost:8090/api/courses-leipzig/{name}

#### > **/api/courses-leipzig/{name}/edit**
 - pathvariables:
    - {name} ->  name of the requested "CourseLeipzig" 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - modelAttribute: "CourseLeipzigRelationEditDTO"

            moduleLeipzigDTO[0].name: ***
            moduleLeipzigDTO[0].code: ***
 - returns "name" of updated "CourseLeipzig"
 - http://localhost:8090/api/courses-leipzig/{name}/edit

 <a href="#34-studiengaenge-in-leipzig-courseleipzigcontroller">Back to CourseLeipzigController</a>

### _**DELETE - Requests:**_

#### > **/api/courses-leipzig/{name}**
 - pathvariables:
    - {name} ->  name of the requested "CourseLeipzig" 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - returns "DELETED" / "DEACTIVATED" 
 - http://localhost:8090/api/courses-leipzig/{name}

 <a href="#34-studiengaenge-in-leipzig-courseleipzigcontroller">Back to CourseLeipzigController</a>         
 <a href="#top">Back to top</a>


## 3.5. Handling of JSON files: "JsonFileController" 
### _**GET - Requests:**_

#### > **/file/json/courses**
 - required role:
    - ROLE_ADMIN
 - returns "APPLICATION_JSON" of all Courses- and ModulesLeipzig 
 - http://localhost:8090/file/json/courses

 <a href="#top">Back to top</a>

### _**POST - Requests:**_

#### > **/file/json/courses/upload**
 - required role:
    - ROLE_ADMIN
 - requestParam:
    - "jsonFile": MultipartFile
 - returns success message with file name
 - http://localhost:8090/file/json/courses/upload

 <a href="#top">Back to top</a>


## 3.6. Module in Leipzig: "ModuleLeipzigController"
### _**GET - Requests:**_

#### > **/api/modules-leipzig**
 - returns "List" of all "ModulesLeipzig"
 - http://localhost:8090/api/modules-leipzig

 <a href="#top">Back to top</a>

### _**POST - Requests:**_

#### > **/api/modules-leipzig**
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - modelAttribute: "ModuleLeipzigDTO"

            name: ***
            code: ***
 - returns "name" of created "ModuleLeipzig"
 - http://localhost:8090/api/modules-leipzig

 <a href="#top">Back to top</a>

### _**PUT - Requests:**_

#### > **/api/modules-leipzig/{name}**
 - pathvariables:
    - {name} ->  name of the requested "ModuleLeipzig" 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - modelAttribute: "ModuleLeipzigDTO"

            name: ***
            code: ***
 - returns "name" of updated "ModuleLeipzig"
 - http://localhost:8090/api/modules-leipzig/{name}

 <a href="#top">Back to top</a>

### _**DELETE - Requests:**_

#### > **/api/modules-leipzig/{name}**
 - pathvariables:
    - {name} ->  name of the requested "ModuleLeipzig" 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - returns "DELETED" / "DEACTIVATED" 
 - http://localhost:8090/api/modules-leipzig/{name}

 <a href="#top">Back to top</a>


## 3.7. ModulesConnection: "ModulesConnectionController"
### _**GET - Requests:**_

#### > **/api/modules-connection/{id}/related**
 - pathvariables:
    - {id} ->  id of the requested "ModulesConnection" 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
 - returns filtered "List" of related "ModulesConnection"
 - http://localhost:8090/api/modules-connection/{id}/related

 <a href="#top">Back to top</a>


## 3.8. Handling of PDF files: "PdfDocumentContoller"
### _**GET - Requests:**_

#### > **/file/pdf-documents/{id}**
 - pathvariables:
    - {id} ->  id of the requested "pdfDocument" 
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
 - returns "APPLICATION_PDF"
 - http://localhost:8090/file/pdf-documents/{id}

#### > **/file/pdf-documents/application/{id}**
 - pathvariables:
    - {id} ->  id of the "Applicaton" of the requested "pdfDocument" 
 - returns generated "APPLICATION_PDF" of requested "Applicaton"
 - http://localhost:8090/file/pdf-documents/application/{id}

 <a href="#top">Back to top</a>


## 3.9. User: "UserController"
### _**GET - Requests:**_

#### > **/api/user/me**
 - returns "UserSummary" of current user
 - http://localhost:8090/api/user/me

#### > **/api/user/me/id**
 - returns "UserSummary" (only id) of current user
 - http://localhost:8090/api/user/me

#### > **/api/user/me/name**
 - returns "UserSummary" (only name) of current user
 - http://localhost:8090/api/user/me

#### > **/api/user/role**
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - returns "name" of role of the current user
 - http://localhost:8090/api/user/role

#### > **/api/user/all**
 - required role:
    - ROLE_ADMIN
 - returns "List" of "UserSummary" of all "User"
 - http://localhost:8090/api/user/all

 <a href="#top">Back to top</a>

### _**POST - Requests:**_

#### > **/api/user/register**
 - required role:
    - ROLE_ADMIN
 - modelAttribute: "EditUserDTO"

            username: ***
            password: ***
            passwordConfirm: ***
            role: ***
 - returns success message
 - http://localhost:8090/api/user/register

 <a href="#top">Back to top</a>

### _**PUT - Requests:**_

#### > **/api/user/change/username**
 - required role:
    - ROLE_STUDY
    - ROLE_CHAIR
    - ROLE_ADMIN
 - modelAttribute: "EditUserDTO"

            id: ***
            username: ***
 - returns success message
 - http://localhost:8090/api/user/change/username

#### > **/api/user/change/role**
 - required role:
    - ROLE_ADMIN
 - modelAttribute: "EditUserDTO"

            id: ***
            role: ***
 - returns success message
 - http://localhost:8090/api/user/change/role

 <a href="#top">Back to top</a>
 
### _**DELETE - Requests:**_

#### > **/api/user**
 - required role:
    - ROLE_ADMIN
 - modelAttribute: "EditUserDTO"

            id: ***
 - returns success message
 - http://localhost:8090/api/user/register

 <a href="#top">Back to top</a>

ef 

# Summary of this Readme

1. **[General Information](#1-general-information)**
2. **[Setup and settings](#2-setup-and-settings)**
    1. [Setup backend and database (development)](#21-setup-backend-and-database-development)
    2. [Setup frontend (development)](#22-setup-frontend-development)
    3. [Dataloader settings in *.properties files](#23-dataloader-settings-in-application-devprodproperties)
         - [app.config.data.adminUsername/Password](#appconfigdataadminusernamepassword)
         - [app.config.data.loadTestData](#appconfigdataloadtestdata-true--false)
    4. [Other application.properties settings](#24-other-applicationproperties-settings)
        - [spring.jpa.hibernate.ddl-auto](#springjpahibernateddl-auto-create-drop--update)
        - [server.port](#serverport--default-8090)
        - [token expiration msec](#appauthtokenexpirationmsec)
        - [refreshToken midnight toggle](#appauthexpireatmidnight--true--false)
    5. [Test Data](#25-test-data)
        - [users](#1-parameters-to-create-these-users)
        - [random applications](#2-parameters-to-create-randapplications)
        - [random external modules](#3-parameters-to-create-randexternalmodules)
        - [internal courses and modules](#4-internal-courses-and-modules---courses)
3. **[Javadoc](#3-javadoc-generation)**
4. **[Api-Endpoints](#4-api-endpoints)**
    1. [List of Views.class](#41-overview-of-views-modelviewsjava)
    2. [Application: "ApplicationController" -> /api/applications](#42-application-applicationcontroller)
    3. [Authentication: "AuthController" -> /api/auth](#43-authentication-authcontroller)
    4. [Studiengaenge in Leipzig: "CourseLeipzigController" -> /api/courses-leipzig](#44-studiengaenge-in-leipzig-courseleipzigcontroller)
    5. [Handling of JSON files: "JsonFileController" -> /file/json](#45-handling-of-json-files-jsonfilecontroller)
    6. [Module in Leipzig: "ModuleLeipzigController" -> /api/modules-leipzig](#46-module-in-leipzig-moduleleipzigcontroller)
    7. [ModulesConnection: "ModulesConnectionController" -> /api/modules-connection](#47-modulesconnection-modulesconnectioncontroller)
    8. [Handling of PDF files: "PdfDocumentContoller" -> /file/pdf-documents](#48-handling-of-pdf-files-pdfdocumentcontoller)
    9. [User: "UserController" -> /api/user](#39-user-usercontroller)
5. **[Folder structure explanation](#5-folder-structure-explanation)**
    1. [Backend](#51-backend)
    2. [Frontend](#52-frontend)

.  
 <a href="#top">Back to top</a>

# 1. General Information 

You can reach this groups VM under http://172.26.92.91:8080

After a new deploy on the VM it might be, that only the admin user definied <a href="#5-folder-structure-explanation"> here </a> exists. In this case you might need to create other users.
If no courses and modules exists, you should upload them as a JSON. A backup of this json can be found in this repository.    
Current backup file is: _**modulecrediting_config_2024-03-10_16-57-48.json**_


An explanation of the folder structures both in backend and frontend, 
can be found at the <a href="#5-folder-structure-explanation"> end </a>of this README

NOTE: if you use 'create-drop' this app will only create 1 admin user 
(<a href="#23-dataloader-settings-in-application-devprodproperties">more here</a>),
if the 'TestDataLoader' is not enabled

 <a href="#top">Back to top</a>

# 2. Setup and settings
## 2.1. Setup backend and database (development)

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

## 2.2. Setup frontend (development)

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

## 2.3. Dataloader settings in application-(dev/prod).properties:
### > app.config.data.adminUsername/Password:
Everytime the backend service is started, the dataloader will check if at least one admin-user exists. 
If it does not, the dataloader will create an admin-user with these parameters.

### > app.config.data.loadTestData= true / false
On startup the dataloader will check if it should run the "TestDataLoader.java" class.
The TestDataLoader class reads in test_data.json from the backend ressources folder.

 <a href="#top">Back to top</a>

## 2.4. Other application.properties settings:
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

## 2.5. Test Data
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

# 3. JAVADOC GENERATION

 - to (re)generate the Javadoc for this project run these commands in \backend\modulecrediting :
    
    - mvn clean (optional)
    - mvn install

    - alternatively "mvn javadoc:javadoc" can be run aswell

 - NOTE: in production the first two commands are executed everytime the pipeline makes a new build of the backend

 - afterwards the documentation can be found under \backend\modulecrediting\target\apidocs\index.html      
 - it is recommended to open the file with a browser and not in an IDE


 <a href="#top">Back to top</a>

# 4. API ENDPOINTS
## 4.1. Overview of Views: "\model\Views.java"

In some cases Views are used, instead of DTOs, to generate ResponseData

 - ApplicationLoginOverview
    - request: GET: /api/applications
    - includes: 
         - application:
            - id
            - fullstatus
            - creationDate
            - lastEditedDate
            - decisionDate
         - courseLeipzig:
            - name
         - list of modulesConnections:
            - list of externalModules:
               - name
               - university
               - externalCourse

 - ApplicationLogin
    - request: GET: /api/applications/{id}
    - extends: ApplicationLoginOverview
    - includes:
         - list of modulesConnections:
            - id 
            - commentApplicant
            - commentStudyOffice
            - decisionSuggestion
            - commentDecision
            - decisionFinal
            - formalRejection
            - formalRejectionComment
            - list of modulesLeipzig:
               - name
            - list of externalModules:
               - id
               - points
               - pointsystem
               - pdfDocument:
                  - id
                  - name

 - ApplicationStudent
    - request: GET: /api/applications/student/{id}
    - includes:
      - application:
         - id 
         - fullstatus
         - creationDate
         - decisionDate
         - courseLeipzig:
            - name
         - list of modulesConnections:
            - modulesConnectionOriginal (includes same fields as modulesConnection)
            - id
            - commentApplicant
            - commentDecision
            - decisionFinal
            - formalRejection
            - formalRejectionComment
            - list of modulesLeipzig:
               - name
            - list of externalModules:
               - id
               - name
               - points
               - pointsystem
               - university
               - externalCourse
               - pdfDocument:
                  - id
                  - name

 - CoursesWithModules
    - request: GET: /api/courses-leipzig
    - inlcudes: 
      - courseLeipzig:
         - name
         - isActive
         - list of modulesLeipzig:
            - name
            - code
            - isActive

 - ModulesWithoutCourse
    - request: GET: /api/modules-leipzig
    - includes: 
      - modulesLeipzig:
         - name
         - code
         - isActive

 - RelatedModulesConnection
    - request: GET: /api/modules-connection/{id}/related
    - includes: 
      - modulesConnection:
         - id
         - decisionFinal
         - application:
            - name
            - decisionDate
            - courseLeipzig
               - name
         - list of externalModules
            - name
            - university
            - exernalCourse
         - list of moduleLeipzig
            - name
            
 <a href="#top">Back to top</a>

## 4.2. Application: "ApplicationController"
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


## 4.3. Authentication: "AuthController"
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


## 4.4. Studiengaenge in Leipzig: "CourseLeipzigController"
### _**GET - Requests:**_

#### > **/api/courses-leipzig**
 - returns "list" of all "CourseLeipzig" 
 - http://localhost:8090/api/courses-leipzig

<a href="#top">Back to top</a>

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

<a href="#top">Back to top</a>

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

<a href="#top">Back to top</a>

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
       
 <a href="#top">Back to top</a>


## 4.5. Handling of JSON files: "JsonFileController" 
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


## 4.6. Module in Leipzig: "ModuleLeipzigController"
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


## 4.7. ModulesConnection: "ModulesConnectionController"
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


## 4.8. Handling of PDF files: "PdfDocumentContoller"
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


## 4.9. User: "UserController"
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

# 5. Folder structure explanation 
## 5.1. Backend

### resources:
- The static- and templates-folders are for the pdf generation.
- The application*.properties are explained in
<a href="#21-setup-backend-and-database-development">2.1.</a>, 
<a href="#23-dataloader-settings-in-application-devprodproperties">2.3.</a> and 
<a href="#24-other-applicationproperties-settings">2.4.</a>
- The 'test_data.json' is the config for the 'TestDataLoader'. For more see <a href="#25-test-data">here</a>

#### The following folder and files are in the \src folder:
### config: 
- These are all the springsecurity config classes
### controller: 
- These classes are the controller of the API-Endpoints
### dto: 
- These are all the DTO classes used for requests and responses
### model: 
- These classes are the 'Entity' classes of the app
### repository: 
- These classes are responsible for the communication between the backend and the database
### service: 
- These classes are the main 'logic' classes of this app
### util: 
- These classes have mostly 'utility' methods
### ModulecreditingApplication.java: 
- This is the 'main'
### Dataloader.java: 
- This class is responsible for writing, if it not already exist, the necessary data into the database on startup.
- It can start the TestDataLoader with a setting change in application-dev.properties
### TestDataLoader.java: 
- This class creates testdata for development purposes 

 <a href="#top">Back to top</a>

## 5.2. Frontend
#### The following folder and files are in the \src folder:
### assets:
- todo
### components: 
- todo
### config:
- todo
### i18n:
- todo
### requests:
- todo
### router:
- todo
### store:
- todo
### utils:
- todo
### views:
- todo
### App.vue:
- todo
### main.js:
- todo

 <a href="#top">Back to top</a>

end of file 

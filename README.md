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

.

# API ENDPOINTS

## Application: "ApplicationController"

### GET - Requests:

http://localhost:8090/api/applications -> returns a "List" of all "Applications"    
"Views.ApplicationOverview.class"

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

    true / false
.


### POST - Requests:

http://localhost:8090/api/courses-leipzig/create -> needs "CourseLeipzigCreateDTO", returns "name" of created CourseLeipzig

    id
.


### PUT - Requests:

http://localhost:8090/api/courses-leipzig/{name}/edit -> needs {name} "EditCourseDTO", returns "Boolean"

    true / false
.


### DELETE - Requests:

http://localhost:8090/api/courses-leipzig/{name}/delete -> needs {name}, returns 

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

    true / false
.


### POST - Requests:

http://localhost:8090/api/modules-leipzig/create -> needs "ModuleLeipzigCreateDTO", returns "name" of created CourseLeipzig

    id
.


### PUT - Requests:

http://localhost:8090/api/modules-leipzig/{name}/edit -> needs {name} "ModuleLeipzigUpdateDTO", returns "Boolean"

    true / false
.


### DELETE - Requests:

http://localhost:8090/api/modules-leipzig/{name}/delete -> needs {name}, returns 

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
    HttpStatus.BAD_REQUEST  -> User doesnt exists! 
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



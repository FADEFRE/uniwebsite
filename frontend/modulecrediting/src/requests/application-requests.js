import httpClient from "@/requests/httpClient";
import { consoleDebug } from "@/requests/consoleDebug";

let axiosColor = "color:deepskyblue";


// helper - creates basicFormData for PUT-Requests
// basicConnectionObjects has to be array containing objects with below used data
function createBasicFormData(courseLeipzig, basicConnectionObjects) {
    const formData = new FormData();
    formData.append("courseLeipzig", courseLeipzig);
    basicConnectionObjects.forEach((connection, connectionIndex) => {
        if (connection.id) {
            formData.append(`modulesConnections[${connectionIndex}].id`, connection.id);
        }
        connection.externalModules.forEach(
            (externalModule, externalModuleIndex) => {
                if (externalModule.id) {
                    formData.append(
                        `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].id`,
                        externalModule.id
                    );
                }
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].name`,
                    externalModule.name
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].points`,
                    externalModule.points
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].pointSystem`,
                    externalModule.pointSystem
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].university`,
                    externalModule.university
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].externalCourse`,
                    externalModule.externalCourse
                );
            }
        );
        connection.internalModules.forEach((moduleName, moduleIndex) => {
            formData.append(
                `modulesConnections[${connectionIndex}].modulesLeipzig[${moduleIndex}].name`,
                moduleName
            );
        });
    });
    return formData;
}

/*
GET-Request to '/api/applications' endpoint
returns list of applications (containing only data needed for overview)

parameters:
    none
 */
function getApplications() {
    consoleDebug(axiosColor, "getApplications ()");

    return httpClient
        .get("/api/applications")
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
GET-Request to '/api/applications/{id}' endpoint
returns data of the application

parameters:
    id - Number, application id
 */
function getApplicationById(id) {
    consoleDebug(axiosColor, "getApplicationById (" + id + ")");

    return httpClient
        .get("/api/applications/" + id)
        .then((response) => {
            response.data["modulesConnections"].sort((a, b) => a.id - b.id);
            response.data["modulesConnections"].forEach((connection) => {
                connection["externalModules"].sort((a, b) => a.id - b.id);
            });
            return response.data;
        })
        .catch((_) => {
        });
}

/*
GET-Request to '/api/applications/student/{id}' endpoint
returns data of the application (only data that should be visible to applicants)

parameters:
    id - Number, application id
 */
function getApplicationByIdForStatus(id) {
    consoleDebug(axiosColor, "getApplicationByIdForStatus (" + id + ")");

    return httpClient
        .get("/api/applications/student/" + id)
        .then((response) => {
            response.data["modulesConnections"].sort((a, b) => a.id - b.id);
            response.data["modulesConnections"].forEach((connection) => {
                connection["externalModules"].sort((a, b) => a.id - b.id);
            });
            return response.data;
        })
        .catch((_) => {
        });
}

/*
GET-Request to '/api/applications/{id}/exists' endpoint
returns true if application with id exists
else returns false

parameters:
    id - Number, application id
 */
function getApplicationExists(id) {
    consoleDebug(axiosColor, "getApplicationExists ()");

    return httpClient
        .get(`/api/applications/${id}/exists`)
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
GET-Request to '/api/applications/{id}/related' endpoint
returns list of all related module-connections

parameters:
    moduleConnectionId - Number, module connection id
 */
function getRelatedModuleConnections(moduleConnectionId) {
    consoleDebug(axiosColor, "getRelatedModuleConnections (moduleConnectionId: " + moduleConnectionId + ")");

    return httpClient
        .get("/api/modules-connection/" + moduleConnectionId + "/related")
        .then((response) => {
            return response.data;
        })
        .catch((_) => {
        });
}

/*
GET-Request to '/api/applications/{id}/update-status-allowed' endpoint
returns true/false if updating application status is allowed

parameters:
    - id, Number application id
 */
function getUpdateStatusAllowed(id) {
    consoleDebug(axiosColor, "getUpdateStatusAllowed (id: " + id + ")");

    return httpClient
        .get("/api/applications/" + id + "/update-status-allowed")
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
POST-Request to '/api/applications' endpoint
posts a new application

parameters:
    course - String, must match a backend option
    applicationObjects - array of objects, each containing ...
        externalModules, list of objects, each containing ...
            String name, String points, String pointSystem, String university, String externalCourse, PDF description
        internalModules, list of Strings (must match existing module names)
        commentApplicant, String comment
 */
function postApplication(course, applicationObjects) {
    consoleDebug(axiosColor, "postApplication (course: " + course + ", applicationObjects: " + applicationObjects + ")");

    const formData = new FormData();
    formData.append(`courseLeipzig`, course);

    applicationObjects.forEach((connection, connectionIndex) => {
        connection.externalModules.forEach(
            (externalModule, externalModuleIndex) => {
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].name`,
                    externalModule.name
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].points`,
                    externalModule.points
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].pointSystem`,
                    externalModule.pointSystem
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].university`,
                    externalModule.university
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].externalCourse`,
                    externalModule.externalCourse
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].description`,
                    externalModule.selectedFile
                );
            }
        );
        connection.internalModules.forEach((moduleName, moduleIndex) => {
            formData.append(
                `modulesConnections[${connectionIndex}].modulesLeipzig[${moduleIndex}].name`,
                moduleName
            );
        });
        formData.append(
            `modulesConnections[${connectionIndex}].commentApplicant`,
            connection.commentApplicant
        );
    });

    return httpClient
        .post("/api/applications", formData)
        .then((response) => {
            return response.data;
        })
        .catch((_) => {
        });
}

/*
PUT-request to '/api/applications/student/{id}' endpoint
puts new application data

parameters:
    applicationId - Number, application id
    courseLeipzig - String, must match an existing course
    applicationObjects - array of objects, each containing ...
        id, Number, connection id
        externalModules, list of objects, each containing ...
            Number id, String name, String points, String pointSystem, String university, String externalCourse, (optional) PDF description
        internalModules, list of Strings (must match existing module names)
        commentApplicant, String comment
 */
function putApplicationStudent(applicationId, courseLeipzig, connectionObjects) {
    consoleDebug(axiosColor, "putApplicationStandard (applicationId: " + applicationId + ", courseLeipzig: " + courseLeipzig + ", connectionsObjects: " + connectionObjects + ")");

    const formData = createBasicFormData(courseLeipzig, connectionObjects);

    connectionObjects.forEach((connection, connectionIndex) => {
        formData.append(
            `modulesConnections[${connectionIndex}].commentApplicant`,
            connection.commentApplicant
        );
        connection.externalModules.forEach((externalModule, externalModuleIndex) => {
                if (externalModule.selectedFile && externalModule.selectedFile instanceof File) {
                    formData.append(
                        `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].description`,
                        externalModule.selectedFile
                    );
                }
            }
        );
    });

    return httpClient
        .put("/api/applications/student/" + applicationId, formData)
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
PUT-request to '/api/applications/study-office/{id}' endpoint
puts modified application data and decision data

parameters:
    applicationId - Number, application id
    courseLeipzig - String, must match an existing course
    applicationObjects - array of objects, each containing ...
        id, Number, connection id
        externalModules, list of objects, each containing ...
            Number id, String name, String points, String pointSystem, String university, String externalCourse, (optional) PDF description
        internalModules, list of Strings (must match existing module names)
        formalRejectionData, object containing ...
            Boolean formalRejection, String comment
        studyOfficeDecisionData, object containing ...
            Boolean decision, String comment
 */
function putApplicationStudyOffice(applicationId, courseLeipzig, connectionObjects) {
    consoleDebug(axiosColor, "putApplicationStudyOffice (applicationId: " + applicationId + ", courseLeipzig: " + courseLeipzig + ", connectionsObjects: " + connectionObjects + ")");

    const formData = createBasicFormData(courseLeipzig, connectionObjects);

    connectionObjects.forEach((connection, connectionIndex) => {
        if (connection.formalRejectionData["formalRejection"]) {
            formData.append(
                `modulesConnections[${connectionIndex}].formalRejection`,
                true
            );
            formData.append(
                `modulesConnections[${connectionIndex}].formalRejectionComment`,
                connection.formalRejectionData.comment
            );
        } else {
            formData.append(
                `modulesConnections[${connectionIndex}].formalRejection`,
                false
            );
            formData.append(
                `modulesConnections[${connectionIndex}].formalRejectionComment`,
                ""
            );
            if (connection.studyOfficeDecisionData["decision"]) {
                formData.append(
                    `modulesConnections[${connectionIndex}].decisionSuggestion`,
                    connection.studyOfficeDecisionData.decision
                );
            }
            if (connection.studyOfficeDecisionData["comment"]) {
                formData.append(
                    `modulesConnections[${connectionIndex}].commentStudyOffice`,
                    connection.studyOfficeDecisionData.comment
                );
            }
        }
    });

    return httpClient
        .put("/api/applications/study-office/" + applicationId, formData)
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
PUT-request to '/api/applications/chairman/{id}' endpoint
puts modified application data and decision data

parameters:
    applicationId - Number, application id
    courseLeipzig - String, must match an existing course
    applicationObjects - array of objects, each containing ...
        id, Number, connection id
        externalModules, list of objects, each containing ...
            Number id, String name, String points, String pointSystem, String university, String externalCourse, (optional) PDF description
        internalModules, list of Strings (must match existing module names)
        chairmanDecisionData, object containing ...
            Boolean decision, String comment
 */
function putApplicationChairman(applicationId, courseLeipzig, connectionObjects) {
    consoleDebug(axiosColor, "putApplicationStudyOffice (applicationId: " + applicationId + ", courseLeipzig: " + courseLeipzig + ", connectionsObjects: " + connectionObjects + ")");

    const formData = createBasicFormData(courseLeipzig, connectionObjects);

    connectionObjects.forEach((connection, connectionIndex) => {
        if (connection.chairmanDecisionData["decision"]) {
            formData.append(
                `modulesConnections[${connectionIndex}].decisionFinal`,
                connection.chairmanDecisionData.decision
            );
        }
        if (connection.chairmanDecisionData["comment"]) {
            formData.append(
                `modulesConnections[${connectionIndex}].commentDecision`,
                connection.chairmanDecisionData.comment
            );
        }
    });

    return httpClient
        .put("/api/applications/chairman/" + applicationId, formData)
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
PUT-Request to '/api/applications/{id}/update-status' endpoint
updates application status if possible

parameters:
    - id, Number application id
 */
function putUpdateStatus(id) {
    consoleDebug(axiosColor, "updateStatus (id: " + id + ")");

    return httpClient
        .put("/api/applications/" + id + "/update-status")
        .then((response) => {
            return response.data;
        })
        .catch((_) => {
        });
}

export {
    // GET-requests
    getApplications,
    getApplicationById,
    getApplicationByIdForStatus,
    getApplicationExists,
    getRelatedModuleConnections,
    getUpdateStatusAllowed,
    // POST-requests
    postApplication,
    // PUT-requests
    putApplicationStudent,
    putApplicationStudyOffice,
    putApplicationChairman,
    putUpdateStatus,
}
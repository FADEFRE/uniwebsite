import httpClient from "@/requests/httpClient";

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
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].university`,
                    externalModule.university
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].points`,
                    externalModule.points
                );
                formData.append(
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].pointSystem`,
                    externalModule.pointSystem
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
GET-Request to '/applications' endpoint
returns list of applications (only data needed for overview)

parameters:
    none
 */
function getApplications() {
    console.debug("%c" + "getApplications ()", axiosColor);

    return httpClient
        .get("/api/applications")
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
GET-Request to '/applications/{id}' endpoint
returns data of the related application

parameters:
    id - Number, application id
 */
function getApplicationById(id) {
    console.debug("%c" + "getApplicationById (" + id + ")", axiosColor);

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
GET-Request to '/applications/student/{id}' endpoint
return application data without study office and chairman data

parameters:
    id - Number, application id
 */
function getApplicationByIdForStatus(id) {
    console.debug("%c" + "getApplicationByIdForStatus (" + id + ")", axiosColor);

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
GET-Request to '/applications/{id}/exists' endpoint
returns true if application with id exists: else returns false

parameters:
    id - Number, application id
 */
function getApplicationExists(id) {
    console.debug("%c" + "getApplicationExists ()", axiosColor);

    return httpClient
        .get(`/api/applications/${id}/exists`)
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
GET-Request to '/applications/{id}/related' endpoint
returns related module connections

parameters:
    moduleConnectionId - Number, module connection id
 */
function getRelatedModuleConnections(moduleConnectionId) {
    console.debug("%c" + "getRelatedModuleConnections (moduleConnectionId: " + moduleConnectionId + ")", axiosColor);

    return httpClient
        .get("/api/modules-connection/" + moduleConnectionId + "/related")
        .then((response) => {
            return response.data;
        })
        .catch((_) => {
        });
}

/*
GET-Request to /applications/{id}/update-status-allowed endpoint
returns true/false if updating application status is allowed

parameters:
    - id, Number application id
 */
function getUpdateStatusAllowed(id) {
    console.debug(
        "%c" + "getUpdateStatusAllowed (id: " + id + ")",
        axiosColor
    );

    return httpClient
        .get("/api/applications/" + id + "/update-status-allowed")
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
POST-Request to '/applications' endpoint
posts a new application

parameters:
    course - String, must match a backend option
    applicationObjects - array of objects, each containing String moduleName, String university, Number CreditPoints, ...
    ... String pointSystem, File descriptionFile, String comment, array of Strings selectedInternalModules
 */
function postApplication(course, applicationObjects) {
    console.debug("%c" + "postApplication (course: " + course + ", applicationObjects: " + applicationObjects + ")", axiosColor);

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
                    `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].university`,
                    externalModule.university
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

function putApplicationStudent(
    applicationId,
    courseLeipzig,
    connectionObjects
) {
    console.debug(
        "%c" +
        "putApplicationStandard (applicationId: " +
        applicationId +
        ", courseLeipzig: " +
        courseLeipzig +
        ", connectionsObjects: " +
        connectionObjects +
        ")",
        axiosColor
    );

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

function putApplicationStudyOffice(
    applicationId,
    courseLeipzig,
    connectionObjects
) {
    console.debug(
        "%c" +
        "putApplicationStudyOffice (applicationId: " +
        applicationId +
        ", courseLeipzig: " +
        courseLeipzig +
        ", connectionsObjects: " +
        connectionObjects +
        ")",
        axiosColor
    );

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

function putApplicationChairman(
    applicationId,
    courseLeipzig,
    connectionObjects
) {
    console.debug(
        "%c" +
        "putApplicationStudyOffice (applicationId: " +
        applicationId +
        ", courseLeipzig: " +
        courseLeipzig +
        ", connectionsObjects: " +
        connectionObjects +
        ")",
        axiosColor
    );

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
PUT-Request to /applications/{id}/update-status endpoint
updates application status if possible

parameters:
    - id, Number application id
 */
function putUpdateStatus(id) {
    console.debug("%c" + "updateStatus (id: " + id + ")", axiosColor);

    return httpClient
        .put("/api/applications/" + id + "/update-status")
        .then((response) => {
            if (response.data) {
                console.log("update successful");
            } else {
                console.log("update was not successful");
            }
            return response.data;
        })
        .catch((_) => {
        });
}

export {
    getApplications,
    getApplicationById,
    getApplicationByIdForStatus,
    getApplicationExists,
    getRelatedModuleConnections,
    getUpdateStatusAllowed,

    postApplication,

    putApplicationStudent,
    putApplicationStudyOffice,
    putApplicationChairman,
    putUpdateStatus,
}
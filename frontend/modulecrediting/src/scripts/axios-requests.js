import { url } from "@/scripts/url-config.js";
import httpResource from "@/scripts/httpResource";

/*
GET-Request to '/courses-leipzig' endpoint
return list of all course names

parameters:
    none
 */
function getCoursesLeipzig() {
  console.debug("%c" + "getCoursesLeipzig ()", "color:yellow");

  return httpResource
    .get("/api/courses-leipzig")
    .then((response) => {
      return response.data.map((obj) => obj.name);
    })
    .catch((_) => {});
}

/*
GET-Request to '/courses-leipzig' endpoint
returns a list containing all modules related to a specific course

parameters:
    course - String, course name
 */
function getModulesByCourse(course) {
  console.debug("%c" + "getModulesByCourse (" + course + ")", "color:yellow");

  return httpResource
    .get("/api/courses-leipzig")
    .then((response) => {
      const courseObject = response.data.find((obj) => obj.name === course);
      return courseObject.modulesLeipzigCourse
        .filter((m) => m.isActive)
        .map((obj) => obj.name)
        .sort();
    })
    .catch((_) => {});
}

/*
GET-Request to '/applications' endpoint
returns list of applications (only data needed for overview)

parameters:
    none
 */
function getApplications() {
  console.debug("%c" + "getApplications ()", "color:yellow");

  return httpResource
    .get("/api/applications")
    .then((response) => response.data)
    .catch((_) => {});
}

/*
GET-Request to '/applications/{id}' endpoint
returns data of the related application

parameters:
    id - Number, application id
 */
function getApplicationById(id) {
  console.debug("%c" + "getApplicationById (" + id + ")", "color:yellow");

  return httpResource
    .get("/api/applications/" + id)
    .then((response) => {
      response.data["modulesConnections"].sort((a, b) => a.id - b.id);
      response.data["modulesConnections"].forEach((connection) => {
        connection["externalModules"].sort((a, b) => a.id - b.id);
      });
      return response.data;
    })
    .catch((_) => {});
}

/*
GET-Request to '/applications/student/{id} endpoint
return application data without study office and chairman data

parameters:
    id - Number, application id
 */
function getApplicationByIdForStatus(id) {
  console.debug(
    "%c" + "getApplicationByIdForStatus (" + id + ")",
    "color:yellow"
  );
  return httpResource
    .get("/api/applications/student/" + id)
    .then((response) => {
      response.data["modulesConnections"].sort((a, b) => a.id - b.id);
      response.data["modulesConnections"].forEach((connection) => {
        connection["externalModules"].sort((a, b) => a.id - b.id);
      });
      return response.data;
    })
    .catch((_) => {});
}

/*
GET-Request to '/applications/{id}/exists' endpoint
returns true if application with id exists: else returns false

parameters:
    id - Number, application id
 */
function getApplicationExists(id) {
  console.debug("%c" + "getApplicationExists ()", "color:yellow");

  return httpResource
    .get(url + `/api/applications/${id}/exists`)
    .then((response) => response.data)
    .catch((_) => {});
}

/*
GET-Request to '/applications/{id}/related' endpoint
returns related module connections

parameters:
    moduleConnectionId - Number, module connection id
 */
function getRelatedModuleConnections(moduleConnectionId) {
  console.debug(
    "%c" +
      "getRelatedModuleConnections (moduleConnectionId: " +
      moduleConnectionId +
      ")",
    "color:yellow"
  );

  return httpResource
    .get("/api/modules-connection/" + moduleConnectionId + "/related")
    .then((response) => {
      return response.data;
    })
    .catch((_) => {});
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
  console.debug(
    "%c" +
      "postApplication (course: " +
      course +
      ", applicationObjects: " +
      applicationObjects +
      ")",
    "color:yellow"
  );

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
        `modulesConnections[${connectionIndex}].modulesLeipzig[${moduleIndex}]`,
        moduleName
      );
    });
    formData.append(
      `modulesConnections[${connectionIndex}].commentApplicant`,
      connection.commentApplicant
    );
  });

  return httpResource
    .post("/api/applications", formData)
    .then((response) => {
      return response.data;
    })
    .catch((_) => {});
}

// helper - creates basicFormData for PUT-Requests
// basicConnectionObjects has to be array containing objects with below used data
function createBasicFormData(courseLeipzig, basicConnectionObjects) {
  const formData = new FormData();
  formData.append("courseLeipzig", courseLeipzig);
  basicConnectionObjects.forEach((connection, connectionIndex) => {
    formData.append(`modulesConnections[${connectionIndex}].id`, connection.id);
    connection.externalModules.forEach(
      (externalModule, externalModuleIndex) => {
        formData.append(
          `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].id`,
          externalModule.id
        );
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

function putApplicationStandard(
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
    "color:yellow"
  );

  const formData = createBasicFormData(courseLeipzig, connectionObjects);

  connectionObjects.forEach((connection, connectionIndex) => {
    formData.append(
      `modulesConnections[${connectionIndex}].commentApplicant`,
      connection.commentApplicant
    );
    connection.externalModules.forEach(
      (externalModule, externalModuleIndex) => {
        if (externalModule.selectedFile) {
          formData.append(
            `modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].description`,
            externalModule.selectedFile
          );
        }
      }
    );
  });

  return httpResource
    .put(url + "/api/applications/standard/" + applicationId, formData)
    .then((response) => response.data)
    .catch((_) => {});
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
    "color:yellow"
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
        formData.append(
          `modulesConnections[${connectionIndex}].commentStudyOffice`,
          connection.studyOfficeDecisionData.comment
        );
      }
    }
  });

  return httpResource
    .put("/api/applications/study-office/" + applicationId, formData)
    .then((response) => response.data)
    .catch((_) => {});
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
        "color:yellow"
    );

    const formData = createBasicFormData(courseLeipzig, connectionObjects);

    connectionObjects.forEach((connection, connectionIndex) => {
        if (connection.chairmanDecisionData["decision"]) {
            formData.append(
                `modulesConnections[${connectionIndex}].decisionFinal`,
                connection.chairmanDecisionData.decision
            );
            formData.append(
                `modulesConnections[${connectionIndex}].commentDecision`,
                connection.chairmanDecisionData.comment
            );
        }
    });

    return httpResource
        .put("/api/applications/chairman/" + applicationId, formData)
        .then((response) => response.data)
        .catch((_) => {});
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
    "color:yellow"
  );

  return httpResource
    .get("/api/applications/" + id + "/update-status-allowed")
    .then((response) => response.data)
    .catch((_) => {});
}

/*
PUT-Request to /applications/{id}/update-status endpoint
updates application status if possible

parameters:
    - id, Number application id
 */
function updateStatus(id) {
  console.debug("%c" + "updateStatus (id: " + id + ")", "color:yellow");

  return httpResource
    .put("/api/applications/" + id + "/update-status")
    .then((response) => {
      if (response.data) {
        console.log("update successful");
      } else {
        console.log("update was not successful");
      }
      return response.data;
    })
    .catch((_) => {});
}

/*
POST-Request to '/api/courses-leipzig' endpoint
create new course leipzig

parameters:
    name of course
 */
function postCourseLeipzig(coursename) {
  console.debug(
    "%c" + "create course leipzig (name: " + coursename + ")",
    "color:yellow"
  );

  const formData = new FormData();
  formData.append(`courseName`, coursename);

  return httpResource
    .post("/api/courses-leipzig", formData)
    .then((response) => {
      return response.data;
    })
    .catch((_) => {});
}

/*
POST-Request to '/api/modules-leipzig' endpoint
create new module leipzig

parameters:
    name of module
    modulecode
 */
function postModuleLeipzig(modulename, modulecode) {
  console.debug(
    "%c" + "create module leipzig (name: " + modulename + ")",
    "color:yellow"
  );

  const formData = new FormData();
  formData.append(`name`, modulename);
  formData.append(`code`, modulecode);

  return httpResource
    .post("/api/modules-leipzig", formData)
    .then((response) => {
      return response.data;
    })
    .catch((_) => {});
}

export {
  getCoursesLeipzig,
  getModulesByCourse,
  getApplications,
  getApplicationById,
  getApplicationByIdForStatus,
  getApplicationExists,
  getRelatedModuleConnections,
  postApplication,
  putApplicationStandard,
  putApplicationStudyOffice,
  putApplicationChairman,
  getUpdateStatusAllowed,
  updateStatus,
  postCourseLeipzig,
  postModuleLeipzig,
};

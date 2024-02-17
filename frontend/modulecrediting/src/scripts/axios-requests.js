import { url } from "@/scripts/url-config.js";
import httpResource from "@/scripts/httpResource";

let axiosColor = "color:blue";

/*
GET-Request to '/courses-leipzig' endpoint
return list of all course names
removes non active courses
parameters:
    none
 */
function getCoursesLeipzigName() {
  console.debug("%c" + "getCoursesLeipzigName ()", axiosColor);

  return httpResource
    .get("/api/courses-leipzig")
    .then((response) => {
      return response.data.filter(course => course.isActive).map((obj) => obj.name);
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
  console.debug("%c" + "getModulesByCourse (" + course + ")", axiosColor);

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

function putCourseLeipzigEdit(coursename, moduleList) {
  console.debug("%c" + "putCourseLeipzigEdit", axiosColor);

  const formData = new FormData();

  moduleList.forEach((moduleLeipzig, moduleIndex) => {
    formData.append(`modulesLeipzig[${moduleIndex}].name`, moduleLeipzig.name);
    formData.append(`modulesLeipzig[${moduleIndex}].code`, moduleLeipzig.code);
  });

  return httpResource
    .put(`/api/courses-leipzig/${coursename}/edit`, formData)
    .then((response) => response.data)
    .catch((_) => {});
}

function putUpdateCourseLeipzig(courseName, newCourseName) {
  console.debug("%c" + "putUpdateCourseLeipzig", axiosColor);

  const formData = new FormData();
  formData.append("courseName", newCourseName);

  return httpResource
    .put(`/api/courses-leipzig/${courseName}`, formData)
    .then((response) => response.data)
    .catch((_) => {});
}

function deleteCourseLeipzig(coursename) {
  console.debug("%c" + "deleteCourseLeipzig", axiosColor);

  return httpResource
    .delete(`/api/courses-leipzig/${coursename}`)
    .then((response) => response.data)
    .catch((_) => {});
}

/*
GET-Request to '/courses-leipzig' endpoint
returns a list containing all modules related to a specific course
{name: module name, code: module code }

parameters:
    course - String, course name
 */
function getModulesNameCodeByCourse(course) {
  console.debug("%c" + "getModulesByCourse (" + course + ")", axiosColor);

  return httpResource
    .get("/api/courses-leipzig")
    .then((response) => {
      const courseObject = response.data.find((obj) => obj.name === course);
      return courseObject.modulesLeipzigCourse
        .filter((m) => m.isActive)
        .map((obj) => ({ name: obj.name, code: obj.code }))
        .sort((a, b) => a.name.localeCompare(b.name));
    })
    .catch((_) => {});
}

function getModulesNameCode() {
  console.debug("%c" + "getModulesNameCode", axiosColor);

  return httpResource
    .get("/api/modules-leipzig")
    .then((response) => {
      return response.data
        .filter((singleModule) => singleModule.isActive)
        .map((singleModule) => ({
          name: singleModule.name,
          code: singleModule.code,
        }))
        .sort((a, b) => a.name.localeCompare(b.name));
    })
    .catch((_) => {});
}

function putUpdateModuleLeipzig(moduleName, newModuleName, newModuleCode) {
    console.debug("%c" + "putUpdateModuleLeipzig (moduleName: " + moduleName + ", newModuleName: " + newModuleName + ", newModuleCode: " + newModuleCode + ")", axiosColor)

    const formData = new FormData()

    formData.append('name', newModuleName)
    formData.append('code', newModuleCode)

    return httpResource.put(`/api/modules-leipzig/${moduleName}`, formData)
        .then(response => response.data)
        .catch(_ => {})
}

function deleteModuleLeipzig(modulename) {
  console.debug("%c" + "deleteModuleLeipzig", axiosColor);

  return httpResource
    .delete(`/api/modules-leipzig/${modulename}`)
    .then((response) => response.data)
    .catch((_) => {});
}

/*
GET-Request to '/applications' endpoint
returns list of applications (only data needed for overview)

parameters:
    none
 */
function getApplications() {
  console.debug("%c" + "getApplications ()", axiosColor);

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
  console.debug("%c" + "getApplicationById (" + id + ")", axiosColor);

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
  console.debug("%c" + "getApplicationByIdForStatus (" + id + ")", axiosColor);

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
  console.debug("%c" + "getApplicationExists ()", axiosColor);

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
  console.debug("%c" + "getRelatedModuleConnections (moduleConnectionId: " + moduleConnectionId + ")", axiosColor);

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
  console.debug("%c" + "postApplication (course: " +  course + ", applicationObjects: " +  applicationObjects + ")", axiosColor);

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

  return httpResource
    .put(url + "/api/applications/student/" + applicationId, formData)
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
    axiosColor
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
  console.debug("%c" + "updateStatus (id: " + id + ")", axiosColor);

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
    axiosColor
  );

  const formData = new FormData();
  formData.append(`courseName`, coursename);

  return httpResource
    .post("/api/courses-leipzig", formData)
    .then((response) => response.data)
    .catch((error) => Promise.reject(error));
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
    axiosColor
  );

  const formData = new FormData();
  formData.append(`name`, modulename);
  formData.append(`code`, modulecode);

  return httpResource
    .post("/api/modules-leipzig", formData)
    .then((response) => response.data)
    .catch((error) => Promise.reject(error));
}

function getUserMe () {
    console.debug("%c" + "getUserMe ()", axiosColor)

    return httpResource.get("/api/user/me")
        .then(response => response.data)
        .catch(() => {})
}

function getUserMeId () {
  console.debug("%c" + "getUserMeId ()", axiosColor)

  return httpResource.get("/api/user/me/id")
      .then(response => response.data)
      .catch(() => {})
}

function getUserMeName () {
  console.debug("%c" + "getUserMeName ()", axiosColor)

  return httpResource.get("/api/user/me/name")
      .then(response => response.data)
      .catch(() => {})
}

function getAllUsers () {
    console.debug("%c" + "getAllUsers ()", axiosColor);

    return httpResource.get("/api/user/all")
        .then(response => response.data)
        .catch(_ => {})
}

function putUserUsername (id, username) {
    console.debug("%c" + "getUserUsername (id: " + id + ", username: " + username + ")", axiosColor)

    const formData = new FormData()

    formData.append('id', id)
    formData.append('username', username)

    return httpResource.put('/api/user/change/username', formData)
        .then(response => response.data)
        .catch(error => Promise.reject(error))
}

function putUserPassword (id, password, passwordConfirm) {
    console.debug("%c" + "putUserPassword (id: " + id + ", password: [hidden]" + ", passwordConfirm: [hidden]" + ")", axiosColor)

    const formData = new FormData()

    formData.append('id', id)
    formData.append('password', password)
    formData.append('passwordConfirm', passwordConfirm)

    return httpResource.put('/api/user/change/password', formData)
        .then(response => response.data)
        .catch(_ => {})
}

function putUserRole (id, role) {
    console.debug("%c" + "getAllUsers (id: " + id + ", role: " + role + ")", axiosColor);

    const formData = new FormData()

    formData.append('id', id)
    formData.append('role', role)

    return httpResource.put('/api/user/change/role', formData)
        .then(response => response.data)
        .catch(_ => {})
}

function deleteUser (id) {
    console.debug("%c" + "delteUser (id: " + id + ")", axiosColor)

    const formData = new FormData()

    formData.append('id', id)

    return httpResource.post('api/auth/delete', formData)
        .then(response => response.data)
        .catch(_ => {})
}

function createUser (username, password, passwordConfirm, role) {
    console.debug(
        "%c" + "create user (username: " + username + ", role: " + role + ")",
        axiosColor
    );

    const formData = new FormData()

    formData.append('username', username)
    formData.append('password', password)
    formData.append('passwordConfirm', passwordConfirm)
    formData.append('role', role)

    return httpResource.post("/api/auth/register", formData)
        .then(response => response.data)
        .catch(error => Promise.reject(error))

}

export {
    getCoursesLeipzigName,
    getModulesByCourse,
    getModulesNameCodeByCourse,
    getModulesNameCode,
    putUpdateModuleLeipzig,
    deleteModuleLeipzig,
    putCourseLeipzigEdit,
    putUpdateCourseLeipzig,
    deleteCourseLeipzig,
    getApplications,
    getApplicationById,
    getApplicationByIdForStatus,
    getApplicationExists,
    getRelatedModuleConnections,
    postApplication,
    putApplicationStudent,
    putApplicationStudyOffice,
    putApplicationChairman,
    getUpdateStatusAllowed,
    updateStatus,
    postCourseLeipzig,
    postModuleLeipzig,
    getUserMe,
    getUserMeId,
    getUserMeName,
    getAllUsers,
    putUserUsername,
    putUserPassword,
    putUserRole,
    deleteUser,
    createUser
};

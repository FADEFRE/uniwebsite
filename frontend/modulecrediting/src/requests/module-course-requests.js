import httpClient from "@/requests/httpClient";
import { consoleDebug } from "@/requests/consoleDebug";

let axiosColor = "color:deepskyblue";

/*
GET-Request to '/api/courses-leipzig' endpoint
return list with names of all active courses

parameters:
    none
 */
function getCoursesLeipzigName() {
    consoleDebug(axiosColor, "getCoursesLeipzigName ()");

    return httpClient
        .get("/api/courses-leipzig")
        .then((response) => {
            return response.data.filter(course => course.isActive).map((obj) => obj.name);
        })
        .catch((_) => {
        });
}

/*
GET-Request to '/api/courses-leipzig' endpoint
returns a sorted list with names of all modules associated to a specific course

parameters:
    course - String, course name
 */
function getModulesByCourse(course) {
    consoleDebug(axiosColor, "getModulesByCourse (" + course + ")");

    return httpClient
        .get("/api/courses-leipzig")
        .then((response) => {
            const courseObject = response.data.find((obj) => obj.name === course);
            return courseObject.modulesLeipzigCourse
                .filter((m) => m.isActive)
                .map((obj) => obj.name)
                .sort();
        })
        .catch((_) => {
        });
}

/*
GET-request to '/api/modules-leipzig' endpoint
returns a list of all active modules (each object containing name and code) sorted by name

parameters:
    none
 */
function getModulesNameCode() {
    consoleDebug(axiosColor, "getModulesNameCode");

    return httpClient
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
        .catch((_) => {
        });
}

/*
GET-Request to '/api/courses-leipzig' endpoint
returns a list of all active modules (each object containing name and code) associated to a specific course sorted by name

parameters:
    course - String, course name
 */
function getModulesNameCodeByCourse(course) {
    consoleDebug(axiosColor, "getModulesByCourse (" + course + ")");

    return httpClient
        .get("/api/courses-leipzig")
        .then((response) => {
            const courseObject = response.data.find((obj) => obj.name === course);
            return courseObject.modulesLeipzigCourse
                .filter((m) => m.isActive)
                .map((obj) => ({name: obj.name, code: obj.code}))
                .sort((a, b) => a.name.localeCompare(b.name));
        })
        .catch((_) => {
        });
}

/*
POST-Request to '/api/courses-leipzig' endpoint
create new course leipzig

parameters:
    courseName - String, name of course

throws:
    Conflict (409) if courseName already exists
 */
function postCourseLeipzig(courseName) {
    consoleDebug(axiosColor, "create course leipzig (name: " + courseName + ")");

    const formData = new FormData();
    formData.append(`courseName`, courseName);

    return httpClient
        .post("/api/courses-leipzig", formData)
        .then((response) => response.data)
        .catch((error) => Promise.reject(error));
}

/*
POST-Request to '/api/modules-leipzig' endpoint
create new module leipzig

parameters:
    moduleName - String, name of module
    moduleCode - String, code of module

throws:
    Conflict (409) if moduleName already exists
    Conflict (409) if moduleCode already exists
 */
function postModuleLeipzig(moduleName, moduleCode) {
    consoleDebug(axiosColor, "create module leipzig (name: " + moduleName + ")");

    const formData = new FormData();
    formData.append(`name`, moduleName);
    formData.append(`code`, moduleCode);

    return httpClient
        .post("/api/modules-leipzig", formData)
        .then((response) => response.data)
        .catch((error) => Promise.reject(error));
}

/*
POST-request to '/file/json/courses/upload' endpoint
configures courses and modules according to configuration file
new courses and modules will be created
existing courses and modules, that are not mentioned in the configuration file, will be deleted / set inactive

parameters:
    configFile - JSON file of structure like below

|file structure|
courses: [
    {
        name: ___,
        modules : [
            {
                name: ___,
                code: ___
            },
            ...
        ]
    },
    ...
]
 */
function postJsonConfig(configFile) {
    consoleDebug(axiosColor, "postJsonConfig (configFile: " + configFile + ")")

    const formData = new FormData()
    formData.append("jsonFile", configFile)

    return httpClient.post("/file/json/courses/upload", formData)
        .then(response => response.data)
        .catch(_ => {
        })
}

/*
PUT-request to '/api/courses-leipzig/{courseName}/edit' endpoint
modifies list of associated modules for a course

parameters:
    courseName, String name of the course to be modified
    moduleList, list of objects (modules to be associated to the course), each containing ...
        String name, String code
 */
function putCourseLeipzigEdit(courseName, moduleList) {
    consoleDebug(axiosColor, "putCourseLeipzigEdit");

    const formData = new FormData();

    moduleList.forEach((moduleLeipzig, moduleIndex) => {
        formData.append(`modulesLeipzig[${moduleIndex}].name`, moduleLeipzig.name);
        formData.append(`modulesLeipzig[${moduleIndex}].code`, moduleLeipzig.code);
    });

    return httpClient
        .put(`/api/courses-leipzig/${courseName}/edit`, formData)
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
PUT-request to '/api/courses-leipzig/{courseName}' endpoint
modifies name of a course

parameters:
    courseName - String, old name of the course
    newCourseName - String, new name of the course

throws:
    Conflict (409) if courseName already exists
 */
function putUpdateCourseLeipzig(courseName, newCourseName) {
    consoleDebug(axiosColor, "putUpdateCourseLeipzig", axiosColor);

    const formData = new FormData();
    formData.append("courseName", newCourseName);

    return httpClient
        .put(`/api/courses-leipzig/${courseName}`, formData)
        .then((response) => response.data)
}

/*
PUT-request to '/api/modules-leipzig/{moduleName}' endpoint
modifies name and code of a module

parameters:
    moduleName - String, old name of the module
    newModuleName - Sting, new name of the module
    newModuleCode - String, new code of the module

throws:
    Conflict (409) if moduleName already exists
    Conflict (409) if moduleCode already exists
 */
function putUpdateModuleLeipzig(moduleName, newModuleName, newModuleCode) {
    consoleDebug(axiosColor, "putUpdateModuleLeipzig (moduleName: " + moduleName + ", newModuleName: " + newModuleName + ", newModuleCode: " + newModuleCode + ")")

    const formData = new FormData()

    formData.append('name', newModuleName)
    formData.append('code', newModuleCode)

    return httpClient.put(`/api/modules-leipzig/${moduleName}`, formData)
        .then(response => response.data)
}

/*
DELETE-request to '/api/courses-leipzig/{courseName}' endpoint
deletes a course (or if referenced in any application will be set inactive)

parameters:
    courseName - String, name of the course to be deleted
 */
function deleteCourseLeipzig(courseName) {
    consoleDebug(axiosColor, "deleteCourseLeipzig");

    return httpClient
        .delete(`/api/courses-leipzig/${courseName}`)
        .then((response) => response.data)
        .catch((_) => {
        });
}

/*
DELETE-request to '/api/modules-leipzig/{moduleName}' endpoint
deletes a module (or if referenced in any application will be set inactive)

parameters:
    moduleName - String, name of the module to be deleted
 */
function deleteModuleLeipzig(moduleName) {
    consoleDebug(axiosColor, "deleteModuleLeipzig");

    return httpClient
        .delete(`/api/modules-leipzig/${moduleName}`)
        .then((response) => response.data)
        .catch((_) => {
        });
}

export {
    // GET-requests
    getCoursesLeipzigName,
    getModulesByCourse,
    getModulesNameCode,
    getModulesNameCodeByCourse,
    // POST-requests
    postCourseLeipzig,
    postModuleLeipzig,
    postJsonConfig,
    // PUT-requests
    putCourseLeipzigEdit,
    putUpdateCourseLeipzig,
    putUpdateModuleLeipzig,
    // DELETE-requests
    deleteCourseLeipzig,
    deleteModuleLeipzig,
}
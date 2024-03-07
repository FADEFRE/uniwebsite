import httpClient from "@/requests/httpClient";
import { consoleDebug } from "@/requests/consoleDebug";

let axiosColor = "color:deepskyblue";

/*
GET-Request to '/courses-leipzig' endpoint
return list of all course names
removes non active courses
parameters:
    none
 */
function getCoursesLeipzigName() {
    consoleDebug("%c" + "getCoursesLeipzigName ()", axiosColor);

    return httpClient
        .get("/api/courses-leipzig")
        .then((response) => {
            return response.data.filter(course => course.isActive).map((obj) => obj.name);
        })
        .catch((_) => {
        });
}

/*
GET-Request to '/courses-leipzig' endpoint
returns a list containing all modules related to a specific course

parameters:
    course - String, course name
 */
function getModulesByCourse(course) {
    consoleDebug("%c" + "getModulesByCourse (" + course + ")", axiosColor);

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

function getModulesNameCode() {
    consoleDebug("%c" + "getModulesNameCode", axiosColor);

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
GET-Request to '/courses-leipzig' endpoint
returns a list containing all modules related to a specific course
{name: module name, code: module code }

parameters:
    course - String, course name
 */
function getModulesNameCodeByCourse(course) {
    consoleDebug("%c" + "getModulesByCourse (" + course + ")", axiosColor);

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
    name of course
 */
function postCourseLeipzig(courseName) {
    consoleDebug(
        "%c" + "create course leipzig (name: " + courseName + ")",
        axiosColor
    );

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
    moduleName
    moduleCode
 */
function postModuleLeipzig(moduleName, moduleCode) {
    consoleDebug(
        "%c" + "create module leipzig (name: " + moduleName + ")",
        axiosColor
    );

    const formData = new FormData();
    formData.append(`name`, moduleName);
    formData.append(`code`, moduleCode);

    return httpClient
        .post("/api/modules-leipzig", formData)
        .then((response) => response.data)
        .catch((error) => Promise.reject(error));
}

function postJsonConfig(configFile) {
    consoleDebug("%c" + "postJsonConfig (configFile: " + configFile + ")", axiosColor)

    const formData = new FormData()
    formData.append("jsonFile", configFile)

    return httpClient.post("/file/json/courses/upload", formData)
        .then(response => response.data)
        .catch(_ => {
        })
}

function putCourseLeipzigEdit(coursename, moduleList) {
    consoleDebug("%c" + "putCourseLeipzigEdit", axiosColor);

    const formData = new FormData();

    moduleList.forEach((moduleLeipzig, moduleIndex) => {
        formData.append(`modulesLeipzig[${moduleIndex}].name`, moduleLeipzig.name);
        formData.append(`modulesLeipzig[${moduleIndex}].code`, moduleLeipzig.code);
    });

    return httpClient
        .put(`/api/courses-leipzig/${coursename}/edit`, formData)
        .then((response) => response.data)
        .catch((_) => {
        });
}

function putUpdateCourseLeipzig(courseName, newCourseName) {
    consoleDebug("%c" + "putUpdateCourseLeipzig", axiosColor);

    const formData = new FormData();
    formData.append("courseName", newCourseName);

    return httpClient
        .put(`/api/courses-leipzig/${courseName}`, formData)
        .then((response) => response.data)
}

function putUpdateModuleLeipzig(moduleName, newModuleName, newModuleCode) {
    consoleDebug("%c" + "putUpdateModuleLeipzig (moduleName: " + moduleName + ", newModuleName: " + newModuleName + ", newModuleCode: " + newModuleCode + ")", axiosColor)

    const formData = new FormData()

    formData.append('name', newModuleName)
    formData.append('code', newModuleCode)

    return httpClient.put(`/api/modules-leipzig/${moduleName}`, formData)
        .then(response => response.data)
}

function deleteCourseLeipzig(coursename) {
    consoleDebug("%c" + "deleteCourseLeipzig", axiosColor);

    return httpClient
        .delete(`/api/courses-leipzig/${coursename}`)
        .then((response) => response.data)
        .catch((_) => {
        });
}

function deleteModuleLeipzig(modulename) {
    consoleDebug("%c" + "deleteModuleLeipzig", axiosColor);

    return httpClient
        .delete(`/api/modules-leipzig/${modulename}`)
        .then((response) => response.data)
        .catch((_) => {
        });
}

export {
    getCoursesLeipzigName,
    getModulesByCourse,
    getModulesNameCode,
    getModulesNameCodeByCourse,

    postCourseLeipzig,
    postModuleLeipzig,
    postJsonConfig,

    putCourseLeipzigEdit,
    putUpdateCourseLeipzig,
    putUpdateModuleLeipzig,

    deleteCourseLeipzig,
    deleteModuleLeipzig,
}
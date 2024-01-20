import { url } from "@/scripts/url-config.js"
import axios from "axios";

/*
GET-Request to '/courses-leipzig' endpoint
return list of all course names

parameters:
    none
 */
function getCoursesLeipzig () {
    return axios.get(url + '/api/courses-leipzig')
        .then(response => {
            const courses = response.data.map(obj => obj.name)
            console.log(courses)
            return courses
        })
}

/*
GET-Request to '/courses-leipzig' endpoint
returns a list containing all modules related to a specific course

parameters:
    course - String, course name
 */
function getModulesByCourse (course) {
    return axios.get(url + '/api/courses-leipzig')
        .then(response => {
            const courseObject = response.data.find(obj => obj.name === course)
            return courseObject.modulesLeipzigCourse
                .filter(m => m.isActive)
                .map(obj => obj.name)
                .sort()
        })
}

/*
GET-Request to '/applications' endpoint
returns list of applications (only data needed for overview)

parameters:
    none
 */
function getApplications () {
    return axios.get(url + '/api/applications')
        .then(response => response.data)
}

/*
GET-Request to '/applications/{id}' endpoint
returns data of the related application

parameters:
    id - Number, application id
 */
function getApplicationById (id) {
    return axios.get(url + '/api/applications/' + id)
        .then(response => {
            return response.data
        })
}

/*
GET-Request to '/applications/student/{id} endpoint
return application data without study office and chairman data

parameters:
    id - Number, application id
 */
function getApplicationByIdForStatus (id) {
    return axios.get(url + '/api/applications/student/' + id)
        .then(response => {
            return response.data
        })
}

/*
GET-Request to '/applications/{id}/related' endpoint
returns related module connections

parameters:
    moduleConnectionId - Number, module connection id
 */
function getRelatedModuleConnections (moduleConnectionId) {
    return axios.get(url + '/api/modules-connection/' + moduleConnectionId + '/related')
        .then(response => {
            return response.data
        })
}

/*
POST-Request to '/applications' endpoint
posts a new application

parameters:
    course - String, must match a backend option
    applicationObjects - array of objects, each containing String moduleName, String university, Number CreditPoints, ...
    ... String pointSystem, File descriptionFile, String comment, array of Strings selectedInternalModules
 */
function postApplication (course, applicationObjects) {

    const formData = new FormData()
    formData.append(`courseLeipzig`, course)

    applicationObjects.forEach(
        (connection, connectionIndex) => {
            connection.externalModules.forEach(
                (externalModule, externalModuleIndex) => {
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].name`, externalModule.name)
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].university`, externalModule.university)
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].points`, externalModule.points)
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].pointSystem`, externalModule.pointSystem)
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].description`, externalModule.selectedFile)
                }
            )
            connection.internalModules.forEach(
                (moduleName, moduleIndex) => {
                    formData.append(`modulesConnections[${connectionIndex}].modulesLeipzig[${moduleIndex}]`, moduleName)
                }
            )
            formData.append(`modulesConnections[${connectionIndex}].commentApplicant`, connection.commentApplicant)
        }
    )

    console.log('post request to /applications')
    console.log([...formData])

    return axios.post(url + '/api/applications', formData)
        .then(response => {
            console.log('post application successful, data returned: ' + response.data)
            return response.data
        })
}

/*
PUT-Request to '/applications/{id}' endpoints
updates an existing application (including study-office and chairman data)

parameters:
    userRole - may be 'stud_office' or 'pav'
    id - Number, application id
    courseLeipzig - String, application course name
    connectionObjects - array of objects, each containing Number id, Array externalModules, Array internalModules, ...
    ..., optional String commentStudyOffice, optional String decisionSuggestion, ...
    ..., optional String commentDecision, optional String decisionFinal
 */
function putApplication (userRole, id, courseLeipzig, connectionObjects) {

    const formData = new FormData()
    formData.append('userRole', userRole)
    formData.append('courseLeipzig', courseLeipzig)

    connectionObjects.forEach(
        (connection, connectionIndex) => {
            formData.append(`modulesConnections[${connectionIndex}].id`, connection.id)
            if (connection.commentStudyOffice) {
                formData.append(`modulesConnections[${connectionIndex}].commentStudyOffice`, connection.commentStudyOffice)
            }
            if (connection.decisionSuggestion) {
                formData.append(`modulesConnections[${connectionIndex}].decisionSuggestion`, connection.decisionSuggestion)
            }
            if (connection.commentDecision) {
                formData.append(`modulesConnections[${connectionIndex}].commentDecision`, connection.commentDecision)
            }
            if (connection.decisionFinal) {
                formData.append(`modulesConnections[${connectionIndex}].decisionFinal`, connection.decisionFinal)
            }
            connection.externalModules.forEach(
                (externalModule, externalModuleIndex) => {
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].id`, externalModule.id)
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].name`, externalModule.name)
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].university`, externalModule.university)
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].points`, externalModule.points)
                    formData.append(`modulesConnections[${connectionIndex}].moduleApplications[${externalModuleIndex}].pointSystem`, externalModule.pointSystem)
                }
            )
            connection.internalModules.forEach(
                (moduleName, moduleIndex) => {
                    formData.append(`modulesConnections[${connectionIndex}].modulesLeipzig[${moduleIndex}].name`, moduleName)
                }
            )
        })

    console.log('calling applications put request')
    console.log([...formData])
    return axios.put(url + '/api/applications/' + id, formData)
        .then(response => console.log(response.data))
}

/*
GET-Request to /applications/{id}/update-status-allowed endpoint
returns true/false if updating application status is allowed

parameters:
    - id, Number application id
 */
function getUpdateStatusAllowed (id) {
    return axios.get(url + '/api/applications/' + id + '/update-status-allowed')
        .then(response => response.data)
}

/*
PUT-Request to /applications/{id}/update-status endpoint
updates application status if possible

parameters:
    - id, Number application id
 */
function updateStatus (id) {
    console.log('updating status for applications: ' + id)
    return axios.put(url + '/api/applications/' + id + '/update-status')
        .then(response => {
            if (response.data) {
                console.log('update successful')
            } else {
                console.log('update was not successful')
            }
            return response.data
        })
}

export {
    getCoursesLeipzig,
    getModulesByCourse,
    getApplications,
    getApplicationById,
    getApplicationByIdForStatus,
    getRelatedModuleConnections,
    postApplication,
    putApplication,
    getUpdateStatusAllowed,
    updateStatus,
}

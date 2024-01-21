import { url } from "@/scripts/url-config.js"
import axios from "axios";
import httpResource from "@/scripts/httpResource";

/*
GET-Request to '/courses-leipzig' endpoint
return list of all course names

parameters:
    none
 */
function getCoursesLeipzig () {
    return httpResource.get(url + '/api/courses-leipzig')
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
    return httpResource.get(url + '/api/courses-leipzig')
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
    return httpResource.get(url + '/api/applications')
        .then(response => response.data)
}

/*
GET-Request to '/applications/{id}' endpoint
returns data of the related application

parameters:
    id - Number, application id
 */
function getApplicationById (id) {
    return httpResource.get(url + '/api/applications/' + id)
        .then(response => {
            response.data['modulesConnections'].sort((a, b) => a.id - b.id)
            response.data['modulesConnections'].forEach(connection => {
                console.log(connection)
                connection['externalModules'].sort((a, b) => a.id - b.id)
            })
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
    return httpResource.get(url + '/api/applications/student/' + id)
        .then(response => {
            response.data['modulesConnections'].sort((a, b) => a.id - b.id)
            response.data['modulesConnections'].forEach(connection => {
                connection['externalModules'].sort((a, b) => a.id - b.id)
            })
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
    return httpResource.get(url + '/api/modules-connection/' + moduleConnectionId + '/related')
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
                    formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].name`, externalModule.name)
                    formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].university`, externalModule.university)
                    formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].points`, externalModule.points)
                    formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].pointSystem`, externalModule.pointSystem)
                    formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].description`, externalModule.selectedFile)
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

    return httpResource.post(url + '/api/applications', formData)
        .then(response => {
            console.log('post application successful, data returned: ' + response.data)
            return response.data
        })
}

// helper - creates basicFormData for PUT-Requests
// basicConnectionObjects has to be array containing objects with below used data
function createBasicFormData (courseLeipzig, basicConnectionObjects) {
    const formData = new FormData()
    formData.append('courseLeipzig', courseLeipzig)
    basicConnectionObjects.forEach((connection, connectionIndex) => {
        formData.append(`modulesConnections[${connectionIndex}].id`, connection.id)
        connection.externalModules.forEach((externalModule, externalModuleIndex) => {
            formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].id`, externalModule.id)
            formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].name`, externalModule.name)
            formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].university`, externalModule.university)
            formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].points`, externalModule.points)
            formData.append(`modulesConnections[${connectionIndex}].externalModules[${externalModuleIndex}].pointSystem`, externalModule.pointSystem)
        })
        connection.internalModules.forEach((moduleName, moduleIndex) => {
            formData.append(`modulesConnections[${connectionIndex}].modulesLeipzig[${moduleIndex}].name`, moduleName)
        })
    })
    return formData
}

function putApplicationStandard (applicationId, courseLeipzig, connectionObjects) {

    const formData = createBasicFormData(courseLeipzig, connectionObjects)

    connectionObjects.forEach((connection, connectionIndex) => {
        // todo add comment applicant
    })

    httpResource.put(url + '/api/applications/standard/' + applicationId, formData)
        .then(response => response.data)
}

function putApplicationStudyOffice (applicationId, courseLeipzig, connectionObjects) {

    console.log(connectionObjects)

    const formData = createBasicFormData(courseLeipzig, connectionObjects)

    connectionObjects.forEach((connection, connectionIndex) => {
        if (connection.formalRejectionData['formalRejection']) {
            formData.append(`modulesConnections[${connectionIndex}].formalRejection`, true)
            formData.append(`modulesConnections[${connectionIndex}].formalRejectionComment`, connection.formalRejectionData.comment)
        } else {
            formData.append(`modulesConnections[${connectionIndex}].formalRejection`, false)
            formData.append(`modulesConnections[${connectionIndex}].formalRejectionComment`, '')
            if (connection.studyOfficeDecisionData['decision']) {
                formData.append(`modulesConnections[${connectionIndex}].decisionSuggestion`, connection.studyOfficeDecisionData.decision)
                formData.append(`modulesConnections[${connectionIndex}].commentStudyOffice`, connection.studyOfficeDecisionData.comment)
            }
        }
    })

    console.log('put study office')
    console.log([...formData])
    return httpResource.put(url + '/api/applications/study-office/' + applicationId, formData)
        .then(response => response.data)
}

/*
GET-Request to /applications/{id}/update-status-allowed endpoint
returns true/false if updating application status is allowed

parameters:
    - id, Number application id
 */
function getUpdateStatusAllowed (id) {
    return httpResource.get(url + '/api/applications/' + id + '/update-status-allowed')
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
    return httpResource.put(url + '/api/applications/' + id + '/update-status')
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
    putApplicationStandard,
    putApplicationStudyOffice,
    getUpdateStatusAllowed,
    updateStatus,
}

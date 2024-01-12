import { url } from "@/scripts/url-config.js"
import axios from "axios";

/*
GET-Request to '/courses-leipzig' endpoint
return list of all course names

parameters:
    none
 */
function getCoursesLeipzig () {
    return axios.get(url + '/courses-leipzig')
        .then(response => {
            const courses = response.data.map(obj => obj.name)
            console.log(courses)
            return courses
        })
}

/*
GET-Request to '/courses-leipzig' endpoint
returns todo

parameters:
    none
 */
function getCourseData () {
    return axios.get(url + '/courses-leipzig')
        .then(response => {
            console.log(response.data)
            return response.data
        })
}

/*
GET-Request to '/courses-leipzig' endpoint
returns a list containing all modules related to a specific course

parameters:
    course - String, course name
 */
function getModulesByCourse (course) {
    return axios.get(url + '/courses-leipzig')
        .then(response => {
            const courseObject = response.data.find(obj => obj.name === course)
            return courseObject.modulesLeipzigCourse.map(obj => obj.name)
        })
}

/*
GET-Request to '/applications/{id}' endpoint
returns data of the related application

parameters:
    id - Number, application id
 */
function getApplicationById (id) {
    return axios.get(url + '/applications/' + id)
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
    return axios.get(url + '/applications/student/' + id)
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
    return axios.get(url + '/modules-connection/' + moduleConnectionId + '/related')
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

    return axios.post(url + '/applications', formData)
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
    return axios.put(url + '/applications/' + id, formData)
        .then(response => console.log(response.data))
}

function putStudyOffice (id, applicationObjects) {
    const formData = new FormData()
    formData.append('userRole', 'study_office')  // todo
    applicationObjects.forEach(
        (object, index) => {
            // application data
            formData.append(`moduleBlockUpdateDTOList[${index}].moduleName`, object.moduleName)
            formData.append(`moduleBlockUpdateDTOList[${index}].university`, object.university)
            formData.append(`moduleBlockUpdateDTOList[${index}].points`, object.creditPoints)
            formData.append(`moduleBlockUpdateDTOList[${index}].pointSystem`, object.pointSystem)
            formData.append(`moduleBlockUpdateDTOList[${index}].asExamCertificate`, object.asExamCertificate)
            object.selectedInternalModules.forEach(
                (moduleName, moduleIndex) => {
                    formData.append(`moduleBlockUpdateDTOList[${index}].moduleNamesLeipzig[${moduleIndex}]`, moduleName)
                }
            )
            // study office data
            formData.append(`moduleBlockUpdateDTOList[${index}].decisionSuggestion`, object.decisionSuggestion)
            formData.append(`moduleBlockUpdateDTOList[${index}].commentStudyOffice`, object.commentStudyOffice)
        }
    )
    console.log([...formData])
    return axios.put(url + '/applications/' + id, formData)
        .then(response => console.log(response.data))
}

function putChairman (id, applicationObjects) {
    const formData = new FormData()
    formData.append('userRole', 'pav')  // todo
    applicationObjects.forEach(
        (object, index) => {
            // application data
            formData.append(`moduleBlockUpdateDTOList[${index}].moduleName`, object.moduleName)
            formData.append(`moduleBlockUpdateDTOList[${index}].university`, object.university)
            formData.append(`moduleBlockUpdateDTOList[${index}].points`, object.creditPoints)
            formData.append(`moduleBlockUpdateDTOList[${index}].pointSystem`, object.pointSystem)
            formData.append(`moduleBlockUpdateDTOList[${index}].asExamCertificate`, object.asExamCertificate)
            object.selectedInternalModules.forEach(
                (moduleName, moduleIndex) => {
                    formData.append(`moduleBlockUpdateDTOList[${index}].moduleNamesLeipzig[${moduleIndex}]`, moduleName)
                }
            )
            // chairman data
            formData.append(`moduleBlockUpdateDTOList[${index}].decisionFinal`, object.decisionFinal)
            formData.append(`moduleBlockUpdateDTOList[${index}].commentDecision`, object.commentDecision)
        }
    )
    console.log([...formData])
    return axios.put(url + '/applications/' + id, formData)
        .then(response => console.log(response.data))
}

export {
    getCoursesLeipzig,
    getCourseData,
    getModulesByCourse,
    getApplicationById,
    getApplicationByIdForStatus,
    getRelatedModuleConnections,
    postApplication,
    putApplication,
    putStudyOffice,
    putChairman
}

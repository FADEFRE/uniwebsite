import { url } from "@/scripts/url-config.js"
import axios from "axios";

/*
GET-Request to '/courses-leipzig' endpoint
return a list containing all modules related to a specific course

parameters:
    course - String, course name
on error:
    returns 'error' instead of data
 */
function getModulesByCourse (course) {
    return axios.get(url + '/courses-leipzig')
        .then(response => {
            const courseObject = response.data.find(obj => obj.name === course)
            return courseObject.modulesLeipzigCourse.map(obj => obj.moduleName)
        })
        .catch(error => {
            console.log(error)
            return 'error'
        })
}

/*
GET-Request to '/applications/{id}' endpoint
returns data of the related application

parameters:
    id - Number, application id
on error:
    returns 'error' instead of data
 */
function getApplicationById (id) {
    return axios.get(url + '/applications/' + id)
        .then(response => {
            return response.data
        })
        .catch(error => {
            console.log(error)
            return 'error'
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
        (object, index) => {
            formData.append(`moduleBlockCreateDTOList[${index}].moduleName`, object.moduleName)
            formData.append(`moduleBlockCreateDTOList[${index}].university`, object.university)
            formData.append(`moduleBlockCreateDTOList[${index}].points`, object.creditPoints)
            formData.append(`moduleBlockCreateDTOList[${index}].pointSystem`, object.pointSystem)
            formData.append(`moduleBlockCreateDTOList[${index}].description`, object.descriptionFile)
            object.selectedInternalModules.forEach(
                (moduleName, moduleIndex) => {
                    formData.append(`moduleBlockCreateDTOList[${index}].moduleNamesLeipzig[${moduleIndex}]`, moduleName)
                }
            )
            formData.append(`moduleBlockCreateDTOList[${index}].commentApplicant`, object.comment)
        }
    )
    return axios.post(url + '/applications', formData)
        .then(response => response.data)
    // todo error catching
}

export { getModulesByCourse, getApplicationById, postApplication }

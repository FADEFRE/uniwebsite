import { url } from "@/scripts/url-config.js"
import axios from "axios";

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
    formData.append(`courseLeipzig`, course.value)
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

export { postApplication }

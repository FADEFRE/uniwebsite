import httpClient from "@/requests/httpClient";
import { consoleDebug } from "@/requests/consoleDebug";

let axiosColor = "color:deepskyblue";

function getAllUsers() {
    consoleDebug(axiosColor, "getAllUsers ()");

    return httpClient.get("/api/user/all")
        .then(response => response.data)
        .catch(_ => {
        })
}

function getUserMe() {
    consoleDebug(axiosColor, "getUserMe ()")

    return httpClient.get("/api/user/me")
        .then(response => response.data)
        .catch(() => {
        })
}

function getUserMeId() {
    consoleDebug(axiosColor, "getUserMeId ()")

    return httpClient.get("/api/user/me/id")
        .then(response => response.data)
        .catch(() => {
        })
}

function getUserMeName() {
    consoleDebug(axiosColor, "getUserMeName ()")

    return httpClient.get("/api/user/me/name")
        .then(response => response.data)
        .catch(() => {
        })
}

function postNewUser(username, password, passwordConfirm, role) {
    consoleDebug(axiosColor, "create user (username: " + username + ", role: " + role + ")");

    const formData = new FormData()

    formData.append('username', username)
    formData.append('password', password)
    formData.append('passwordConfirm', passwordConfirm)
    formData.append('role', role)

    return httpClient.post("/api/user/register", formData)
        .then(response => response.data)
        .catch(error => Promise.reject(error))
}

function putUserUsername(id, username) {
    consoleDebug(axiosColor, "getUserUsername (id: " + id + ", username: " + username + ")")

    const formData = new FormData()

    formData.append('id', id)
    formData.append('username', username)

    return httpClient.put('/api/user/change/username', formData)
        .then(response => response.data)
        .catch(error => Promise.reject(error))
}

function putUserPassword(id, password, passwordConfirm) {
    consoleDebug(axiosColor, "putUserPassword (id: " + id + ", password: [hidden]" + ", passwordConfirm: [hidden]" + ")")

    const formData = new FormData()

    formData.append('id', id)
    formData.append('password', password)
    formData.append('passwordConfirm', passwordConfirm)

    return httpClient.put('/api/user/change/password', formData)
        .then(response => response.data)
        .catch(_ => {
        })
}

function putUserRole(id, role) {
    consoleDebug(axiosColor, "getAllUsers (id: " + id + ", role: " + role + ")");

    const formData = new FormData()

    formData.append('id', id)
    formData.append('role', role)

    return httpClient.put('/api/user/change/role', formData)
        .then(response => response.data)
        .catch(_ => {
        })
}

function deleteUser(id) {
    consoleDebug(axiosColor, "delteUser (id: " + id + ")")

    const formData = new FormData()

    formData.append('id', id)

    return httpClient.delete('/api/user', formData)
        .then(response => response.data)
        .catch(_ => {
        })
}

export {
    getAllUsers,
    getUserMe,
    getUserMeId,
    getUserMeName,

    postNewUser,

    putUserUsername,
    putUserPassword,
    putUserRole,

    deleteUser,
}
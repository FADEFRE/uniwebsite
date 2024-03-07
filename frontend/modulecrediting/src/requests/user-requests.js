import httpClient from "@/requests/httpClient";
import { consoleDebug } from "@/requests/consoleDebug";

let axiosColor = "color:deepskyblue";

function getAllUsers() {
    consoleDebug("%c" + "getAllUsers ()", axiosColor);

    return httpClient.get("/api/user/all")
        .then(response => response.data)
        .catch(_ => {
        })
}

function getUserMe() {
    consoleDebug("%c" + "getUserMe ()", axiosColor)

    return httpClient.get("/api/user/me")
        .then(response => response.data)
        .catch(() => {
        })
}

function getUserMeId() {
    consoleDebug("%c" + "getUserMeId ()", axiosColor)

    return httpClient.get("/api/user/me/id")
        .then(response => response.data)
        .catch(() => {
        })
}

function getUserMeName() {
    consoleDebug("%c" + "getUserMeName ()", axiosColor)

    return httpClient.get("/api/user/me/name")
        .then(response => response.data)
        .catch(() => {
        })
}

function postNewUser(username, password, passwordConfirm, role) {
    consoleDebug(
        "%c" + "create user (username: " + username + ", role: " + role + ")",
        axiosColor
    );

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
    consoleDebug("%c" + "getUserUsername (id: " + id + ", username: " + username + ")", axiosColor)

    const formData = new FormData()

    formData.append('id', id)
    formData.append('username', username)

    return httpClient.put('/api/user/change/username', formData)
        .then(response => response.data)
        .catch(error => Promise.reject(error))
}

function putUserPassword(id, password, passwordConfirm) {
    consoleDebug("%c" + "putUserPassword (id: " + id + ", password: [hidden]" + ", passwordConfirm: [hidden]" + ")", axiosColor)

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
    consoleDebug("%c" + "getAllUsers (id: " + id + ", role: " + role + ")", axiosColor);

    const formData = new FormData()

    formData.append('id', id)
    formData.append('role', role)

    return httpClient.put('/api/user/change/role', formData)
        .then(response => response.data)
        .catch(_ => {
        })
}

function deleteUser(id) {
    consoleDebug("%c" + "delteUser (id: " + id + ")", axiosColor)

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
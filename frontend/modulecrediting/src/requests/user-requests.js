import httpClient from "@/requests/httpClient";
import { consoleDebug } from "@/requests/consoleDebug";

let axiosColor = "color:deepskyblue";

/*
GET-request to '/api/user/all' endpoint
returns list of all users (username, userId, role)

parameters:
    none
 */
function getAllUsers() {
    consoleDebug(axiosColor, "getAllUsers ()");

    return httpClient.get("/api/user/all")
        .then(response => response.data)
        .catch(_ => {
        })
}

/*
GET-request to '/api/user/me' endpoint
returns logged in user (username, userId, role)
if not logged in returns user with all fields null

parameters:
    none
 */
function getUserMe() {
    consoleDebug(axiosColor, "getUserMe ()")

    return httpClient.get("/api/user/me")
        .then(response => response.data)
        .catch(() => {
        })
}

/*
GET-request to '/api/user/me/id' endpoint
returns logged in user (userId)
if not logged in returns user with userId null

parameters:
    none
 */
function getUserMeId() {
    consoleDebug(axiosColor, "getUserMeId ()")

    return httpClient.get("/api/user/me/id")
        .then(response => response.data)
        .catch(() => {
        })
}

/*
GET-request to '/api/user/me/name' endpoint
returns logged in user (username)
if not logged in returns user with username null

parameters:
    none
 */
function getUserMeName() {
    consoleDebug(axiosColor, "getUserMeName ()")

    return httpClient.get("/api/user/me/name")
        .then(response => response.data)
        .catch(() => {
        })
}

/*
POST-request to 'api/user/register' endpoint
creates new user

parameters:
    username - String, username
    password - String, password
    passwordConfirm - String, passwordConfirm
    role - String, must match existing role

throws:
    Conflict (409) if username already exists
 */
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

/*
PUT-request to '/api/user/change/username' endpoint
modifies username

parameters:
    id - Number, user id
    username - String, new username

throws:
    Conflict (409) if username already exists
 */
function putUserUsername(id, username) {
    consoleDebug(axiosColor, "getUserUsername (id: " + id + ", username: " + username + ")")

    const formData = new FormData()

    formData.append('id', id)
    formData.append('username', username)

    return httpClient.put('/api/user/change/username', formData)
        .then(response => response.data)
        .catch(error => Promise.reject(error))
}

/*
PUT-request to '/api/user/change/password' endpoint
modifies password

parameters:
    id - Number, user id
    password - String, new password
    passwordConfirm - String, confirm new password
 */
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

/*
PUT-request to '/api/user/change/role' endpoint
modifies role

parameters:
    id - Number, user id
    role - String, must match existing role
 */
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

/*
DELETE-request to '/api/user/{id}' endpoint
deletes user

parameters:
    id - Number, user id
 */
function deleteUser(id) {
    consoleDebug(axiosColor, "deleteUser (id: " + id + ")")

    return httpClient.delete('/api/user/' + id)
        .then(response => response.data)
        .catch(_ => {
        })
}

export {
    // GET-requests
    getAllUsers,
    getUserMe,
    getUserMeId,
    getUserMeName,
    // POST-requests
    postNewUser,
    // PUT-requests
    putUserUsername,
    putUserPassword,
    putUserRole,
    // DELETE-requests
    deleteUser,
}
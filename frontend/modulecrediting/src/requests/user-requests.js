import httpClient from "@/requests/httpClient";

let axiosColor = "color:deepskyblue";

function getAllUsers() {
    console.debug("%c" + "getAllUsers ()", axiosColor);

    return httpClient.get("/api/user/all")
        .then(response => response.data)
        .catch(_ => {
        })
}

function getUserMe() {
    console.debug("%c" + "getUserMe ()", axiosColor)

    return httpClient.get("/api/user/me")
        .then(response => response.data)
        .catch(() => {
        })
}

function getUserMeId() {
    console.debug("%c" + "getUserMeId ()", axiosColor)

    return httpClient.get("/api/user/me/id")
        .then(response => response.data)
        .catch(() => {
        })
}

function getUserMeName() {
    console.debug("%c" + "getUserMeName ()", axiosColor)

    return httpClient.get("/api/user/me/name")
        .then(response => response.data)
        .catch(() => {
        })
}

function postNewUser(username, password, passwordConfirm, role) {
    console.debug(
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
    console.debug("%c" + "getUserUsername (id: " + id + ", username: " + username + ")", axiosColor)

    const formData = new FormData()

    formData.append('id', id)
    formData.append('username', username)

    return httpClient.put('/api/user/change/username', formData)
        .then(response => response.data)
        .catch(error => Promise.reject(error))
}

function putUserPassword(id, password, passwordConfirm) {
    console.debug("%c" + "putUserPassword (id: " + id + ", password: [hidden]" + ", passwordConfirm: [hidden]" + ")", axiosColor)

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
    console.debug("%c" + "getAllUsers (id: " + id + ", role: " + role + ")", axiosColor);

    const formData = new FormData()

    formData.append('id', id)
    formData.append('role', role)

    return httpClient.put('/api/user/change/role', formData)
        .then(response => response.data)
        .catch(_ => {
        })
}

function deleteUser(id) {
    console.debug("%c" + "delteUser (id: " + id + ")", axiosColor)

    const formData = new FormData()

    formData.append('id', id)

    return httpClient.post('/api/user/delete', formData)
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
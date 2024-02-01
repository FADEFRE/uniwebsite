import axios from "axios";
import { url } from "./url-config";
import { performLogout } from '@/router/logout'
import router from "@/router";

const isHandlerEnabled = (config = {}) => {
    return config.hasOwnProperty("handlerEnabled") && !config.handlerEnabled
        ? false
        : true;
};

const instance = axios.create({
    baseURL: url,
    withCredentials: true,
    timeout: 10000
});

let requestColor = "color:orange"
let responseColor = "color:lightgreen"
let errorColor = "color:red"


const requestHandler = request => {
    if (isHandlerEnabled(request)) {
        //TODO remove debug logs
        if (request.data instanceof FormData) {
            console.log("%c" + request.method.toUpperCase() + "-Request: " + request.url, requestColor, "  Start of Data: ");
            let formData = new FormData;
            formData = request.data;
            for (const pair of formData.entries()) {
                console.log(pair[0], pair[1]);
            }
            console.log("%c" + "End of Data", requestColor);
        }
        else console.log("%c" + request.method.toUpperCase() + "-Request: " + request.url, requestColor, "  Data: " + request.data);
    }
    return request;
};

const errorHandler = error => {
    // TODO 401 route to login
    // TODO 403 route to forbidden error page
    // TODO 404 route to id does not exist error page
    // TODO else route to server error page
    if (isHandlerEnabled(error.config)) {
        console.log("%c" + "Error Interceptor", errorColor, error); //TODO remove debug log

        if (error.response) {
        if (error.response.status === 401) {
            console.debug("%c" + "LOGOUT", errorColor) //TODO remove debug log
            performLogout();
            router.push({ name: "login" });
        }
        if (error.response.status === 403) {
            console.debug("%c" + "FORBIDDEN", errorColor) //TODO remove debug log
                router.push({ name: "Forbidden" });

            }
        if (error.response.status === 404) {
                router.push({ name: "IdError" });
            } 

        //debug
        if (error.response.status === 402 ) {
            console.debug("%c" + "RestAuthenticationEntryPoint FEHLER", errorColor, error.response)
        }
        }
    }
    return Promise.reject({ ...error });
};

const successHandler = response => {
    if (isHandlerEnabled(response.config)) {
        console.log("%c" + "Response: " + response.status + " ", responseColor, response.request.responseURL, "  Data:", response.data); //TODO remove debug log
    }
    return response;
};

instance.interceptors.request.use(request => requestHandler(request));

instance.interceptors.response.use(
    response => successHandler(response),
    error => errorHandler(error)
);

export default instance;
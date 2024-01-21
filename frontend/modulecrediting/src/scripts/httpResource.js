import axios from "axios";
import { url } from "./url-config";
import { performLogout } from "@/scripts/utils";

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
        console.log("%c" + request.method.toUpperCase() + "-Request: " + request.url, requestColor);
        //console.log(request)
    }
    return request;
};

const errorHandler = error => {
    if (isHandlerEnabled(error.config)) {
        console.log("%c" + "Error Interceptor", errorColor, error); //TODO remove debug log

        if (error.response) {
        if (error.response.status === 401) {
            performLogout();
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
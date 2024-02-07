import axios from "axios";
import { url } from "./url-config";
import { performLogout } from "@/router/logout";
import router from "@/router";
import { parseApierror } from "@/scripts/utils";

const isHandlerEnabled = (config = {}) => {
  return config.hasOwnProperty("handlerEnabled") && !config.handlerEnabled
    ? false
    : true;
};

const instance = axios.create({
  baseURL: url,
  withCredentials: true,
  timeout: 10000,
});

let requestColor = "color:orange";
let responseColor = "color:lightgreen";
let errorColor = "color:red";

const requestHandler = (request) => {
  if (isHandlerEnabled(request)) {
    //TODO remove debug logs
    if (request.data instanceof FormData) {
      console.log(
        "%c" + request.method.toUpperCase() + "-Request: " + request.url,
        requestColor,
        "  Start of Data: "
      );
      let formData = new FormData();
      formData = request.data;
      for (const pair of formData.entries()) {
        console.log(pair[0], pair[1]);
      }
      console.log("%c" + "End of Data", requestColor);
    } else
      console.log(
        "%c" + request.method.toUpperCase() + "-Request: " + request.url,
        requestColor,
        "  Data: " + request.data
      );
  }
  return request;
};

const errorHandler = (error) => {  
  // TODO 400 bad request? -> wohin? -> to calling function to correctly display?
  // TODO 401 route to login
  // TODO 403 route to forbidden error page
  // TODO 404 route to id does not exist error page
  // TODO 503 
  // TODO else route to server error page
  if (isHandlerEnabled(error.config)) {
    console.log("%c" + "Error Interceptor", errorColor); //TODO remove debug log

    const apiError = parseApierror(error)

    if (apiError.statusCode === 503) {
      console.error(apiError)
      //TODO: ROUTE TO "SERVER NOT RESPONDING" ERROR VIEW tbd
      //router.push({ name: "" }) 
    }

    if (apiError.statusCode === 400) {
      console.error(apiError)
      //TODO: ROUTE TO "BAD_REQUEST" ERROR VIEW tbd
      //router.push({ name: "" }) 
    }

    if (apiError.statusCode === 401) {
      console.error(apiError)
      performLogout();
      router.push({ name: "login" });
    }

    if (apiError.statusCode === 403) {
      console.error(apiError)
      router.push({ name: "Forbidden" });
    }

    if (apiError.statusCode === 404) {
      console.error(apiError)
      router.push({ name: "IdError" }); //TODO catch multiple 404?
    }

    if (apiError.statusCode === 409) {
      console.error(apiError)
    }


    //debug
    if (error.response.status === 402) {
      console.debug("%c" + "Debug 402 catch", errorColor);
      console.error(apiError)
    }

  }
  return Promise.reject({ ...error });
};

const successHandler = (response) => {
  if (isHandlerEnabled(response.config)) {
    console.log(
      "%c" + "Response: " + response.status + " ",
      responseColor,
      response.request.responseURL,
      "  Data:",
      response.data
    ); //TODO remove debug log
  }
  return response;
};

instance.interceptors.request.use((request) => requestHandler(request));

instance.interceptors.response.use(
  (response) => successHandler(response),
  (error) => errorHandler(error)
);

export default instance;

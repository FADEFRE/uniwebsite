import axios from "axios";
import { url } from "@/config/url-config";
import { performLogout } from "@/router/logout";
import router from "@/router";
import { consoleDebug } from "@/requests/consoleDebug";

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


function create503() {
  return {
      status: "SERVICE_UNAVAILABLE",
      statusCode: 503,
      timestamp: new Date(),
      message: "Server is not responding.."
  };
}

function parseApierror(error) {
  consoleDebug(errorColor, "parseapierror \n" + error);
  try {
      if (error && error.hasOwnProperty("response") && error.response.hasOwnProperty("data")) {
          const apierror = error.response.data;
          return {
              status: error.code,
              statusCode: apierror.status,
              timestamp: apierror.timestamp,
              message: apierror.message
          };
      } else {
          return create503();
      }
  }
  catch (parseError) {
      return create503();
  }
}

const requestHandler = (request) => {
  if (isHandlerEnabled(request)) {

    if (request.data instanceof FormData) {
      consoleDebug(requestColor, request.method.toUpperCase() + "-Request: " + request.url, "  Start of Request-Data: " ); 
      let formData = request.data;
      for (const pair of formData.entries()) {
        consoleDebug(null, pair[0], pair[1]);
      }
      consoleDebug(requestColor, "End of " + request.url + " Request-Data");
    } 
    else {
      consoleDebug(requestColor, request.method.toUpperCase() + "-Request: " + request.url, "  Request-Data: " + request.data );
    }
  }
  return request;
};

const errorHandler = (error) => {  

  if (isHandlerEnabled(error.config)) {
    consoleDebug(errorColor, "Error Interceptor");

    const apiError = parseApierror(error)
    consoleDebug(errorColor, apiError, "error");
    const currentRouteFullPath = router.currentRoute.value.fullPath

    switch (apiError.statusCode) {

      case 401: // UNAUTHORIZED
        performLogout();
        router.push({ name: "login" });
        break;

      case 402: // PAYMENT_REQUIRED -> used for debug
        consoleDebug(errorColor, "Debug 402 catch");
        router.replace("/error" + currentRouteFullPath);
        break;

      case 403: // FORBIDDEN
        router.replace("/forbidden" + currentRouteFullPath);
        break;

      case 404: // NOT_FOUND
        router.replace("/not-found" + currentRouteFullPath);
        break;

      case 409: // CONFLICT
        // no routing, this error should be handled in components / views
        break;

      case 503: // SERVER_UNAVAILABLE
        router.replace("/server-unavailable" + currentRouteFullPath);
        break;

      default:
        router.replace("/error" + currentRouteFullPath)

    }

  }

  return Promise.reject({ ...error });

};

const successHandler = (response) => {
  if (isHandlerEnabled(response.config)) {

    if (response.data instanceof Object) {
      consoleDebug(responseColor, "Response: " + response.status + " ", " " + response.request.responseURL + "  Start of Request-Data: " );
      consoleDebug(null, response.data)
      consoleDebug(responseColor, "End of Request-Data");
    } 
    else {
      consoleDebug(responseColor, "Response: " + response.status + " ", " " + response.request.responseURL + "  Data: " + response.data);
    }
  }
  return response;
};

instance.interceptors.request.use((request) => requestHandler(request));

instance.interceptors.response.use(
  (response) => successHandler(response),
  (error) => errorHandler(error)
);

export default instance;

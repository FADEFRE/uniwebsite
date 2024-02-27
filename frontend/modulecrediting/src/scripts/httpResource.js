import axios from "axios";
import { url } from "@/scripts/url-config";
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

  if (isHandlerEnabled(error.config)) {
    console.log("%c" + "Error Interceptor", errorColor); //TODO remove debug log

    const apiError = parseApierror(error)
    console.error(apiError)

    const currentRouteFullPath = router.currentRoute.value.fullPath

    switch (apiError.statusCode) {

      case 401:
        performLogout();
        router.push({ name: "login" });
        break;

      case 402:
        console.debug("%c" + "Debug 402 catch", errorColor);
        router.replace("/error" + currentRouteFullPath);
        break;

      case 403:
        router.replace("/forbidden" + currentRouteFullPath);
        break;

      case 404:
        router.replace("/not-found" + currentRouteFullPath);
        break;

      case 409:
        // no routing, this error should be handled in components / views
        break;

      case 503:
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

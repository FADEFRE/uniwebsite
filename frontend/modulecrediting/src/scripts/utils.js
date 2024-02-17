import httpResource from "./httpResource";
import { performLogout } from '@/router/logout'

export function parseApierror(error) {
    //console.debug("parseapierror", error);
    try {
        if (error && error.hasOwnProperty("response") && error.response.hasOwnProperty("data")) {
            const apierror = error.response.data;
            return {
                status: error.code,
                statusCode: apierror.status,
                timestamp: apierror.timestamp,
                message: apierror.message
            };
        }
    } 
    catch (parseError) {
        return {
            status: "SERVICE_UNAVAILABLE",
            statusCode: 503,
            timestamp: new Date(),
            message: "Server is not responding.."
        };
    }
}

export async function refreshTokenInternal() {
    console.debug("refreshTokenInternal()");
    try {
        const response = await httpResource.post("/api/auth/refresh");
        if (response.status !== 200) performLogout();
    } 
    catch (error) { performLogout(); }
}

export async function refreshToken() {
    console.debug("refreshToken()");
    const response = await httpResource.post("/api/auth/refresh");
    return response.status;
}

export const intervalMilliSeconds = 600000; // 10 minutes
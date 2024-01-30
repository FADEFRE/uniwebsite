import httpResource from "./httpResource";
import { useUserStore } from "@/store/authStore";

export function parseApierror(error) {
    console.debug("parseapierror", error);
    try {
        if (error && error.hasOwnProperty("response") && error.response.hasOwnProperty("data")) {
            const apierror = error.response.data;
            return {
                status: apierror.status,
                statusCode: error.status,
                timestamp: apierror.timestamp,
                message: apierror.message
            };
        }
    } 
    catch (parseError) {
        return {
            status: "INTERNAL_SERVER_ERROR",
            statusCode: 500,
            timestamp: new Date(),
            message: "Server is not responding.."
        };
    }
}

export async function performLogout() {
    console.debug("performLogout()");
    const response = await httpResource.post("/api/auth/logout");
    console.log(response)
    const authUserStore = useUserStore();
    authUserStore.logout();
    const intervalName = authUserStore.getIntervalName;
    if (intervalName) clearInterval(intervalName);
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

export async function getAuthenticatedUser() {
    console.debug("getAuthenticatedUser()");
    const authUserStore = useUserStore();
    try {
        if (authUserStore.getCurrentUserId !== "-1" ) {
            const response = await httpResource.get("/api/user/me");
            if (response.status === 200 && response.data.userId !== null) {
                const currentUser = response.data;
                authUserStore.setCurrentUser(currentUser);
            } 
        }
        else { performLogout(); }
    } 
    catch (error) { 
        console.error(error)
        performLogout(); 
    }
}

export const intervalMilliSeconds = 1800000; // 30 minutes
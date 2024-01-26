import httpResource from "./httpResource";
import { useAuthStore } from '@/store/authStore';

export function parseApierror(error) {
    console.log("parseapierror", error);
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
    const response = await httpResource.post("/api/auth/logout");
    console.log(response)
    const authUserStore = useAuthStore();
    const intervalName = authUserStore.getIntervalName;
    if (intervalName) clearInterval(intervalName);
    authUserStore.logout();
}

export async function refreshTokenInternal() {
    try {
        const response = await httpResource.post("/api/auth/refresh");
        if (response.status !== 200) performLogout();
    } 
    catch (error) { performLogout(); }
}

export async function refreshToken() {
    const response = await httpResource.post("/api/auth/refresh");
    return response.status;
}

export async function getAuthenticatedUser() {
    try {
        const response = await httpResource.get("/api/user/me");
        if (response.data.username !== null) {
            const currentUser = response.data;
            const authUserStore = useAuthStore();
            authUserStore.setCurrentUser(currentUser);
            authUserStore.setIsAuthenticated(true);
            await refreshTokenInternal();
            const intervalName = setInterval(async () => { await refreshTokenInternal(); } , intervalMilliSeconds);
            authUserStore.setIntervalName(intervalName);
        } 
        else { performLogout(); }
    } 
    catch (error) { performLogout(); }
}

export const intervalMilliSeconds = 1800000; // 30 minutes
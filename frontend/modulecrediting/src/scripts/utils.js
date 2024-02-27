import httpResource from "@/scripts/httpResource";
import { performLogout } from '@/router/logout'

function create503() {
    return {
        status: "SERVICE_UNAVAILABLE",
        statusCode: 503,
        timestamp: new Date(),
        message: "Server is not responding.."
    };
}

export function parseApierror(error) {
    console.debug("parseapierror", error);
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

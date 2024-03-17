import router from "@/router";
import httpClient from "@/requests/httpClient";
import { performLogout } from "@/router/logout";
import { useUserStore } from "@/store/userStore";
import { getUserMeId } from "@/requests/user-requests";
import { consoleDebug } from "@/requests/consoleDebug";

async function refreshTokenInternal() {
    consoleDebug(null, "refreshTokenInternal()");
    try {
        const response = await httpClient.post("/api/auth/refresh");
        if (response.status !== 200) performLogout();
    } 
    catch (error) { performLogout(); }
}

export async function runInterval() {
    const authUserStore = useUserStore();
    if (authUserStore.getCurrentUser === true) {
        refreshTokenInternal();
    }
}

export async function login (login_username, login_password) {
    const loginRequest = {
        username: login_username,
        password: login_password
    };
    const authUserStore = useUserStore();

    try {
        const response = await httpClient.post("/api/auth/login", loginRequest);
        if (response.status === 200) {
            const userResponse = await getUserMeId();
            if (userResponse.userId !== null) {
                authUserStore.setCurrentUser(true);
                await refreshTokenInternal();
            }
            const response = await httpClient.get(`/api/user/role`)
            switch (response.data) {
                case "ROLE_STUDY":
                    const routeData = router.resolve({ name: 'studyOfficeSelection' })
                    window.open(routeData.href, '_top')
                    break;
                case "ROLE_CHAIR":
                    const routeData1 = router.resolve({ name: 'chairmanSelection' })
                    window.open(routeData1.href, '_top')
                    break;
                case "ROLE_ADMIN":
                    const routeData2 = router.resolve({ name: 'managementAdmin' })
                    window.open(routeData2.href, '_top')
                    break;
                default:
                    break;
            }
        }
    }
    catch (error) {
        if (response.status !== 200) {
            performLogout();
        }
    }
}

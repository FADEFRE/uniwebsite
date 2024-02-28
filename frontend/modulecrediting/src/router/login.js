import router from "@/router";
import httpResource from "@/scripts/httpResource";
import { performLogout } from "@/router/logout";
import { useUserStore } from "@/store/userStore";


const intervalMilliSeconds = 600000; // 10 minutes


async function refreshTokenInternal() {
    console.debug("refreshTokenInternal()");
    try {
        const response = await httpResource.post("/api/auth/refresh");
        if (response.status !== 200) performLogout();
    } 
    catch (error) { performLogout(); }
}


export async function login (login_username, login_password) {
    const loginRequest = {
        username: login_username,
        password: login_password
    };
    const authUserStore = useUserStore();

    try {
        const response = await httpResource.post("/api/auth/login", loginRequest);
        if (response.status === 200) {
            const userResponse = await httpResource.get("/api/user/me/id"); //TODO cahnge to axios request call
            if (userResponse.data.userId !== null) {
                authUserStore.setCurrentUser(true);
                await refreshTokenInternal();
                const intervalName = setInterval(async () => { await refreshTokenInternal(); } , intervalMilliSeconds);
                authUserStore.setIntervalName(intervalName);
            }
            const response = await httpResource.get(`/api/user/role`)
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

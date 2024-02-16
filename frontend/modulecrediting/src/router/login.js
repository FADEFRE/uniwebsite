import router from "@/router";
import httpResource from "@/scripts/httpResource";
import { parseApierror, refreshTokenInternal, intervalMilliSeconds} from "@/scripts/utils";
import { performLogout } from '@/router/logout'
import { ref } from "vue";
import { useUserStore } from "@/store/userStore";


const displayErrorMessage = ref();
const errorMessage = ref();
const loginInProcess = ref();

async function login (login_username, login_password) {
    loginInProcess.value = true;
    const loginRequest = {
        username: login_username,
        password: login_password
    };
    const authUserStore = useUserStore();
    try {
        console.log(loginRequest)
        const response = await httpResource.post("/api/auth/login", loginRequest);
        console.log(response)
        if (response.status === 200) {
            const userResponse = await httpResource.get("/api/user/me/id"); //TODO cahnge to axios request call
            if (userResponse.data.userId !== null) {
                authUserStore.setCurrentUser(true);
                await refreshTokenInternal();
                const intervalName = setInterval(async () => { await refreshTokenInternal(); } , intervalMilliSeconds);
                authUserStore.setIntervalName(intervalName);
            }
            console.log("getRole");
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
                    const routeData2 = router.resolve({ name: 'accountAdmin' })
                    window.open(routeData2.href, '_top')
                    break;
                default:
                    break;
            }
        }
    }
    catch (error) {
        if (response.status !== 200) {
            const apierror = parseApierror(error);
            displayErrorMessage.value = true;
            errorMessage.value = apierror.message;
            performLogout();
        }
    }

    loginInProcess.value = false;

}

export { login } 
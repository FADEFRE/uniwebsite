import router from "../router";
import httpResource from "../http/httpResource";
import { parseApierror, performLogout, getAuthenticatedUser} from "../scripts/utils";
import { ref } from "vue";
import { useAuthStore } from '../store/authStore2';


const displayErrorMessage = ref();
const errorMessage = ref();
const loginInProcess = ref();

async function login (login_username, login_password) {
    loginInProcess.value = true;
    let canNavigate = false;
    const loginRequest = {
        username: login_username,
        password: login_password
    };
    try {
        console.log(loginRequest)
        const response = await httpResource.post("/auth/login", loginRequest);
        console.log(response)
        if (response.status === 200) {
            await getAuthenticatedUser();
            canNavigate = true;

            const authUserStore = useAuthStore();
            const id = authUserStore.getCurrentUserId;
            const response = await httpResource.get(`/user/${id}/role`)
            switch (response.data) {
                case "ROLE_STUDY":
                    const routeData = router.resolve({name: 'studyOfficeSelection'})
                    window.open(routeData.href, '_top')
                    break;
                case "ROLE_CHAIR":
                    const routeData1 = router.resolve({name: 'chairmanSelection'})
                    window.open(routeData1.href, '_top')
                    break;
                case "ROLE_ADMIN":
                    const routeData2 = router.resolve({name: 'studyOfficeSelection'}) //TODO change to admin when implemented
                    window.open(routeData2.href, '_top')
                    break;
                default:
                    break;
            }
        }
    } 
    catch (error) {
        performLogout();
        const apierror = parseApierror(error);
        displayErrorMessage.value = true;
        errorMessage.value = apierror.message;
    }
    loginInProcess.value = false;

    if (canNavigate) {
        router.replace("/");
    }
}

export { login } 
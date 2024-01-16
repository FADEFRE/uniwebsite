import { useRouter } from "vue-router";
import { ref } from "vue";

const user = ref('');
const styleInvalid = ref(false);
const password = ref('');

const router = useRouter();

const login = () => {
    if (user.value === 'studienbüro' && password.value) {
        const routeData = router.resolve({ name: 'studyOfficeSelection' });
        window.open(routeData.href, '_top');
    } else if (user.value === 'pav' && password.value) {
        const routeData = router.resolve({ name: 'chairmanSelection' });
        window.open(routeData.href, '_top');
    } else {
        styleInvalid.value = true;
    }
};

export { login }
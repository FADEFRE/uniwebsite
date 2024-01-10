import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { getAuthenticatedUser } from './util/utils';
import { createPinia } from 'pinia';
import { useAuthStore } from './store/authStore2'

// PrimeVue setup
import PrimeVue from 'primevue/config'
import Dropdown from 'primevue/dropdown'
import Panel from 'primevue/panel'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import FileUpload from "primevue/fileupload";
import SelectButton from "primevue/selectbutton";
import Checkbox from "primevue/checkbox";

import 'primevue/resources/themes/nova/theme.css'
import 'primeicons/primeicons.css'

const app = createApp(App)
        .use(router)
        .use(createPinia())

app.use(PrimeVue)
    .component('Dropdown', Dropdown)
    .component('Panel', Panel)
    .component('Button', Button)
    .component('InputText', InputText)
    .component('FileUpload', FileUpload)
    .component('SelectButton', SelectButton)
    .component('Checkbox', Checkbox)


async function init() {
    await getAuthenticatedUser();
    const authUserStore = useAuthStore();
    router.beforeEach(
        async (to, from, next) => {
            if (to.path !== "/login" && !authUserStore.getIsAuthenticated) {
                try {
                    const statusCode = await refreshToken();
                    if (statusCode !== 200) next("/login");
                    else next();
                } 
                catch (error) { next("/login"); }
            } 
            else { next(); }
        }
    );

    app.mount('#app')
}
init();

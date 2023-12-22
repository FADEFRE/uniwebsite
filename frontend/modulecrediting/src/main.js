import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)
    .use(router)

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

app.use(PrimeVue)
    .component('Dropdown', Dropdown)
    .component('Panel', Panel)
    .component('Button', Button)
    .component('InputText', InputText)
    .component('FileUpload', FileUpload)
    .component('SelectButton', SelectButton)
    .component('Checkbox', Checkbox)

// app mounting
app.mount('#app')


//ab hier wäre der code fürs login, aber halt mit bootstrap :/ 

import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import { getAuthenticatedUser } from "./util/utils";
import BootstrapVue from "bootstrap-vue";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";

Vue.use(BootstrapVue); //hier vermutlich einfach primevue
Vue.config.productionTip = false;

async function init() {
    await getAuthenticatedUser();
    router.beforeEach(async (to, from, next) => {
        if (to.path !== "/login" && !store.getters.getIsAuthenticated) {
        try {
            const statusCode = await refreshToken();
            if (statusCode !== 200) next("/login");
            else next();
        } catch (error) {
            next("/login");
        }
        } else {
        next();
        }
    });

    new Vue({
        router,
        store,
        render: h => h(App)
    }).$mount("#app");
}

init();
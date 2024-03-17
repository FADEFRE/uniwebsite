import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedState from 'pinia-plugin-persistedstate'
import App from '@/App.vue'
import i18n from '@/i18n'
import router from '@/router'

const pinia = createPinia();
pinia.use(piniaPluginPersistedState)

const app = createApp(App)
    .use(pinia)
    .use(router)

// PrimeVue setup
import PrimeVue from 'primevue/config'
import Dropdown from 'primevue/dropdown'
import Panel from 'primevue/panel'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import SelectButton from 'primevue/selectbutton'
import Dialog from 'primevue/dialog'

import 'primevue/resources/themes/nova/theme.css'
import 'primeicons/primeicons.css'

app.use(i18n)
app.use(PrimeVue)
    .component('Dropdown', Dropdown)
    .component('Panel', Panel)
    .component('Button', Button)
    .component('InputText', InputText)
    .component('SelectButton', SelectButton)
    .component('Dialog', Dialog)

app.mount('#app')
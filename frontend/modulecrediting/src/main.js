import { createApp } from 'vue'
import { createI18n } from 'vue-i18n'
import { createPinia } from 'pinia'
import EN from './locale/en.json'
import DE from './locale/de.json'
import App from './App.vue'
import router from './router'

const app = createApp(App)
    .use(createPinia())
    .use(router)
    

// PrimeVue setup
import PrimeVue from 'primevue/config'
import Dropdown from 'primevue/dropdown'
import Panel from 'primevue/panel'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import SelectButton from "primevue/selectbutton";

import 'primevue/resources/themes/nova/theme.css'
import 'primeicons/primeicons.css'

const i18n = createI18n({
    locale: document.cookie.split('=')[1],
    messages: {
        EN: EN,
        DE: DE,
    }

    })

app.use(i18n)
app.use(PrimeVue)
    .component('Dropdown', Dropdown)
    .component('Panel', Panel)
    .component('Button', Button)
    .component('InputText', InputText)
    .component('SelectButton', SelectButton)

app.mount('#app')
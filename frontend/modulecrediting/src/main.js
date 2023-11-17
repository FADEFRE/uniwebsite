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
import Divider from 'primevue/divider'
import InputText from 'primevue/inputtext'
import FileUpload from "primevue/fileupload";

import 'primevue/resources/themes/nova/theme.css'
import 'primeicons/primeicons.css'

app.use(PrimeVue)
    .component('Dropdown', Dropdown)
    .component('Panel', Panel)
    .component('Button', Button)
    .component('Divider', Divider)
    .component('InputText', InputText)
    .component('FileUpload', FileUpload)

// app mounting
app.mount('#app')

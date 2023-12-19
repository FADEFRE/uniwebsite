import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)
<<<<<<< HEAD

app.use(router)
=======
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
>>>>>>> develop

// app mounting
app.mount('#app')

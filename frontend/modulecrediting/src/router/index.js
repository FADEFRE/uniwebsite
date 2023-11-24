import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'
import StatusSearchView from "@/views/StatusSearchView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: SubmitApplicationView
    },
    {
      path: '/status',
      name: 'status',
      component: StatusSearchView
    }
  ]
})

export default router

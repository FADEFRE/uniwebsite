import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'
import StatusDetailView from "@/views/StatusDetailView.vue";
import LoginView from "@/views/LoginView.vue";
import HomepageView from "@/views/HomepageView.vue"
import ApplicationConfirmationView from "@/views/ApplicationConfirmationView.vue"
import AdministrativeDetailView from "@/views/AdministrativeDetailView.vue";
import AdministrativeSelectionView from "@/views/AdministrativeSelectionView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomepageView,
      meta: { type: 'standard' }
    },
    {
      path: '/antrag',
      name: 'submitApplication',
      component: SubmitApplicationView,
      meta: { type: 'standard' }
    },
    {
      path: '/confirmation/:id',
      name: 'confirmation',
      component: ApplicationConfirmationView,
      meta: { type: 'standard' }
    },
    {
      path: '/status/:id',
      name: 'statusDetail',
      component: StatusDetailView,
      meta: { type: 'standard' }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { type: 'standard' }
    },
    {
      path: '/studienbuero',
      name: 'studyOfficeSelection',
      component: AdministrativeSelectionView,
      meta: { type: 'study-office', forward: 'studyOfficeDetail' }
    },
    {
      path: '/studienbuero/:id',
      name: 'studyOfficeDetail',
      component: AdministrativeDetailView,
      meta: { type: 'study-office' }
    },
    {
      path: '/pruefungsausschuss',
      name: 'chairmanSelection',
      component: AdministrativeSelectionView,
      meta: { type: 'chairman', forward: 'chairmanDetail' }
    },
    {
      path: '/pruefungsausschuss/:id',
      name: 'chairmanDetail',
      component: AdministrativeDetailView,
      meta: { type: 'chairman' }
    }
  ]
})

export default router

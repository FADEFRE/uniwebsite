import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'
import StatusSearchView from "@/views/StatusSearchView.vue";
import StatusDetailView from "@/views/StatusDetailView.vue";
import SelectionView from "@/views/SelectionView.vue";
import StudyOfficeDetailView from "@/views/StudyOfficeDetailView.vue";
import ChairmanDetailView from "@/views/ChairmanDetailView.vue";
import LoginView from "@/views/LoginView.vue";
import HomepageView from "@/views/HomepageView.vue"
import ApplicationConfirmationView from "@/views/ApplicationConfirmationView.vue"
import TestView from "@/views/TestView.vue";
import AdministrativeDetailView from "@/views/AdministrativeDetailView.vue";

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
      component: SelectionView,
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
      component: SelectionView,
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

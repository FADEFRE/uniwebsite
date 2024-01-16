import { createRouter, createWebHistory } from 'vue-router'
import HomepageView from "@/views/HomepageView.vue"

import TestView from "@/views/TestView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomepageView,
      meta: { authType: 'standard' }
    },
    {
      path: '/antrag',
      name: 'submitApplication',
      component: () => import('../views/SubmitApplicationView.vue'),
      meta: { authType: 'standard' }
    },
    {
      path: '/confirmation/:id',
      name: 'confirmation',
      component: () => import('../views/ApplicationConfirmationView.vue'),
      meta: { authType: 'standard' }
    },
    {
      path: '/status/:id',
      name: 'statusDetail',
      component: () => import('../views/StatusDetailView.vue'),
      meta: { authType: 'standard' }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { authType: 'standard' }
    },
    {
      path: '/studienbuero',
      name: 'studyOfficeSelection',
      component: () => import('../views/AdministrativeSelectionView.vue'),
      meta: { authType: 'study-office', forward: 'studyOfficeDetail' }
    },
    {
      path: '/studienbuero/:id',
      name: 'studyOfficeDetail',
      component: () => import('../views/AdministrativeDetailView.vue'),
      meta: { authType: 'study-office' }
    },
    {
      path: '/pruefungsausschuss',
      name: 'chairmanSelection',
      component: () => import('../views/AdministrativeSelectionView.vue'),
      meta: { authType: 'chairman', forward: 'chairmanDetail' }
    },
    {
      path: '/pruefungsausschuss/:id',
      name: 'chairmanDetail',
      component: () => import('../views/AdministrativeDetailView.vue'),
      meta: { authType: 'chairman' }
    }
  ]
})

export default router

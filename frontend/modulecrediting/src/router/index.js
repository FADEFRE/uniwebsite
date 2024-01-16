import { createRouter, createWebHistory } from 'vue-router'
import HomepageView from "@/views/HomepageView.vue"

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
      component: () => import('../views/SubmitApplicationView.vue'),
      meta: { type: 'standard' }
    },
    {
      path: '/confirmation/:id',
      name: 'confirmation',
      component: () => import('../views/ApplicationConfirmationView.vue'),
      meta: { type: 'standard' }
    },
    {
      path: '/status/:id',
      name: 'statusDetail',
      component: () => import('../views/StatusDetailView.vue'),
      meta: { type: 'standard' }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { type: 'standard' }
    },
    {
      path: '/studienbuero',
      name: 'studyOfficeSelection',
      component: () => import('../views/AdministrativeSelectionView.vue'),
      meta: { type: 'study-office', forward: 'studyOfficeDetail' }
    },
    {
      path: '/studienbuero/:id',
      name: 'studyOfficeDetail',
      component: () => import('../views/AdministrativeDetailView.vue'),
      meta: { type: 'study-office' }
    },
    {
      path: '/pruefungsausschuss',
      name: 'chairmanSelection',
      component: () => import('../views/AdministrativeSelectionView.vue'),
      meta: { type: 'chairman', forward: 'chairmanDetail' }
    },
    {
      path: '/pruefungsausschuss/:id',
      name: 'chairmanDetail',
      component: () => import('../views/AdministrativeDetailView.vue'),
      meta: { type: 'chairman' }
    }
  ]
})

export default router

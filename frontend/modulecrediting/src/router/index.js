import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'

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
      name: 'statusSearch',
      component: () => import('../views/StatusDetailView.vue')
    },
    {
      path: '/status/:id',
      name: 'statusDetail',
      component: () => import('../views/StatusDetailView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/study-office',
      name: 'studyOfficeSelection',
      component: () => import('../views/SelectionView.vue'),
      meta: {forward: 'studyOfficeDetail'}
    },
    {
      path: '/study-office/:id',
      name: 'studyOfficeDetail',
      component: () => import('../views/StudyOfficeDetailView.vue')
    },
    {
      path: '/chairman',
      name: 'chairmanSelection',
      component: () => import('../views/SelectionView.vue'),
      meta: {forward: 'chairmanDetail'}
    },
    {
      path: '/chairman/:id',
      name: 'chairmanDetail',
      component: () => import('../views/ChairmanDetailView.vue')
    }
  ]
})


export default router

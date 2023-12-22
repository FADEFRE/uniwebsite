import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'
import StatusSearchView from "@/views/StatusSearchView.vue";
import StatusDetailView from "@/views/StatusDetailView.vue";
import SelectionView from "@/views/SelectionView.vue";
import StudyOfficeDetailView from "@/views/StudyOfficeDetailView.vue";
import ChairmanDetailView from "@/views/ChairmanDetailView.vue";
import LoginView from "@/views/LoginView.vue";
import JWTloginView from "@/views/JWTloginView.vue"
import JWTstatusView from "@/views/JWTstatusView.vue"

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
      component: StatusSearchView
    },
    {
      path: '/status/:id',
      name: 'statusDetail',
      component: StatusDetailView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/study-office',
      name: 'studyOfficeSelection',
      component: SelectionView,
      meta: {forward: 'studyOfficeDetail'}
    },
    {
      path: '/study-office/:id',
      name: 'studyOfficeDetail',
      component: StudyOfficeDetailView
    },
    {
      path: '/chairman',
      name: 'chairmanSelection',
      component: SelectionView,
      meta: {forward: 'chairmanDetail'}
    },
    {
      path: '/chairman/:id',
      name: 'chairmanDetail',
      component: ChairmanDetailView
    },
    {
      path: '/jwtlogin',
      name: 'jwtlogin',
      component: JWTloginView
    },
    {
      path: '/jwtstatus',
      name: 'jwtstatus',
      component: JWTstatusView,
      meta: {requiresAuth: true, allowedUsers: ['studienbüro', 'pav']}
    }
  ]
})

router.beforeEach((to, from) => {
  if (to.meta['requiresAuth']) {

    // todo if logged in set username
    // todo else set username to undefined
    const username = 'studienbüro'

    if (!to.meta['allowedUsers'].includes(username)) {
      return {name: 'jwtlogin'}
    }

  }
})

export default router

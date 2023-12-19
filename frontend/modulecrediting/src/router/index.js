import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'
import StatusSearchView from "@/views/StatusSearchView.vue";
import StatusDetailView from "@/views/StatusDetailView.vue";
import SelectionView from "@/views/SelectionView.vue";
import StudyOfficeDetailView from "@/views/StudyOfficeDetailView.vue";
import ChairmanDetailView from "@/views/ChairmanDetailView.vue";
import LoginView from "@/views/LoginView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
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
    }
  ]
})

export default router

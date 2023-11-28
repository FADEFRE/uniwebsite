import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'
import StatusSearchView from "@/views/StatusSearchView.vue";
import StatusDetailView from "@/views/StatusDetailView.vue";
import StudyOfficeSelectionView from "@/views/StudyOfficeSelectionView.vue";

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
      path: '/study-office',
      name: 'studyOffice',
      component: StudyOfficeSelectionView
    }
  ]
})

export default router

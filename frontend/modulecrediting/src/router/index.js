import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'
import { getAuthenticatedUser } from '../scripts/utils';
import { useAuthStore } from '../store/authStore2';
import httpResource from "../scripts/httpResource";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: SubmitApplicationView,
      meta: { authType: 'standard' }
    },
    {
      path: '/status',
      name: 'statusSearch',
      component: () => import('../views/StatusSearchView.vue'),
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
      path: '/study-office',
      name: 'studyOfficeSelection',
      component: () => import('../views/SelectionView.vue'),
      meta: { authType: 'studyoffice', forward: 'studyOfficeDetail'}
    },
    {
      path: '/study-office/:id',
      name: 'studyOfficeDetail',
      component: () => import('../views/StudyOfficeDetailView.vue'),
      meta: { authType: 'studyoffice'}
    },
    {
      path: '/chairman',
      name: 'chairmanSelection',
      component: () => import('../views/SelectionView.vue'),
      meta: { authType: 'chairman', forward: 'chairmanDetail'}
    },
    {
      path: '/chairman/:id',
      name: 'chairmanDetail',
      component: () => import('../views/ChairmanDetailView.vue'),
      meta: { authType: 'chairman'}
    }
  ]
})

router.beforeEach(
  async (to) => {
    await getAuthenticatedUser();
    const authUserStore = useAuthStore();
    const id = authUserStore.getCurrentUserId;
    const response = await httpResource.get(`/api/user/${id}/role`)
    switch (to.meta.authType) {
      case "standard":
        return true;
      case "studyoffice":
        if (response.data === "ROLE_STUDY") { return true; }
        return { name: 'login' };
      case "chairman":
        if (response.data === "ROLE_CHAIR") { return true; }
        return { name: 'login' };
      case "admin":
        if (response.data === "ROLE_ADMIN") { return true; }
        return { name: 'login' };
      default:
        break;
    }

    console.log("default");
    return false;

  }
);

export default router

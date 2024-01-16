import { createRouter, createWebHistory } from 'vue-router'
import SubmitApplicationView from '../views/SubmitApplicationView.vue'
import { getAuthenticatedUser } from '../util/utils';
import { useAuthStore } from '../store/authStore2';
import httpResource from "../http/httpResource";
import HomepageView from "@/views/HomepageView.vue"

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
      component: () => import('../views/StatusDetailView.vue'),
      meta: { authType: 'standard' }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { authType: 'standard' }
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

router.beforeEach(
  async (to) => {
    await getAuthenticatedUser();
    const authUserStore = useAuthStore();
    const id = authUserStore.getCurrentUserId;
    const response = await httpResource.get(`/user/${id}/role`)
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

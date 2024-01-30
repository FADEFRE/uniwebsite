import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/store/authStore";
import { getAuthenticatedUser } from "@/scripts/utils";
import httpResource from "@/scripts/httpResource";
import HomepageView from "@/views/HomepageView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomepageView,
      meta: { authType: "standard" },
    },
    {
      path: "/antrag",
      name: "submitApplication",
      component: () => import("../views/SubmitApplicationView.vue"),
      meta: { authType: "standard" },
    },
    {
      path: "/confirmation/:id",
      name: "confirmation",
      component: () => import("../views/ApplicationConfirmationView.vue"),
      meta: { authType: "standard" },
    },
    {
      path: "/status/:id",
      name: "statusDetail",
      component: () => import("../views/StatusDetailView.vue"),
      meta: { authType: "standard" },
    },
    {
      path: "/login",
      name: "login",
      component: () => import("../views/LoginView.vue"),
      meta: { authType: "standard" },
    },
    {
      path: "/verwaltungsbereich",
      name: "management",
      component: () => import("../views/ManagementView.vue"),
      meta: { authType: "internal" },
    },
    {
      path: "/studienbuero",
      name: "studyOfficeSelection",
      component: () => import("../views/AdministrativeSelectionView.vue"),
      meta: { authType: "study-office", forward: "studyOfficeDetail" },
    },
    {
      path: "/studienbuero/:id",
      name: "studyOfficeDetail",
      component: () => import("../views/AdministrativeDetailView.vue"),
      meta: { authType: "study-office" },
    },
    {
      path: "/studienbuero/:id/:connection",
      name: "studyOfficeDetailHighlight",
      component: () => import("../views/AdministrativeDetailView.vue"),
      meta: { authType: "study-office" },
    },
    {
      path: "/pruefungsausschuss",
      name: "chairmanSelection",
      component: () => import("../views/AdministrativeSelectionView.vue"),
      meta: { authType: "chairman", forward: "chairmanDetail" },
    },
    {
      path: "/pruefungsausschuss/:id",
      name: "chairmanDetail",
      component: () => import("../views/AdministrativeDetailView.vue"),
      meta: { authType: "chairman" },
    },
    {
      path: "/pruefungsausschuss/:id/:connection",
      name: "chairmanDetailHighlight",
      component: () => import("../views/AdministrativeDetailView.vue"),
      meta: { authType: "chairman" },
    },
    {
      path: "/:pathMatch(.*)*",
      name: "notFound",
      component: () => import("../views/NotFoundView.vue"),
      meta: { authType: "standard" },
    },
    {
      path: "/:pathMatch(.*)*",
      name: "Forbidden",
      component: () => import("../views/ForbiddenView.vue"),
      meta: { authType: "standard" },
    },
    {
      path: "/:pathMatch(.*)*",
      name: "IdError",
      component: () => import("../views/IdErrorView.vue"),
      meta: { authType: "standard" },
    },
    {
      path: "/:pathMatch(.*)*",
      name: "Permission",
      component: () => import("../views/NoPermissionView.vue"),
      meta: { authType: "standard" },
    }
  ],
});

function changeRole(authRole) {
  const userStore = useUserStore();
  if (authRole === "ROLE_STUDY") {
    userStore.setCurrentRoleNav("study");
  } else if (authRole === "ROLE_CHAIR") {
    userStore.setCurrentRoleNav("chair");
  } else if (authRole === "ROLE_ADMIN") {
    userStore.setCurrentRoleNav("admin");
  } else {
    userStore.setCurrentRoleNav("user");
  }
  return true;
}

router.beforeEach(async (to, from) => {
  const userStore = useUserStore();
  const id = userStore.getCurrentUserId;
  if (from.name === "notFound" && to.name === "notFound") {
    return false;
  }
  if (to.meta.authType === "standard" && id === "-1") {
    userStore.setCurrentRoleNav("user");
    return true;
  }
  getAuthenticatedUser()
  console.log("getRole Router");
  const response = await httpResource.get(`/api/user/${id}/role`);
  switch (to.meta.authType) {
    case "standard":
      changeRole(response.data);
      return true;

    case "study-office":
      if (response.data === "ROLE_STUDY") {
        userStore.setCurrentRoleNav("study");
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };

    case "chairman":
      if (response.data === "ROLE_CHAIR") {
        userStore.setCurrentRoleNav("chair");
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };

    case "admin":
      if (response.data === "ROLE_ADMIN") {
        userStore.setCurrentRoleNav("admin");
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };

    case "internal":
      if (response.data === "ROLE_CHAIR" || response.data === "ROLE_STUDY") {
        changeRole(response.data);
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };

    case "internal-with-admin":
      if (response.data === "ROLE_ADMIN" || response.data === "ROLE_CHAIR" || response.data === "ROLE_STUDY") {
        changeRole(response.data);
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };
      
    default:
      break;
  }

  console.log("default");
  return false;
});

export default router;

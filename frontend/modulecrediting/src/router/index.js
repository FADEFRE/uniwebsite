import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/store/userStore";
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
      path: "/antrag/:id",
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
      path: "/verwaltung",
      name: "management",
      component: () => import("../views/ManagementView.vue"),
      meta: { authType: "internal" },
      children: [
        {
          path: "admin",
          name: "managementAdmin",
          component: () => import("../views/ManagementViewAdminChild.vue"),
          meta: { authType: "admin"}
        }
      ]
    },
    {
      path: "/account",
      name: "account",
      component: () => import("../views/AccountView.vue"),
      meta: { authType: "internal" },
      children: [
        {
          path: "admin",
          name: "accountAdmin",
          component: () => import("../views/AccountViewAdminChild.vue"),
          meta: { authType: "admin" }
        }
      ]
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
  const user = userStore.getCurrentUser;
  if (from.name === "notFound" && to.name === "notFound") {
    return false;
  }
  if (to.meta.authType === "standard" && user === false) {
    userStore.setCurrentRoleNav("user");
    return true;
  }
  if (to.meta.authType !== "standard" && user === false) {
    userStore.logout();
    return { name: "login" };
  }
  console.log("getRole Router");
  const responseRole = await httpResource.get(`/api/user/role`);
  switch (to.meta.authType) {
    case "standard":
      changeRole(responseRole.data);
      return true;

    case "study-office":
      if (responseRole.data === "ROLE_STUDY") {
        userStore.setCurrentRoleNav("study");
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };

    case "chairman":
      if (responseRole.data === "ROLE_CHAIR") {
        userStore.setCurrentRoleNav("chair");
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };

    case "admin":
      if (responseRole.data === "ROLE_ADMIN") {
        userStore.setCurrentRoleNav("admin");
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };

    case "internal":
      if (responseRole.data === "ROLE_CHAIR" || responseRole.data === "ROLE_STUDY") {
        changeRole(responseRole.data);
        return true;
      }
      return { name: "Permission" }; //TODO route to correct error page "permission not allowed"
      //return { name: "login" };

    case "internal-with-admin":
      if (responseRole.data === "ROLE_ADMIN" || responseRole.data === "ROLE_CHAIR" || responseRole.data === "ROLE_STUDY") {
        changeRole(responseRole.data);
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

import router from "@/router";
import httpClient from "@/requests/httpClient";
import { useUserStore } from "@/store/userStore";
import { consoleDebug } from "@/requests/consoleDebug";

async function logout() {
  await performLogout();
  const routeData = router.resolve({name: 'login'})
  window.open(routeData.href, '_top')
}

async function performLogout() {
  consoleDebug(null, "performLogout()");
  const response = await httpClient.post("/api/auth/logout");
  consoleDebug(null, "Logout response: " + response)
  const authUserStore = useUserStore();
  authUserStore.logout();
  const intervalName = authUserStore.getIntervalName;
  if (intervalName) clearInterval(intervalName);
}

export { logout, performLogout }

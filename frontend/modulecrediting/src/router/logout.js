import router from "@/router";

async function logout() {
  await performLogout();
  const routeData = router.resolve({name: 'login'})
  window.open(routeData.href, '_top')
}

export async function performLogout() {
  console.debug("performLogout()");
  const response = await httpResource.post("/api/auth/logout");
  console.log(response)
  const authUserStore = useUserStore();
  authUserStore.logout();
  const intervalName = authUserStore.getIntervalName;
  if (intervalName) clearInterval(intervalName);
}

export { logout, performLogout }
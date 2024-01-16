import router from "../router";
import httpResource from "../scripts/httpResource"
import { performLogout } from "../scripts/utils"

async function logout () {
  //TODO: re-remove comment when security is merged
  //const response = await httpResource.post("/api/auth/logout");
  //console.log(response)
  performLogout();
  const routeData = router.resolve({name: 'login'})
  window.open(routeData.href, '_top')
}

export { logout }
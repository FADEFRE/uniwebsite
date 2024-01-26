import router from "@/router";
import { performLogout } from "@/scripts/utils"

async function logout () {
  performLogout();
  const routeData = router.resolve({name: 'login'})
  window.open(routeData.href, '_top')
}

export { logout }
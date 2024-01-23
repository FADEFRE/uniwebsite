<script setup>
import { logout } from "@/router/logout";
import { useNavTypeStore} from "../store/navTypeStore"
import { computed } from "vue";

const navStore = useNavTypeStore();
const isNavType = computed(() => {
  if (navStore.getCurrentRoleNav === "user") { return "standard" }
  else if (navStore.getCurrentRoleNav === "study" || navStore.getCurrentRoleNav === "chair" || navStore.getCurrentRoleNav === "admin") {
    return "internal"
  }
});
const specificRole = computed(() => navStore.getCurrentRoleNav)
</script>

<template>
    <div v-if="isNavType === 'standard'" class="links-container">
      <router-link :to="{ name: 'home' }" class="router-link-container">
        {{ $t('home page') }}
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </router-link>
      <router-link :to="{ name: 'login' }" class="router-link-container">
        {{ $t('log-in internally') }}
        <img src="@/assets/icons/Login.svg" class="login-logout-icon">
      </router-link>
    </div>

    <div v-else-if="isNavType === 'internal'" class="links-container">
      <router-link to="" class="router-link-container">
        Verwaltung
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </router-link> <!-- todo replace with manage link -->

      <div v-if="specificRole === 'study'"> 
        <router-link :to="{ name: 'studyOfficeSelection' }" class="router-link-container">
          Übersicht
          <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
        </router-link>
      </div>
      <div v-else-if="specificRole === 'chair'">
        <router-link :to="{ name: 'chairmanSelection' }" class="router-link-container">
          Übersicht
          <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
        </router-link>
      </div>

      <Button @click="logout" class="router-link-container">
        Logout
        <img src="@/assets/icons/Login.svg" class="login-logout-icon">
      </Button>
    </div>

</template>

<style scoped lang="scss">
@import '../assets/variables.scss';


.links-container {
  display: flex;
  align-items: center;
  gap: 0.6rem 1.875rem;
  flex-wrap: wrap;
}


.router-link-container {
  display: flex;
  gap: 0.625rem;

  &:hover {
    background-color: $black;

    .arrow-icon {
      transform: translate(0.15rem) rotate(-90deg);
    }

    .login-logout-icon {
      transform: translate(0.15rem);
    }
  }
}


.arrow-icon {
  transform: rotate(-90deg);
  transition: 0.1s ease-in-out;
}
.login-logout-icon {
  transition: 0.1s ease-in-out;
}
</style>
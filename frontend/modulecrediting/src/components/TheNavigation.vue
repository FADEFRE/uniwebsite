<script setup>
import { logout } from "@/router/logout";
import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute()
const routeType = computed(() => route.meta['authType'])
</script>

<template>
  <div class="navigation-container">

    <div v-if="routeType === 'standard'" class="links-container">
      <router-link :to="{ name: 'home' }" class="router-link-container">
        {{ $t('home page') }}
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </router-link>
      <router-link :to="{ name: 'login' }" class="router-link-container">
        {{ $t('log-in internally') }}
        <img src="@/assets/icons/Login.svg" class="login-logout-icon">
      </router-link>
    </div>

    <div v-else-if="routeType === 'study-office' || routeType === 'chairman'" class="links-container">
      <router-link to="" class="router-link-container">
        Verwaltung
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </router-link> <!-- todo replace with manage link -->

      <div v-if="routeType === 'study-office'"> 
        <router-link :to="{ name: 'studyOfficeSelection' }" class="router-link-container">
          Übersicht
          <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
        </router-link>
      </div>
      <div v-else-if="routeType === 'chairman'">
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

  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';

.navigation-container {
  padding: 0rem 1.875rem;
}

.links-container {
  display: flex;
  align-items: center;
  gap: 1.875rem;
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
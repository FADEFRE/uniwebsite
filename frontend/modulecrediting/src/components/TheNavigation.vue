<script setup>
import { logout } from "@/router/logout";
import { useUserStore } from "@/store/authStore"
import { computed } from "vue";

const props = defineProps(['isMenuOpen'])

const navStore = useUserStore();
const isNavType = computed(() => {
  if (navStore.getCurrentRoleNav === "user") { return "standard" }
  else if (navStore.getCurrentRoleNav === "study" || navStore.getCurrentRoleNav === "chair" || navStore.getCurrentRoleNav === "admin") {
    return "internal"
  }
});

const specificRole = computed(() => navStore.getCurrentRoleNav)
</script>

<template>
  <div v-if="isNavType === 'standard'" class="links-container" :class="{ 'small-screen-links-container': isMenuOpen }">
    <router-link :to="{ name: 'home' }" class="router-link" :class="{ 'white-router-link': isMenuOpen }"
      @click="$emit('linkClicked')">
      {{ $t('home page') }}
      <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
    </router-link>
    <router-link :to="{ name: 'login' }" @click="$emit('linkClicked')" class="router-link"
      :class="{ 'white-router-link': isMenuOpen }">
      {{ $t('log-in internally') }}
      <img src="@/assets/icons/LoginWhite.svg" class="login-logout-icon login-icon">
    </router-link>
  </div>

  <div v-else-if="isNavType === 'internal'" class="links-container"
    :class="{ 'small-screen-links-container': isMenuOpen }">
    <router-link :to="{ name: 'management' }" class="router-link" @click="$emit('linkClicked')"
      :class="{ 'white-router-link': isMenuOpen }">
      Verwaltung
      <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
    </router-link>

    <div v-if="specificRole === 'study'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'studyOfficeSelection' }" @click="$emit('linkClicked')" class="router-link"
        :class="{ 'white-router-link': isMenuOpen }">
        Übersicht
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </router-link>
    </div>
    <div v-else-if="specificRole === 'chair'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'chairmanSelection' }" @click="$emit('linkClicked')" class="router-link"
        :class="{ 'white-router-link': isMenuOpen }">
        Übersicht
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </router-link>
    </div>

    <Button @click="logout" class="router-link" :class="{ 'white-router-link': isMenuOpen }">
      Logout
      <img src="@/assets/icons/LogoutWhite.svg" class="login-logout-icon logout-icon">
    </Button>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';


.links-container {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 0.625rem;
  flex-wrap: wrap;
}

.small-screen-links-container {
  flex-direction: column;
  gap: 1rem;

  max-width: 350px;
  width: 100%;
}

.user-specific-container {
  width: 100%;
}


.router-link {
  display: flex;
  gap: 0.625rem;

  &:hover {
    background-color: $dark-gray-hover;

    .arrow-icon {
      transform: translate(0.15rem) rotate(-90deg);
    }

    .login-logout-icon {
      transform: translate(0.15rem);
    }
  }
}

.white-router-link {
  background-color: $white;
  color: $dark-gray;
  width: 100%;

  & .arrow-icon {
    content: url("@/assets/icons/ArrowDark.svg");
  }

  & .login-icon {
    content: url("@/assets/icons/LoginDark.svg");
  }

  & .logout-icon {
    content: url("@/assets/icons/LogoutDark.svg");
  }

  &:hover {
    background-color: $gray;
  }
}

.arrow-icon {
  transform: rotate(-90deg);
  transition: 0.1s ease-in-out;
}

.login-logout-icon {
  transition: 0.1s ease-in-out;
}</style>
<script setup>
import { logout } from "@/router/logout";
import { useUserStore } from "@/store/userStore"
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
    <router-link :to="{ name: 'home' }" class="router-link" :class="{ 'white': isMenuOpen }"
      @click="$emit('linkClicked')">
      {{ $t('navigation.homepage') }}
      <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
    </router-link>
    <router-link :to="{ name: 'login' }" @click="$emit('linkClicked')" class="router-link"
      :class="{ 'white': isMenuOpen }">
      {{ $t('navigation.login') }}
      <img src="@/assets/icons/LoginWhite.svg" class="login-logout-icon login-icon">
    </router-link>
  </div>

  <div v-else-if="isNavType === 'internal'" class="links-container"
    :class="{ 'small-screen-links-container': isMenuOpen }">
    <router-link :to="{ name: 'management' }" class="router-link" @click="$emit('linkClicked')"
      :class="{ 'white': isMenuOpen }">
      Verwaltung
      <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
    </router-link>

    <div v-if="specificRole === 'study'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'studyOfficeSelection' }" @click="$emit('linkClicked')" class="router-link"
        :class="{ 'white': isMenuOpen }">
        Übersicht
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </router-link>
    </div>
    <div v-else-if="specificRole === 'chair'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'chairmanSelection' }" @click="$emit('linkClicked')" class="router-link"
        :class="{ 'white': isMenuOpen }">
        Übersicht
        <img src="@/assets/icons/ArrowWhite.svg" class="arrow-icon">
      </router-link>
    </div>

    <Button @click="logout" class="router-link" :class="{ 'white': isMenuOpen }">
      Logout
      <img src="@/assets/icons/LogoutWhite.svg" class="login-logout-icon logout-icon">
    </Button>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;


.links-container {
  width: 100%;
  display: flex;
  align-items: center;
  gap: spacing(m);
  flex-wrap: wrap;
}

.small-screen-links-container {
  flex-direction: column;
  gap: spacing(m);

  max-width: 350px;
  width: 100%;
}

.user-specific-container {
  width: 100%;
}



.white {
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
}

.arrow-icon {
  transform: rotate(-90deg);
  transition: 0.1s ease-in-out;
}

.login-logout-icon {
  transition: 0.1s ease-in-out;
}

</style>
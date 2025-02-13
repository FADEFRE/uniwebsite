<script setup>
import { computed } from "vue";
import { useUserStore } from "@/store/userStore"
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";
import AuthIcon from "@/assets/icons/AuthIcon.vue";
import { logout } from "@/router/logout";

/*
navigation component for header bar
 */

const props = defineProps([
  /* controls visibility for mobile version, should reflect current opened / closed state of mobile navigation */
  'isMenuOpen'
])

const emit = defineEmits([
  /* emitted on any link click */
  'linkClicked'
])

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
    <router-link :to="{ name: 'home' }" @click="emit('linkClicked')"
                 class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
      {{ $t('TheNavigation.homepage') }}
      <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
    </router-link>
    <router-link :to="{ name: 'login' }" @click="emit('linkClicked')"
                 class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
      {{ $t('TheNavigation.login') }}

      <AuthIcon type="login" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
    </router-link>
  </div>

  <div v-else-if="isNavType === 'internal'" class="links-container"
    :class="{ 'small-screen-links-container': isMenuOpen }">

    <div v-if="specificRole === 'study'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'studyOfficeSelection' }" @click="emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
          ÜBERSICHT
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>
    <div v-else-if="specificRole === 'chair'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'chairmanSelection' }" @click="emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
          ÜBERSICHT
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>

    <div v-if="specificRole === 'admin'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'managementAdmin' }" @click="emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
          VERWALTUNG
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>
    <div v-else :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'management' }" @click="emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
          VERWALTUNG
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>

    <div v-if="specificRole === 'admin'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'accountAdmin' }" @click="emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
          ACCOUNT
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>
    <div v-else :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'account' }" @click="emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
          ACCOUNT
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>

    <Button role="link" @click="logout" class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
        LOGOUT
      <AuthIcon type="logout" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
    </Button>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


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
</style>
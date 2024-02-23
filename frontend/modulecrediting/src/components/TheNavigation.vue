<script setup>
import { logout } from "@/router/logout";
import { useUserStore } from "@/store/userStore"
import { computed } from "vue";
import ArrowIcon from "../assets/icons/ArrowIcon.vue";
import AuthIcon from "../assets/icons/AuthIcon.vue";

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
    <router-link :to="{ name: 'home' }" class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }"
      @click="$emit('linkClicked')">
      {{ $t('navigation.homepage') }}
      <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
    </router-link>
    <router-link :to="{ name: 'login' }" @click="$emit('linkClicked')" class="router-link icon-hover-right"
      :class="{ 'white': isMenuOpen }">
      {{ $t('navigation.login') }}

      <AuthIcon type="login" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
    </router-link>
  </div>

  <div v-else-if="isNavType === 'internal'" class="links-container"
    :class="{ 'small-screen-links-container': isMenuOpen }">

    <div v-if="specificRole === 'study'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'studyOfficeSelection' }" @click="$emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
        Übersicht
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>
    <div v-else-if="specificRole === 'chair'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'chairmanSelection' }" @click="$emit('linkClicked')" class="router-link icon-hover-right"
                   :class="{ 'white': isMenuOpen }">
        Übersicht
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>

    <div v-if="specificRole === 'admin'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'managementAdmin' }" class="router-link icon-hover-right" @click="$emit('linkClicked')"
                   :class="{ 'white': isMenuOpen }">
        Verwaltung
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>
    <div v-else :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'management' }" class="router-link icon-hover-right" @click="$emit('linkClicked')"
                   :class="{ 'white': isMenuOpen }">
        Verwaltung
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>

    <div v-if="specificRole === 'admin'" :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'accountAdmin' }" @click="$emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
        Account
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>
    <div v-else :class="{ 'user-specific-container': isMenuOpen }">
      <router-link :to="{ name: 'account' }" @click="$emit('linkClicked')"
                   class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
        Account
        <ArrowIcon direction="right" :color="isMenuOpen ? 'dark-gray' : 'white'"/>
      </router-link>
    </div>

    <Button @click="logout" class="router-link icon-hover-right" :class="{ 'white': isMenuOpen }">
      Logout
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
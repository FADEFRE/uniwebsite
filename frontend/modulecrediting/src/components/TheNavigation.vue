<script setup>
import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute()
const routeType = computed(() => route.meta['authType'])
</script>

<template>
  <div class="navigation-container">

    <div v-if="routeType === 'standard'" class="links-container">
      <router-link :to="{ name: 'home' }">{{$t('home page')}}</router-link>
      <router-link :to="{ name: 'login' }">{{$t('log-in internally')}}</router-link>
    </div>

    <div v-else-if="routeType === 'study-office' || routeType === 'chairman'" class="links-container">
      <router-link to="">Verwaltungsansicht</router-link> <!-- todo replace with manage link -->

      <div v-if="routeType === 'study-office'">
        <router-link :to="{ name: 'studyOfficeSelection' }">Übersicht</router-link>
      </div>
      <div v-else-if="routeType === 'chairman'">
        <router-link :to="{ name: 'chairmanSelection' }">Übersicht</router-link>
      </div>

      <router-link to="">Log-Out</router-link> <!-- todo replace with logout link -->
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

a {
  &:hover {
    background-color: $black;
  }
}
</style>
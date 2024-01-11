<script setup>
import {useRoute} from "vue-router";
import {computed} from "vue";

const route = useRoute()
const routeType = computed(() => route.meta['type'])
</script>

<template>
  <div>

    <div v-if="routeType === 'standard'">
      <router-link :to="{ name: 'home' }">Startseite</router-link>
      <router-link :to="{ name: 'login' }">Log-in Intern</router-link>
    </div>

    <div v-else-if="routeType === 'study-office' || routeType === 'chairman'">
      <router-link to="">Verwaltungsansicht</router-link>  <!-- todo replace with manage link -->

      <div v-if="routeType === 'study-office'">
        <router-link :to="{ name: 'studyOfficeSelection' }">Übersicht</router-link>
      </div>
      <div v-else-if="routeType === 'chairman'">
        <router-link :to="{ name: 'chairmanSelection' }">Übersicht</router-link>
      </div>

      <router-link to="">Log-Out</router-link>  <!-- todo replace with logout link -->
    </div>

  </div>
</template>

<style scoped lang="scss">

</style>
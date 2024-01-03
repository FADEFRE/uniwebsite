<!--
overview of all applications for study office
displays:
- applications in order of creation VIA OverviewItem
functionality:
- application items link to related detail view
-->

<script setup>
import OverviewItem from "@/components/OverviewItem.vue";
import FilterSelector from "@/components/FilterSelector.vue";
import { useRoute } from "vue-router";
import { ref, onBeforeMount } from "vue"
import { url } from "@/scripts/url-config"
import axios from "axios"

let applicationsData = ref()
onBeforeMount(() => {
  axios.get(url + '/applications')
    .then((response) => {
      applicationsData.value = response.data
    })
  // todo error catching
})

const route = useRoute()
</script>

<template>
  <div class="main">
    <FilterSelector></FilterSelector>
    <div class="overview-list">
      <OverviewItem v-for="application of applicationsData" :data="application" :forward="route.meta.forward" />
    </div>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
  @include main();
}

.overview-list {
  @include verticalList(big);
}
</style>
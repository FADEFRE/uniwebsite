<!--
overview of all applications for study office
displays:
- applications in order of creation VIA OverviewItem
functionality:
- application items link to related detail view
-->

<script setup>
import OverviewItem from "@/components/OverviewItem.vue";
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
</script>

<template>
  <div>
    <OverviewItem
        v-for="application of applicationsData"
        :data="application"
    />
  </div>
</template>

<style scoped>

</style>
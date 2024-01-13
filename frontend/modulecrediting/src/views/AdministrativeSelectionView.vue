<!--
overview of all applications for study office
displays:
- applications in order of creation VIA OverviewItem
functionality:
- application items link to related detail view
-->

<script setup>
import { useRoute } from "vue-router";
import { ref, onBeforeMount } from "vue"
import FilterSelector from "@/components/FilterSelector.vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import { getApplications } from "@/scripts/axios-requests";
import { getFormattedDate } from "@/scripts/date-utils";

let applicationList = ref([])
onBeforeMount(() => {
  getApplications()
      .then(data => applicationList.value = data)
})

const route = useRoute()
</script>

<template>
  <div class="main">
    <FilterSelector></FilterSelector>
    <div class="overview-list">
      <div v-for="application in applicationList">
        <ApplicationOverview
            :id="application['id']"
            :status="application['fullStatus']"
            :course="application['courseLeipzig']['name']"
            :creation-date="getFormattedDate(new Date(application['creationDate']))"
            :last-edited-date="getFormattedDate(new Date(application['lastEditedDate']))"
            :decision-date="getFormattedDate(new Date(application['decisionDate']))"
            :forward="route.meta['forward']"
        />
      </div>
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
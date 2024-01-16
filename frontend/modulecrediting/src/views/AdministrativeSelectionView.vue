<!--
overview of all applications for study office
displays:
- applications in order of creation VIA OverviewItem
functionality:
- application items link to related detail view
-->

<script setup>
import { useRoute } from "vue-router";
import { ref, computed, onBeforeMount } from "vue"
import FilterSelector from "@/components/FilterSelector.vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import { getApplications } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import { filterApplications } from "@/scripts/applications-filter";

const route = useRoute()

let allApplications = ref([])
onBeforeMount(() => {
  getApplications()
      .then(data => allApplications.value = data)
})

const filter = ref()

const filteredApplications = computed(() => {
  if (filter.value) {
    return filterApplications(filter.value, allApplications.value)
  } else {
    return allApplications.value
  }
})                  //:forward="route.matched.some(route => route.meta['forward'])"
</script>

<template>
  <div class="main">
    <FilterSelector ref="filter" />
    <div class="overview-list">

      <div v-for="application in filteredApplications">
        <ApplicationOverview
            :id="application['id']"
            :status="application['fullStatus']"
            :course="application['courseLeipzig']['name']"
            :creation-date="parseRequestDate(application['creationDate'])"
            :last-edited-date="parseRequestDate(application['lastEditedDate'])"
            :decision-date="parseRequestDate(application['decisionDate'])"
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
  width: 70%;
}
</style>
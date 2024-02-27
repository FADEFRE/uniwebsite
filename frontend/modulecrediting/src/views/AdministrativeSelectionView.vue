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
import FilterSelector from "@/components/filter/FilterSelector.vue";
import ApplicationOverview from "@/components/abstract/ApplicationOverview.vue";
import { getApplications } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import { filterApplications } from "@/scripts/applications-filter";
import LoadingContainer from "@/components/util/LoadingContainer.vue";

const route = useRoute()

let allApplications = ref()
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
  <div v-if="allApplications" class="main">
    <h1 class="screen-reader-only">Übersichtsseite aller Anträge</h1>
    <div class="side-infos-list wide">
      <FilterSelector ref="filter" />
    </div>

    <div class="content-container split narrow">
      <h2 class="screen-reader-only">Liste aller Anträge</h2>

      <div v-if="allApplications.length > 0" v-for="application in filteredApplications">
        <ApplicationOverview
            :id="application['id']"
            :status="application['fullStatus']"
            :course="application['courseLeipzig']['name']"
            :creation-date="parseRequestDate(application['creationDate'])"
            :last-edited-date="parseRequestDate(application['lastEditedDate'])"
            :decision-date="parseRequestDate(application['decisionDate'])"
            :forward="route.meta['forward']"
            :adminSelectionView="true"
            class="admin-selection-view"
        />
      </div>
      <div v-else class="applications-empty">
        <p>Es gibt keine Anträge.</p>
      </div>

    </div>
  </div>
  <div v-else class="main centered">
    <LoadingContainer />
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.applications-empty {
  align-self: center;
}
</style>
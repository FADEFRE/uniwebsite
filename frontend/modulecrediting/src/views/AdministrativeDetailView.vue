<script setup>
import {useRoute} from "vue-router";
import {ref, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import AdministrativePanel from "@/components/AdministrativePanel.vue";
import {getApplicationById, getModulesByCourse} from "@/scripts/axios-requests";
import {parseRequestDate} from "@/scripts/date-utils";

const route = useRoute()
const id = route.params.id
const type = route.meta['type']

const applicationData = ref()
const moduleOptions = ref([])

onBeforeMount(() => {
  getApplicationById(id)
      .then(data => {
        applicationData.value = data
        return data
      })
      .then(data => {
        return getModulesByCourse(data['courseLeipzig']['name'])
      })
      .then(modules => {
        moduleOptions.value = modules
      })
})
</script>

<template>
  <div>

    <!-- request pending -->
    <div v-if="!applicationData">
      <p>Lade Daten ...</p>
    </div>

    <div v-else>

      <ApplicationOverview
          :creation-date="parseRequestDate(applicationData['creationDate'])"
          :last-edited-date="parseRequestDate(applicationData['lastEditedDate'])"
          :decision-date="parseRequestDate(applicationData['decisionDate'])"
          :id="applicationData['id']"
          :course="applicationData['courseLeipzig']['name']"
          :status="applicationData['fullStatus']"
      />

      <div v-for="connection in applicationData['modulesConnections']">

        <AdministrativePanel :type="type" :selectable-modules="moduleOptions" :connection-data="connection" />

      </div>

    </div>

  </div>
</template>

<style scoped>

</style>
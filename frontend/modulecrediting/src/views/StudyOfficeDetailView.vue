<!--
documentation todo
-->

<script setup>
import { useRoute } from "vue-router";
import { ref, onBeforeMount } from "vue";
import { getApplicationById, getModulesByCourse } from "@/scripts/axios-requests"
import StudyOfficeModulePanel from "@/components/StudyOfficeModulePanel.vue";

const route = useRoute()

let id = undefined
const applicationData = ref(undefined)

const internalModules = ref(undefined)

onBeforeMount(() => {
  // getting application data
  id = route.params.id
  getApplicationById(id)
      .then(data => {
        applicationData.value = data
        return data
      })
      .then(data => {
        return getModulesByCourse(data.courseLeipzig.name)
      })
      .then(moduleArray => {
        internalModules.value = moduleArray
      })
      .catch(error => {
        console.log(error)
        applicationData.value = 'error'
        internalModules.value = 'error'
      })
})
</script>

<template>
  <div>

    <!-- request pending -->
    <div v-if="!applicationData || !internalModules">
      <p>Lade Daten ...</p>
    </div>

    <!-- request rejected -->
    <div v-else-if="applicationData === 'error' || internalModules === 'error'">
      <p>Fehler bei der Datenabfrage!</p>
    </div>

    <!-- request resolved -->
    <div v-else>

      <div>
        <p>Vorgangsnummer: {{ applicationData.id }}</p>
        <p>Studiengang: {{ applicationData.courseLeipzig.name }}</p>
        <p>Status: {{ applicationData.fullStatus }}</p>
        <p>Erstellt: {{ applicationData.creationDate }}</p>
      </div>

      <div>
        <StudyOfficeModulePanel
          v-for="moduleConnection in applicationData.modulesConnections"
          :module-connection-data="moduleConnection"
          :internal-module-options="internalModules"
        />
      </div>

    </div>

  </div>
</template>

<style scoped>

</style>
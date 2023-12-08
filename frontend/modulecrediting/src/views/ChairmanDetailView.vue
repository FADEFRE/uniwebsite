<!--
documentation todo
-->

<script setup>
import { useRoute } from "vue-router";
import { ref, onBeforeMount } from "vue";
import { getApplicationById, getModulesByCourse, putChairman } from "@/scripts/axios-requests";
import ChairmanModulePanel from "@/components/ChairmanModulePanel.vue";

const route = useRoute()
let id = undefined

const applicationData = ref(undefined)
const internalModules = ref(undefined)

onBeforeMount(() => {
  id = route.params.id
  getApplicationById(id)
      .then(data => {
        applicationData.value = data
        return getModulesByCourse(data.courseLeipzig.name)
      })
      .then(moduleArray => {
        internalModules.value = moduleArray
      })
      .catch(error => {
        console.log(error)
        applicationData.value = 'error'
      })
})

const modulePanelsRef = ref([])

const checkPutData = () => {
  if (!id) {
    return false
  }
  return modulePanelsRef.value.every(obj => obj.chairman.decision)
}

const triggerPutData = () => {
  if (checkPutData()) {
    console.log(modulePanelsRef.value)
    const applicationObjects = modulePanelsRef.value.map(panel => {
      return {
        moduleName: panel.general.base.moduleName,
        university: panel.general.base.university,
        creditPoints: panel.general.base.creditPoints,
        pointSystem: panel.general.base.pointSystem,
        selectedInternalModules: panel.general.internalModules.selectedInternalModules,
        asExamCertificate: panel.general.asExamCertificate,
        decisionFinal: panel.chairman.decision === 'Annehmen' ? 'ANGENOMMEN' : 'ABGELEHNT',
        commentDecision: panel.chairman.comment.comment,
      }
    })
    console.log(applicationObjects)
    putChairman(id, applicationObjects)
        .then(() => location.reload())
  } else {
    console.log('check in chairman triggerPutData failed')
    // todo user feedback
  }
}
</script>

<template>
  <div>

    <!-- request pending -->
    <div v-if="!applicationData || !internalModules">
      <p>Lade Daten...</p>
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
        <ChairmanModulePanel
          v-for="moduleConnection in applicationData.modulesConnections"
          :module-connection-data="moduleConnection"
          :internal-module-options="internalModules"
          ref="modulePanelsRef"
        />
      </div>

      <!-- todo -->
      <Button @click="triggerPutData">Speichern</Button>

    </div>

  </div>
</template>

<style scoped>

</style>
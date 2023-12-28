<!--
documentation todo
-->

<script setup>
import { useRoute } from "vue-router";
import { ref, onBeforeMount } from "vue";
import { getApplicationById, getModulesByCourse, putStudyOffice } from "@/scripts/axios-requests"
import StudyOfficeModulePanel from "@/components/StudyOfficeModulePanel.vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import PanelHeader from "@/components/PanelHeader.vue";
import PanelStatusIcons from "@/components/PanelStatusIcons.vue";
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
      })
})

const modulePanelsRef = ref([])

const checkPutData = () => {
  if (!id) {
    return false
  }
  return modulePanelsRef.value.every(obj => obj.studyOffice.decision)
}

const triggerPutData = () => {
  if (checkPutData()) {
    const applicationObjects = modulePanelsRef.value.map(panel => {
      return {
        moduleName: panel.general.base.moduleName,
        university: panel.general.base.university,
        creditPoints: panel.general.base.creditPoints,
        pointSystem: panel.general.base.pointSystem,
        selectedInternalModules: panel.general.internalModules.selectedInternalModules,
        asExamCertificate: panel.general.asExamCertificate,
        decisionSuggestion: panel.studyOffice.decision === 'Annehmen' ? 'ANGENOMMEN' : 'ABGELEHNT',
        commentStudyOffice: panel.studyOffice.comment.comment,
      }
    })
    console.log(applicationObjects)
    putStudyOffice(id, applicationObjects)
        .then(() => location.reload())
  } else {
    console.log('check in study office triggerPutData failed')
    // todo user feedback
  }
}
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

      </div>
      <PanelStatusIcons
            :decisionSuggestion="moduleConnection['decisionSuggestion']"
            :decisionFinal="moduleConnection['decisionFinal']"
          />
      <div>
        <PanelHeader
        v-for="moduleConnection in applicationData['modulesConnections']"
                         :key="moduleConnection['moduleApplication']['name']"
                         :moduleName="moduleConnection['moduleApplication']['name']"
                         :internalModules="moduleConnection['modulesLeipzig']"
        />
      </div>

      <div>
      <ApplicationOverview
          :vorgangsnummer="applicationData.id"
          :studiengang="applicationData.courseLeipzig.name"
          :status="applicationData.fullStatus"
          :creationDate="applicationData.creationDate">
        </ApplicationOverview>
      </div>

      <div>
        <StudyOfficeModulePanel
          v-for="moduleConnection in applicationData.modulesConnections"
          :module-connection-data="moduleConnection"
          :internal-module-options="internalModules"
          ref="modulePanelsRef"
        />
      </div>

      <Button @click="triggerPutData">
        Speichern
      </Button>

    </div>

  </div>
</template>

<style scoped>

</style>
<script setup>
import { useRoute } from "vue-router";
import { ref, computed, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import AdministrativePanel from "@/components/AdministrativePanel.vue";
import ButtonLink from "@/components/ButtonLink.vue";
import { getApplicationById, getModulesByCourse, putApplication } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import ApplicationConnectionLinks from "@/components/ApplicationConnectionLinks.vue";

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

const moduleConnections = ref()

const connectionsData = computed(() => {
  const dataArray = []
  if (moduleConnections.value) {
    for (let connection of moduleConnections.value) {
      const connectionObj = {}
      connectionObj['id'] = connection.id
      connectionObj['externalModules'] = connection.externalModules.map(m => m.name)
      connectionObj['internalModules'] = connection.internalModules
      dataArray.push(connectionObj)
    }
  }
  return dataArray
})

const triggerPutRequest = () => {
  // defining userRole
  let userRole = undefined
  if (type === 'study-office') userRole = 'study_office'
  else if (type === 'chairman') userRole = 'chairman'
  else console.warn('AdministrativeDetailView: userRole is undefined in triggerPutRequest')

  // creation connectionObjects
  const connectionObjects = []

  for (let connection of moduleConnections.value) {
    const connectionObj = {
      id: connection.id,
      externalModules: connection.externalModules,
      internalModules: connection.internalModules,
    }
    if (type === 'study-office') {
      if (connection.studyOfficeDecisionData.comment) connectionObj['commentStudyOffice'] = connection.studyOfficeDecisionData.comment
      if (connection.studyOfficeDecisionData.decision) connectionObj['decisionSuggestion'] = connection.studyOfficeDecisionData.decision
    }
    else if (type === 'chairman') {
      if (connection.chairmanDecisionData.comment) connectionObj['commentDecision'] = connection.chairmanDecisionData.comment
      if (connection.chairmanDecisionData.decision) connectionObj['decisionFinal'] = connection.chairmanDecisionData.decision
    }
    connectionObjects.push(connectionObj)
  }

  // axios request
  putApplication(userRole, applicationData.value['id'], applicationData.value['courseLeipzig']['name'], connectionObjects)
}
</script>

<template>
  <div>

    <!-- request pending -->
    <div v-if="!applicationData">
      <p>Lade Daten ...</p>
    </div>

    <div v-else>

      <ApplicationConnectionLinks :connections-data="connectionsData" />

      <ApplicationOverview
          :creation-date="parseRequestDate(applicationData['creationDate'])"
          :last-edited-date="parseRequestDate(applicationData['lastEditedDate'])"
          :decision-date="parseRequestDate(applicationData['decisionDate'])"
          :id="applicationData['id']"
          :course="applicationData['courseLeipzig']['name']"
          :status="applicationData['fullStatus']"
      />

      <div v-for="connection in applicationData['modulesConnections']">

        <AdministrativePanel
            :type="type"
            :selectable-modules="moduleOptions"
            :connection-data="connection"
            ref="moduleConnections"
            :id="connection.id"
        />

      </div>

      <ButtonLink @click="triggerPutRequest">Speichern</ButtonLink>

    </div>

  </div>
</template>

<style scoped>

</style>
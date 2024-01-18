<script setup>
import { useRoute } from "vue-router";
import { ref, computed, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import AdministrativePanel from "@/components/AdministrativePanel.vue";
import ButtonLink from "@/components/ButtonLink.vue";
import {
  getApplicationById, getModulesByCourse, putApplication,
  getUpdateStatusAllowed, updateStatus
} from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import ApplicationConnectionLinks from "@/components/ApplicationConnectionLinks.vue";

const route = useRoute()
const id = route.params.id
const connectionHighlightId = route.params.connection
const type = route.meta['authType']
const readonly = ref(true)

const applicationData = ref()
const moduleOptions = ref([])
const passOnPossible = ref(false)

onBeforeMount(() => {
  getApplicationById(id)
    .then(data => {
      // applicationData
      data['modulesConnections'].sort((a, b) => a.id - b.id)
      applicationData.value = data
      // readonly
      if (type === 'study-office' && (data['fullStatus'] === 'NEU' || data['fullStatus'] === 'STUDIENBÜRO')) {
        readonly.value = false
      } else if (type === 'chairman' && data['fullStatus'] !== 'ABGESCHLOSSEN') {
        readonly.value = false
      }
      return data
    })
    .then(data => {
      return getModulesByCourse(data['courseLeipzig']['name'])
    })
    .then(modules => {
      moduleOptions.value = modules
    })
    .then(_ => {
      return getUpdateStatusAllowed(id)
    })
    .then(updateAllowed => {
      passOnPossible.value = updateAllowed
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
      dataArray.push(connectionObj)
    }
  }
  return dataArray
})

const unsaved = ref(false)

const setUnsaved = () => {
  unsaved.value = true
}

const discardChanges = () => {
  location.reload()
}

const saveChanges = () => {
  // defining userRole
  let userRole = undefined
  if (type === 'study-office') userRole = 'study-office'
  else if (type === 'chairman') userRole = 'pav'
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
    } else if (type === 'chairman') {
      if (connection.chairmanDecisionData.comment) connectionObj['commentDecision'] = connection.chairmanDecisionData.comment
      if (connection.chairmanDecisionData.decision) connectionObj['decisionFinal'] = connection.chairmanDecisionData.decision
    }
    connectionObjects.push(connectionObj)
  }

  // axios request
  putApplication(userRole, applicationData.value['id'], applicationData.value['courseLeipzig']['name'], connectionObjects)
    .then(_ => {
      location.reload()
    })
}

const triggerPassOn = () => {
  if (passOnPossible) updateStatus(id).then(_ => location.reload())
}
</script>

<template>
  <!-- request pending -->
  <div v-if="!applicationData" class="main">
    <p>Lade Daten ...</p>
  </div>

  <div v-else class="main">

    <ApplicationConnectionLinks :connections-data="connectionsData" />

    <div class="administrative-detail-container">
      <ApplicationOverview :creation-date="parseRequestDate(applicationData['creationDate'])"
        :last-edited-date="parseRequestDate(applicationData['lastEditedDate'])"
        :decision-date="parseRequestDate(applicationData['decisionDate'])" :id="applicationData['id']"
        :course="applicationData['courseLeipzig']['name']" :status="applicationData['fullStatus']" />

      <div v-for="connection in applicationData['modulesConnections']">

        <AdministrativePanel :type="type" :readonly="readonly" :selectable-modules="moduleOptions"
          :connection-data="connection" ref="moduleConnections"
          :class="{ 'connection-highlight': connection.id == connectionHighlightId }" :id="connection.id"
          @change="setUnsaved" />

      </div>

      <div v-if="!readonly">
        <ButtonLink @click="discardChanges">Änderungen verwerfen</ButtonLink>
        <ButtonLink @click="saveChanges">Speichern</ButtonLink>
        <ButtonLink :disabled="!passOnPossible" :primaryButton="true" @click="triggerPassOn">
          Weitergeben
        </ButtonLink>
      </div>

      <div v-if="unsaved">
        <Button class="unsaved-bitton">
          <h3 class="unsaved-button-text">!</h3>
        </Button>
      </div>

    </div>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
  @include main();
  max-width: 1600px;
}

.administrative-detail-container {
  @include verticalList(small);
  width: 100%;
}

.connection-highlight {
  border-left: 1rem solid $dark-gray;
}

.unsaved-bitton {
  background-color: $red;
  width: 3.125rem;
  height: 3.125rem;
}

.unsaved-button-text {
  color: $white;
}
</style>
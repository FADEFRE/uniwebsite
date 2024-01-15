<script setup>
import { useRoute } from "vue-router";
import { ref, computed, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import AdministrativePanel from "@/components/AdministrativePanel.vue";
import ButtonLink from "@/components/ButtonLink.vue";
import { getApplicationById, getModulesByCourse, putApplication, updateStatus } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import ApplicationConnectionLinks from "@/components/ApplicationConnectionLinks.vue";

const route = useRoute()
const id = route.params.id
const type = route.meta['type']

const applicationData = ref()
const moduleOptions = ref([])
const passOnPossible = ref(false)

const checkPassOnPossibility = (data) => {
  let decisionKey = undefined
  if (type === 'study-office') {
    decisionKey = 'decisionSuggestion'
  } else if (type === 'chairman') {
    decisionKey = 'decisionFinal'
  }
  return data['modulesConnections'].every(c => c[decisionKey] !== 'unedited')
}

onBeforeMount(() => {
  getApplicationById(id)
    .then(data => {
      passOnPossible.value = checkPassOnPossibility(data)
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
      dataArray.push(connectionObj)
    }
  }
  return dataArray
})

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
    }
    else if (type === 'chairman') {
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
  updateStatus(id)
    .then(data => {
      if (!data) {
        alert('Fehler beim Weitergeben!')
      } else {
        location.reload()
      }
    })
}
</script>

<template>
  <div class="main">

    <!-- request pending -->
    <div v-if="!applicationData">
      <p>Lade Daten ...</p>
    </div>

    <div v-else class="administrative-detail-container">

      <ApplicationConnectionLinks :connections-data="connectionsData" />

      <ApplicationOverview :creation-date="parseRequestDate(applicationData['creationDate'])"
        :last-edited-date="parseRequestDate(applicationData['lastEditedDate'])"
        :decision-date="parseRequestDate(applicationData['decisionDate'])" :id="applicationData['id']"
        :course="applicationData['courseLeipzig']['name']" :status="applicationData['fullStatus']" />

      <div v-for="connection in applicationData['modulesConnections']">

        <AdministrativePanel :type="type" :selectable-modules="moduleOptions" :connection-data="connection"
          ref="moduleConnections" :id="connection.id" />

      </div>

      <ButtonLink @click="discardChanges">Änderungen verwerfen</ButtonLink>
      <ButtonLink @click="saveChanges">Speichern</ButtonLink>
      <ButtonLink @click="triggerPassOn" :class="{ 'pass-on-not-possible': !passOnPossible }" primaryButton="true">
        Weitergeben</ButtonLink>

    </div>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
  @include main();
}

.administrative-detail-container {
  @include verticalList(small);
  width: 100%;
}

.side-infos-container {
  @include verticalList(big);
  width: min-content;
}


</style>
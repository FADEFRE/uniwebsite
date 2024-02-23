<!--
shows status of an application
-->

<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onBeforeMount, computed } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import FormalRejectionInfoBox from "@/components/FormalRejectionInfoBox.vue";
import CompletedInfoBox from "@/components/CompletedInfoBox.vue";
import SideInfoContainer from '../components/SideInfoContainer.vue';
import StatusPanel from "@/components/StatusPanel.vue";
import ApplicationPanel from "@/components/ApplicationPanel.vue";
import ButtonLink from '@/components/ButtonLink.vue';
import ButtonAdd from "../components/ButtonAdd.vue";
import ButtonDownloadVue from '../components/ButtonDownload.vue';
import LoadingContainer from "../components/LoadingContainer.vue";
import { url } from "@/scripts/url-config"
import { getApplicationByIdForStatus, getModulesByCourse, putApplicationStudent } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import ApplicationProcessSideInfo from "@/components/ApplicationProcessSideInfo.vue";
import StudyOfficeSideInfo from "@/components/StudyOfficeSideInfo.vue";

const id = useRoute().params.id
const summaryDocumentLink = `${url}/file/pdf-documents/application/${id}`

const applicationData = ref()
const moduleOptions = ref([])
const router = useRouter();


onBeforeMount(() => {
  getApplicationByIdForStatus(id)
    .then(data => {
      applicationData.value = data;
      existingConnections.value = data['modulesConnections']
      return data;
    })
    .then(data => {
      return getModulesByCourse(data['courseLeipzig']['name'])
    })
    .then(modules => {
      moduleOptions.value = modules
    })
})

// handling existing connections
const existingConnections = ref()

const deleteExistingConnection = (id) => {
  existingConnections.value = existingConnections.value.filter(c => id !== c.id)
}

const existingConnectionsRef = ref()

// handling new connections
const newConnections = ref([])

const addNewConnection = () => {
  if (newConnections.value.length > 0) {
    const nextIndex = Math.max(...newConnections.value) + 1
    newConnections.value.push(nextIndex)
  } else {
    newConnections.value.push(0)
  }
}

const deleteNewConnection = (key) => {
  newConnections.value = newConnections.value.filter(k => k !== key)
}

const newConnectionsRef = ref()

// concatenating connections
const moduleConnections = computed(() => {
  let connectionsArray = []
  if (existingConnectionsRef.value) connectionsArray = connectionsArray.concat(existingConnectionsRef.value)
  if (newConnectionsRef.value) connectionsArray = connectionsArray.concat(newConnectionsRef.value)
  return connectionsArray
})

const openSummaryDocument = () => {
  window.open(summaryDocumentLink, '_blank')
}

const checkValidity = () => {
  return moduleConnections.value.map(c => c.checkValidity()).every(Boolean)
}

const triggerSubmit = () => {
  if (checkValidity()) {
    putApplicationStudent(applicationData.value['id'], applicationData.value['courseLeipzig']['name'], moduleConnections.value)
      .then(_ => location.reload())
  }
}
</script>

<template>
  <div v-if="applicationData" class="main">

    <div class="content-container split">

      <div v-if="applicationData['fullStatus'] === 'FORMFEHLER'">
        <FormalRejectionInfoBox />
      </div>

      <div v-if="applicationData['fullStatus'] === 'ABGESCHLOSSEN'">
        <CompletedInfoBox />
      </div>

      <ApplicationOverview :creation-date="parseRequestDate(applicationData['creationDate'])"
        :last-edited-date="parseRequestDate(applicationData['lastEditedDate'])"
        :decision-date="parseRequestDate(applicationData['decisionDate'])" :id="applicationData['id']"
        :course="applicationData['courseLeipzig']['name']" :status="applicationData['fullStatus']" />

      <div v-if="existingConnections" class="modules-connections-container">
        <StatusPanel v-for="connection in existingConnections" :key="connection.id" :connection="connection"
          :selectable-modules="moduleOptions" :readonly="!(applicationData['fullStatus'] === 'FORMFEHLER')"
          :allow-delete="applicationData['fullStatus'] === 'FORMFEHLER' && moduleConnections.length > 1"
          @delete-self="deleteExistingConnection(connection.id)" ref="existingConnectionsRef"
          :class="{ 'formal-rejection-highlight': connection['formalRejection'] }" />
      </div>

      <div v-if="applicationData['fullStatus'] === 'FORMFEHLER'" class="modules-connections-container">
        <ApplicationPanel v-for="key in newConnections" :key="key" :selectable-modules="moduleOptions"
          :allow-delete="moduleConnections.length > 1" @delete-self="deleteNewConnection(key)" ref="newConnectionsRef" />
      </div>

      
      <div class="application-buttons-container">
        <ButtonAdd v-if="applicationData['fullStatus'] === 'FORMFEHLER'" @click="addNewConnection">Modulzuweisung hinzufügen</ButtonAdd>
        <ButtonLink v-if="applicationData['fullStatus'] === 'FORMFEHLER'" :redButton="true" @click="triggerSubmit">Neu einreichen</ButtonLink>
      </div>
      <ButtonDownloadVue @click="openSummaryDocument">
        Antrag herunterladen
      </ButtonDownloadVue>
    </div>

    <div class="side-infos-list">
      <ApplicationProcessSideInfo />
      <StudyOfficeSideInfo />
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


.modules-connections-container {
  @include verticalList(s);
  width: 100%;
  overflow: hidden;
}

.formal-rejection-highlight {
  border-left: spacing(m) solid $red;
}
</style>

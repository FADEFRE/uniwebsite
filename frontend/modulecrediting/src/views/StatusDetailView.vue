<!--
shows status of an application
-->

<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onBeforeMount, computed } from "vue";
import ApplicationOverview from "@/components/abstract/ApplicationOverview.vue";
import StatusPanel from "@/components/panel/StatusPanel.vue";
import ApplicationPanel from "@/components/panel/ApplicationPanel.vue";
import ButtonLink from '@/components/button/ButtonLink.vue';
import ButtonAdd from "@/components/button/ButtonAdd.vue";
import ButtonDownloadVue from '@/components/button/ButtonDownload.vue';
import LoadingContainer from "@/components/util/LoadingContainer.vue";
import FormalRejectionInfoBox from "@/components/info-box/FormalRejectionInfoBox.vue";
import FinishedInfoBox from "@/components/info-box/FinishedInfoBox.vue";
import SideInfoApplicationProcess from '@/components/side-info/SideInfoApplicationProcess.vue';
import SideInfoStudyOffice from '@/components/side-info/SideInfoStudyOffice.vue';
import { url } from "@/config/url-config"
import { parseRequestDate } from "@/utils/date-utils";
import { getModulesByCourse } from "@/requests/module-course-requests";
import { getApplicationByIdForStatus, putApplicationStudent } from "@/requests/application-requests";
import i18n from '@/i18n'

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
    let popup = i18n.global.t('StatusDetailView.Popup.Heading') + "\n\n" + i18n.global.t('StatusDetailView.Popup.Text')
    if (confirm(popup)) {
      putApplicationStudent(applicationData.value['id'], applicationData.value['courseLeipzig']['name'], moduleConnections.value)
          .then(_ => location.reload())
    }
  }
}
</script>

<template>
  <div v-if="applicationData" class="main">
    <h1 class="screen-reader-only">{{ $t('StatusDetailView.SRHeading') }}</h1>

    <div class="content-container split">

      <div v-if="applicationData['fullStatus'] === 'FORMFEHLER'">
        <FormalRejectionInfoBox />
      </div>

      <div v-if="applicationData['fullStatus'] === 'ABGESCHLOSSEN'">
        <FinishedInfoBox />
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
        <ButtonAdd v-if="applicationData['fullStatus'] === 'FORMFEHLER'" @click="addNewConnection">
          {{ $t('StatusDetailView.AddModule') }}
        </ButtonAdd>
        <ButtonLink v-if="applicationData['fullStatus'] === 'FORMFEHLER'" :redButton="true" @click="triggerSubmit">
          {{ $t('StatusDetailView.Renew') }}
        </ButtonLink>
      </div>
      <ButtonDownloadVue @click="openSummaryDocument">
        {{ $t('StatusDetailView.DownloadApplication') }}
      </ButtonDownloadVue>
    </div>

    <aside class="side-infos-list">
      <SideInfoApplicationProcess />
      <SideInfoStudyOffice />
    </aside>

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

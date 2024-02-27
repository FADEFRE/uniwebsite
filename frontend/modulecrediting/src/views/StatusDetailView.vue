<!--
shows status of an application
-->

<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onBeforeMount, computed } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import StatusPanel from "@/components/StatusPanel.vue";
import ApplicationPanel from "@/components/ApplicationPanel.vue";
import ButtonLink from '@/components/ButtonLink.vue';
import ButtonAdd from "@/components/ButtonAdd.vue";
import ButtonDownloadVue from '@/components/ButtonDownload.vue';
import LoadingContainer from "@/components/LoadingContainer.vue";
import { url } from "@/scripts/url-config"
import { getApplicationByIdForStatus, getModulesByCourse, putApplicationStudent } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import SideInfoApplicationProcess from '@/components/SideInfoApplicationProcess.vue';
import SideInfoStudyOffice from '@/components/SideInfoStudyOffice.vue';
import ModuleStatusIcon from "@/assets/icons/ModuleStatusIcon.vue";

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
    if (confirm('Haben Sie alle Formfehler korrigiert?\n\nNach dem erneuten Einreichen können Sie den Antrag nicht weiter bearbeiten.')) {
      putApplicationStudent(applicationData.value['id'], applicationData.value['courseLeipzig']['name'], moduleConnections.value)
          .then(_ => location.reload())
    }
  }
}
</script>

<template>
  <div v-if="applicationData" class="main">
    <h1 class="screen-reader-only">Status des Antrags</h1>

    <div class="content-container split">

      <div v-if="applicationData['fullStatus'] === 'FORMFEHLER'" class="application-info-container">
        <h2>Formfehler</h2>
        <p class="text-justify">
          Ihr Antrag wurde aufgrund von Formfehlern zurückgewiesen.
          Es sind die Modulzuweisungen rot markiert, die Formfehler enthalten.
          In der Modulzuweisung finden sie unten eine Erklärung des konkreten Fehlers.
          Bitte korrigieren sie alle angegebenen Fehler.
          Anschließend können sie ihren Antrag neu einreichen.
        </p>
      </div>

      <div v-if="applicationData['fullStatus'] === 'ABGESCHLOSSEN'" class="application-info-container">
        <h2>Wie geht es weiter?</h2>
        <p class="text-justify">
          Es wurde eine finale Entscheidung zu ihrem Antrag getroffen.
          Dies ist nur eine Informationen über die Möglichkeit der Anrechnung.
          Um sich ihre Leistungen offiziell anrechnen zu lassen, müssen sie zum Studienbüro gehen.
          Bringen sie hierfür bitte alle relevanten Dokumente mit, die den Abschluss der anzurechnenden Leistungen
          belegen.
          Falls sie weitere Fragen zu ihrem Antrag haben, kontaktieren sie bitte das Studienbüro.
        </p>
        <div class="legend-container">
          <h3 class="h4">Legende</h3>
          <ul>
            <li class="explanation-list-item">
              <ModuleStatusIcon status-decision="accepted" size="small"/>
              <p>Anrechnung angenommen</p>
            </li>
            <li class="explanation-list-item">
              <ModuleStatusIcon status-decision="asExamCertificate" size="small"/>
              <p>Anrechnung als Übungsschein</p>
            </li>
            <li class="explanation-list-item">
              <ModuleStatusIcon status-decision="denied" size="small"/>
              <p>Anrechnung abgelehnt</p>
            </li>
          </ul>
        </div>
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
          Modulzuweisung hinzufügen
        </ButtonAdd>
        <ButtonLink v-if="applicationData['fullStatus'] === 'FORMFEHLER'" :redButton="true" @click="triggerSubmit">
          Neu einreichen
        </ButtonLink>
      </div>
      <ButtonDownloadVue @click="openSummaryDocument">
        Antrag herunterladen
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

.legend-container {
  @include verticalList(s);
}

.explanation-list-item {
  @include smallHighlightBox();
  @include verticalListItem($gray);
  justify-content: flex-start;
}
</style>

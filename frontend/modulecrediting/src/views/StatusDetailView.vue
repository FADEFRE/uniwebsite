<!--
shows status of an application
-->

<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onBeforeMount, computed } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import FormalRejectionInfoBox from "@/components/FormalRejectionInfoBox.vue";
import SideInfoContainer from '../components/SideInfoContainer.vue';
import StatusPanel from "@/components/StatusPanel.vue";
import ApplicationPanel from "../components/ApplicationPanel.vue";
import ButtonLink from '@/components/ButtonLink.vue';
import ButtonAdd from "../components/ButtonAdd.vue";
import { url } from "@/scripts/url-config"
import { getApplicationByIdForStatus, getModulesByCourse, putApplicationStudent } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";

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
  <div class="main">

    <div v-if="applicationData" class="status-detail-container">

      <div v-if="applicationData['fullStatus'] === 'FORMFEHLER'" class="formal-rejection-info-container">
        <FormalRejectionInfoBox />
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
        <ButtonAdd @click="addNewConnection">Modulzuweisung hinzufügen</ButtonAdd>
      </div>

      <Button @click="openSummaryDocument">
        Antrag herunterladen
        <img src="../assets/icons/Download.svg">
      </Button>

      <ButtonLink v-if="applicationData['fullStatus'] === 'FORMFEHLER'" :fixed="true" @click="triggerSubmit">
        Neu einreichen
      </ButtonLink>
    </div>
    <div class="side-infos-container">
      <!--SideInfoContainerfür Antragprozess -->
      <SideInfoContainer :heading="'ANTRAGSPROZESS'">
        <ul class="list-container">
          <li class="list-item">Antrag online stellen</li>
          <li class="list-item">Über Vorgangsnummer online Status einsehen</li>
          <li class="list-item">Auf Entscheidung des PAV warten</li>
          <li class="list-item">Mit abgeschlossenem Antrag zum Studienbüro gehen</li>
        </ul>
      </SideInfoContainer>
      <SideInfoContainer :heading="'STUDIENBÜRO'">
        <p>Fakultät für Mathematik und Informatik</p>
        <div class="main-info-container">
          <div class="info-group-container">
            <h4>Anschrift</h4>
            <ul>
              <li>Neues Augusteum</li>
              <li>Augustusplatz 10</li>
              <li>04109 Leipzig</li>
            </ul>
          </div>
          <div class="info-group-container">
            <h4>Kontakt</h4>
            <ul>
              <li>Telefon: +49 341 97-32165</li>
              <li>Telefax: +49 341 97-32193</li>
              <li>E-Mail: studienbuero@math.uni-leipzig.de</li>
            </ul>
          </div>
          <div class="info-group-container">
            <h4>Sprechzeiten</h4>
            <p>Dienstag und Donnerstag: 9:00 - 11:30 Uhr und 12:30 - 16:00 Uhr</p>
          </div>
          <a href="https://www.mathcs.uni-leipzig.de/studium/studienbuero" class="link-container">
            Zum Studienbüro
            <img src="../assets/icons/ArrowWhite.svg" class="arrow-icon" alt="Arrow Icon">
          </a>
        </div>

      </SideInfoContainer>
    </div>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
  @include main();
}

.status-detail-container {
  @include applicationContainer(noSplit);
}

.formal-rejection-info-container {
  margin-bottom: 1rem;
}

.modules-connections-container {
  @include verticalList(small);
  width: 100%;
  overflow: hidden;
}


.side-infos-container {
  @include sideInfoListContainer();
}

.formal-rejection-highlight {
  border-left: 1rem solid $red;
}

.link-container {
  &:hover {
    .arrow-icon {
      transform: translate(0.15rem) rotate(-90deg);
    }
  }
}

.arrow-icon {
  transform: rotate(-90deg);
  transition: 0.1s ease-in-out;
}
</style>

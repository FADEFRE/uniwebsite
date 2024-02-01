<!--
shows status of an application
-->

<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import StatusPanel from "@/components/StatusPanel.vue";
import SideInfoContainer from '../components/SideInfoContainer.vue';
import { url } from "@/scripts/url-config"
import { getApplicationByIdForStatus, getModulesByCourse, putApplicationStudent } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import ButtonLink from '@/components/ButtonLink.vue';
import FormalRejectionInfoBox from "@/components/FormalRejectionInfoBox.vue";

const id = useRoute().params.id
const summaryDocumentLink = `${url}/file/pdf-documents/application/${id}`

const applicationData = ref()
const moduleOptions = ref([])
const router = useRouter();


onBeforeMount(() => {
  getApplicationByIdForStatus(id)
    .then(data => {
     
      applicationData.value = data;
      return data;
    })
    .then(data => {
      return getModulesByCourse(data['courseLeipzig']['name'])
    })
    .then(modules => {
      moduleOptions.value = modules
    })
})


const moduleConnections = ref()

const openSummaryDocument = () => {
  window.open(summaryDocumentLink, '_blank')
}
const triggerSubmit = () => {
  putApplicationStudent(applicationData.value['id'], applicationData.value['courseLeipzig']['name'], moduleConnections.value)
    .then(_ => location.reload())
}
</script>

<template>
  <div class="main">
    
    <div v-if="applicationData" class="status-detail-container">
      
      <div v-if="applicationData['fullStatus'] === 'FORMFEHLER'">
        <FormalRejectionInfoBox />
      </div>

      <ApplicationOverview :creation-date="parseRequestDate(applicationData['creationDate'])"
        :last-edited-date="parseRequestDate(applicationData['lastEditedDate'])"
        :decision-date="parseRequestDate(applicationData['decisionDate'])" :id="applicationData['id']"
        :course="applicationData['courseLeipzig']['name']" :status="applicationData['fullStatus']" />



      <div v-for="connection in applicationData['modulesConnections']">

        <StatusPanel :connection="connection" :selectable-modules="moduleOptions"
          :readonly="!(applicationData['fullStatus'] === 'FORMFEHLER')" ref="moduleConnections"
          :class="{ 'formal-rejection-highlight': connection['formalRejection'] }" />

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
  @include verticalList(small);
  width: 100%;
  overflow: hidden;
}


.side-infos-container {
  @include sideInfoContainer();
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

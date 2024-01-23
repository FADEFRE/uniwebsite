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
import { getApplicationByIdForStatus, getModulesByCourse, putApplicationStandard } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import ButtonLink from '@/components/ButtonLink.vue';
import ErrorContainer from '../components/ErrorContainer.vue';

const id = useRoute().params.id
const summaryDocumentLink = `${url}/file/pdf-documents/application/${id}`

const applicationData = ref()
const moduleOptions = ref([])
const router = useRouter();

onBeforeMount(() => {
  getApplicationByIdForStatus(id)
    .then(data => {
      if (!data) {
        router.push({
          name: 'notFound'
        });
        return;
      }
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

const openSummaryDocument = () => {
  window.open(summaryDocumentLink, '_blank')
}
const redirectToHome = () => {
  router.push({ name: 'home' });
};

const triggerSubmit = () => {
  putApplicationStandard(applicationData.value['id'], applicationData.value['courseLeipzig']['name'], moduleConnections.value)
    .then(_ => location.reload())
}
</script>

<template>
  <div v-if="!applicationData" class="main">
      <ErrorContainer :customTitle="'Ungültige Vorgangsnummer'" :customMessage="'Bitte kehren Sie zur Startseite zurück.'">
        <ButtonLink @click="redirectToHome">Zur Startseite</ButtonLink>
      </ErrorContainer>
    </div>

  <div v-else class="main">



    <div class="status-detail-container">

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

      <ButtonLink v-if="applicationData['fullStatus'] === 'FORMFEHLER'" :primaryButton="true" @click="triggerSubmit">Neu
        einreichen</ButtonLink>

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
        <ul class="list-container">
          <li class="list-item">Antrag online stellen</li>
          <li class="list-item">Über Vorgangsnummer online Status einsehen</li>
          <li class="list-item">Auf Entscheidung des PAV warten</li>
          <li class="list-item">Mit abgeschlossenem Antrag zum Studienbüro gehen</li>
        </ul>
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
  @include verticalList(big);
  width: min-content;
}

.formal-rejection-highlight {
  border-left: 1rem solid $red;
}
</style>

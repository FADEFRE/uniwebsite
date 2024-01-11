<!--
shows status of an application
-->

<script setup>
import { useRoute } from 'vue-router'
import { ref, onBeforeMount } from "vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import CustomPanel from "@/components/CustomPanel.vue";
import PanelHeader from '../components/PanelHeader.vue';
import PanelExternalModules from "@/components/PanelExternalModules.vue";
import PanelInternalModules from "@/components/PanelInternalModules.vue";
import PanelComment from "@/components/PanelComment.vue";
import PanelDecisionBlock from "@/components/PanelDecisionBlock.vue";
import SideInfoContainer from '../components/SideInfoContainer.vue';
import { url } from "@/scripts/url-config"
import { getApplicationByIdForStatus, getModulesByCourse } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";

const id = useRoute().params.id
const summaryDocumentLink = `${url}/applications/pdf-data/${id}`

const applicationData = ref()
const moduleOptions = ref([])

onBeforeMount(() => {
  getApplicationByIdForStatus(id)
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
// todo error handling

const openSummaryDocument = () => {
  window.open(summaryDocumentLink, '_blank')
}
</script>

<template>
  <div class="main">

    <!-- request pending -->
    <div v-if="!applicationData">
      <p>Lade Daten ...</p>
    </div>

    <div v-else class="status-detail-container">

      <ApplicationOverview :creation-date="parseRequestDate(applicationData['creationDate'])"
        :last-edited-date="parseRequestDate(applicationData['lastEditedDate'])"
        :decision-date="parseRequestDate(applicationData['decisionDate'])" :id="applicationData['id']"
        :course="applicationData['courseLeipzig']['name']" :status="applicationData['fullStatus']" />


      <div v-for="connection in applicationData['modulesConnections']">

        <CustomPanel>

          <template #header>
            <PanelHeader :external-modules="connection['moduleApplications'].map(module => module['name'])"
              :internal-modules="connection['modulesLeipzig'].map(module => module['name'])" />
          </template>

          <template #icons>
            <img v-if="connection['decisionFinal'] === 'accepted'" src="../assets/icons/ModuleAccepted.svg">
            <img v-else-if="connection['decisionFinal'] === 'asExamCertificate'"
              src="../assets/icons/ModuleAsExamCertificate.svg">
            <img v-else-if="connection['decisionFinal'] === 'denied'" src="../assets/icons/ModuleDenied.svg">
          </template>

          <div>

            <PanelExternalModules type="edit" :modules-data="connection['moduleApplications']" />

            <PanelInternalModules type="edit" :options="moduleOptions"
              :selected-modules="connection['modulesLeipzig'].map(m => m.name)" />

            <PanelComment type="readonly" :comment="connection['commentApplicant']" />

            <PanelDecisionBlock v-if="connection['decisionFinal']" type="readonly"
              :display-decision="connection['decisionFinal']" :comment="connection['commentDecision']" />

          </div>

        </CustomPanel>

      </div>

      <Button @click="openSummaryDocument">
        Antrag herunterladen
        <img src="../assets/icons/Download.svg">
      </Button>

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
}


.side-infos-container {
  @include verticalList(big);
  width: min-content;
}
</style>

<!--
shows status of an application
-->

<script setup>
import { useRoute } from 'vue-router'
import { ref, computed, onBeforeMount } from "vue";
import {getApplicationByIdForStatus, getModulesByCourse} from "@/scripts/axios-requests";
import { url } from "@/scripts/url-config"
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import PanelHeader from '../components/PanelHeader.vue';
import PanelExternalModules from "@/components/PanelExternalModules.vue";
import PanelInternalModules from "@/components/PanelInternalModules.vue";
import PanelComment from "@/components/PanelComment.vue";

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
</script>

<template>
  <div>

    <!-- request pending -->
    <div v-if="!applicationData">
      <p>Lade Daten ...</p>
    </div>

    <div v-else>

      <ApplicationOverview
          :creation-date="applicationData.creationDate"
          :last-edited-date="undefined"
          :decision-date="applicationData['decisionDate']"
          :id="applicationData['id']"
          :course="applicationData['courseLeipzig']['name']"
          :status="applicationData['status']"
      />

      <div v-for="connection in applicationData['modulesConnections']">

        <Panel toggleable>

          <template #header>
            <PanelHeader
                :external-modules="connection['moduleApplications'].map(module => module['name'])"
                :internal-modules="connection['modulesLeipzig'].map(module => module['name'])"
            />
          </template>

          <template #icons>
            <img v-if="connection['decisionFinal'] === 'ANGENOMMEN'" src="../assets/icons/ModuleAccepted.svg">
            <img v-else-if="connection['decisionFinal'] === 'ÜBUNGSSCHEIN'" src="../assets/icons/ModuleAsExamCertificate.svg">
            <img v-else-if="connection['decisionFinal'] === 'ABGELEHNT'" src="../assets/icons/ModuleDenied.svg">
          </template>

          <div>

            <PanelExternalModules type="edit" :modules-data="connection['moduleApplications']" />
            <hr>
            <PanelInternalModules type="edit" :options="moduleOptions" :selected-modules="connection['modulesLeipzig'].map(m => m.name)" />
            <hr>
            <PanelComment type="readonly" :comment="connection['commentApplicant']" />

          </div>

        </Panel>

      </div>

    </div>

  </div>
</template>

<style scoped>

</style>

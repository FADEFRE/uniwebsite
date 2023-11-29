<!--
shows status of an application
displays:
- application id
- overall status
- course application is related to
- panels containing module application information
-->

<script setup>
import { useRoute } from 'vue-router'
import { ref, computed, onBeforeMount } from "vue";
import { url } from "@/scripts/url-config";
import axios from "axios";

const route = useRoute()

let applicationData = ref()
const id = ref()
onBeforeMount(() => {
  id.value = route.params.id
  axios.get(url + `/applications/student/${id.value}`)
      .then(response => {
        applicationData.value = response.data
        console.log(applicationData.value)
      })
  // todo error catching
})

// link to pdf document
const fileLinkBase = computed(() => `${url}/pdf-documents/`)
</script>

<template>
  <div class="view-container">

    <div v-if="applicationData">
      <div>

        <div>
          <h2>Vorgangsnummer: {{ id }}</h2>
          <p>Status: {{ applicationData['fullStatus'] }}</p>
          <p>Anrechnung für {{ applicationData['courseLeipzig']['name'] }} an der Universität Leipzig.</p>
        </div>

        <Panel
            toggleable
            collapsed
            v-for="moduleConnection in applicationData['modulesConnections']"
            class="module-panel"
        >

          <template #header>
            <h2>
              {{ moduleConnection['moduleApplication']['name'] }}
            </h2>
          </template>

          <!-- External Module -->
          <div>
            <h3>Anzurechnendes Modul:</h3>
            <!-- Module Name -->
            <div>
              <p>Modulname: {{ moduleConnection['moduleApplication']['name'] }}</p>
            </div>
            <!-- University -->
            <div>
              <p>Universität: {{ moduleConnection['moduleApplication']['university'] }}</p>
            </div>
            <!-- Credit Points -->
            <div>
              <p>Punkte: {{ moduleConnection['moduleApplication']['points'] }} {{ moduleConnection['moduleApplication']['pointSystem'] }}</p>
            </div>
            <!-- File Link -->
            <div>
              <a :href="fileLinkBase + moduleConnection['moduleApplication']['pdfDocument']['id']" target="_blank">
                Modulbeschreibung anzeigen
              </a>
            </div>
          </div>

          <!-- Internal Module -->
          <div>
            <h3>Module der Uni Leipzig:</h3>
            <!-- Internal Module List -->
            <div>
              <p v-for="internalModule in moduleConnection['modulesLeipzig']">{{ internalModule['moduleName'] }}</p>
            </div>
            <!-- Comment -->
            <div v-if="moduleConnection['moduleApplication']['commentApplicant'] !== 'undefined'">
              <p>Anmerkung: {{ moduleConnection['moduleApplication']['commentApplicant'] }}</p>
            </div>
          </div>

        </Panel>
      </div>

    </div>

  </div>
</template>

<style scoped>
.module-panel {
  margin: 10px;
}
</style>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, computed, onMounted, onBeforeMount } from "vue";
import { url } from "@/scripts/url-config";
import axios from "https://cdn.jsdelivr.net/npm/axios@1.3.5/+esm";

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

const fileLink = computed(() => `${url}/pdf-documents/${id.value}`)
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

        <Panel toggleable collapsed v-for="moduleConnection in applicationData['modulesConnections']" class="module-panel">

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
              <a :href="fileLink" target="_blank">Modulbeschreibung anzeigen</a>
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

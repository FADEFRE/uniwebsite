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
import { getApplicationByIdForStatus } from "@/scripts/axios-requests";
import { url } from "@/scripts/url-config"

const route = useRoute()

const applicationData = ref(undefined)
let id = undefined
let pdfDataLink = undefined
onBeforeMount(() => {
  id = route.params.id
  pdfDataLink = url + '/applications/pdf-data/' + id
  getApplicationByIdForStatus(id)
      .then(data => applicationData.value = data)
      .catch(error => {
        console.log(error)
        applicationData.value = 'error'
      })
})

// link to pdf document
const fileLinkBase = computed(() => `${url}/pdf-documents/`)
</script>

<template>
  <div>

    <!-- request pending -->
    <div v-if="!applicationData">
      <p>Lade Daten ...</p>
    </div>

    <!-- request rejected -->
    <div v-else-if="applicationData === 'error'">
      <p>Fehler bei der Datenabfrage!</p>
    </div>

    <!-- request resolved -->
    <div v-else class="view-container">

      <div v-if="applicationData">
        <div>

          <div>
            <h2>Vorgangsnummer: {{ id }}</h2>
            <p>Status: {{ applicationData['fullStatus'] }}</p>
            <p>Anrechnung für {{ applicationData['courseLeipzig']['name'] }} an der Universität Leipzig.</p>
            <p>Zusammenfassung als PDF: <a :href="pdfDataLink">Antrag {{ id }}</a></p>
          </div>

          <Panel
              toggleable
              collapsed
              v-for="moduleConnection in applicationData['modulesConnections']"
              class="module-panel"
          >

            <!-- Panel Header -->
            <template #header>
              <h2>
                {{ moduleConnection['moduleApplication']['name'] }}
                <span v-if="moduleConnection['asExamCertificate']" style="color: #8a8a8a">
                  (als Übungsschein)
                </span>
              </h2>
            </template>

            <!-- accept / reject Icons -->
            <template #icons v-if="moduleConnection['decisionFinal'] === 'ANGENOMMEN' || moduleConnection['decisionFinal'] === 'ABGELEHNT'">
              <div class="p-panel-header-icon">
                <div v-if="moduleConnection['decisionFinal'] === 'ANGENOMMEN' && !moduleConnection['asExamCertificate']">
                  <span class="pi pi-check-circle" style="color: green; font-size: 1.5rem"></span>
                </div>

                <div v-else-if="moduleConnection['decisionFinal'] === 'ANGENOMMEN' && moduleConnection['asExamCertificate']">
                  <span class="pi pi-info-circle" style="color: darkorange; font-size: 1.5rem"></span>
                </div>

                <div v-else-if="moduleConnection['decisionFinal'] === 'ABGELEHNT'">
                  <span class="pi pi-times-circle" style="color:red; font-size: 1.5rem"></span>
                </div>
              </div>
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
              <div v-if="moduleConnection['moduleApplication']['commentApplicant']">
                <p>Anmerkung: {{ moduleConnection['moduleApplication']['commentApplicant'] }}</p>
              </div>
            </div>

            <!-- Decision -->
            <template #footer v-if="applicationData['fullStatus'] === 'ABGESCHLOSSEN'">
              <div class="footer-container">
                <h3>Beschluss:</h3>
                <div class="decision-container">

                  <div class="icon-container">
                    <div v-if="moduleConnection['decisionFinal'] === 'ANGENOMMEN' && !moduleConnection['asExamCertificate']">
                      <span class="pi pi-check" style="color: green; font-size: 1.5rem"></span>
                    </div>

                    <div v-else-if="moduleConnection['decisionFinal'] === 'ANGENOMMEN' && moduleConnection['asExamCertificate']">
                      <span class="pi pi-info" style="color: darkorange; font-size: 1.5rem"></span>
                    </div>

                    <div v-else-if="moduleConnection['decisionFinal'] === 'ABGELEHNT'">
                      <span class="pi pi-times" style="color:red; font-size: 1.5rem;"></span>
                    </div>
                  </div>

                  <div class="comment-container">
                    <p>{{ moduleConnection['commentDecision'] }}</p>
                  </div>

                </div>
              </div>
            </template>

          </Panel>
        </div>

      </div>

    </div>

  </div>
</template>

<style scoped>
.module-panel {
  margin: 10px;
}

.p-panel-header-icon {
  margin: 10px;
}

.decision-container {
  display: inline-flex;
}

.icon-container > * {
  margin: 13px;
}
</style>

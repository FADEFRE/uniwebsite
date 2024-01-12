<!--
list of related module connections
props:
  - relatedModules (Array)
      each item should have properties: decision, decisionDate, university, course, externalModules, internalModules
displays:
  - list of related module connections
-->

<script setup>
import PanelHeader from "@/components/PanelHeader.vue";
import { ref, onBeforeMount } from "vue";
import { getRelatedModuleConnections } from "@/scripts/axios-requests";

const props = defineProps({
  connectionId : {
    required: true,
    type: Number
  }
})

const relatedModules = ref()

onBeforeMount(() => {
  getRelatedModuleConnections(props.connectionId)
      .then(data => relatedModules.value = data)
})
</script>

<template>
  <div class="panel-related-modules">
    <h3>Ähnliche Module:</h3>
    <div v-for="module in relatedModules">

      <div>
        <div v-if="module['decisionFinal'] === 'accepted'">
          <img src="../assets/icons/ModuleAccepted.svg">
        </div>
        <div v-else-if="module['decisionFinal'] === 'asExamCertificate'">
          <img src="../assets/icons/ModuleAsExamCertificate.svg">
        </div>
        <div v-else-if="module['decisionFinal'] === 'denied'">
          <img src="../assets/icons/ModuleDenied.svg">
        </div>
      </div>

      <PanelHeader
          :external-modules="module['moduleApplications'].map(m => m.name)"
          :internal-modules="module['modulesLeipzig'].map(m => m.name)"
      />
      <!-- todo PanelHeader should link -->

      <div>
        <img src="../assets/icons/DecisionDate.svg">
        <p>{{ module['application']['decisionDate'] }}</p>
      </div>

      <p>{{ module['application']['university'] }}</p>

      <p>{{ module['application']['courseLeipzig']['name'] }}</p>

    </div>
  </div>
</template>

<style scoped lang="scss">
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';

.panel-related-modules {
  @include panelComponent();
}
</style>
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
import { useRoute } from "vue-router";
import { ref, onBeforeMount } from "vue";
import { getRelatedModuleConnections } from "@/scripts/axios-requests";
import { parseRequestDate } from "@/scripts/date-utils";
import router from "@/router";


//TODO: FIX Related Modules!!!!!! 
const props = defineProps({
  connectionId: {
    required: true,
    type: Number
  }
})

const relatedModules = ref()

onBeforeMount(() => {
  getRelatedModuleConnections(props.connectionId)
    .then(data => relatedModules.value = data)
})

const route = useRoute()
const type = route.meta['authType']

let redirectRouteName = undefined
if (type === 'study-office') {
  redirectRouteName = 'studyOfficeDetailHighlight'
} else if (type === 'chairman') {
  redirectRouteName = 'chairmanDetailHighlight'
} else {
  console.warn("PanelRelatedModules: component should only be used with route types 'study-office' or 'chairman'")
}

const openRelatedModule = (singleModule) => {
  const connectionId = singleModule['id']
  const routeData = router.resolve({ name: redirectRouteName, params: { id: singleModule['application']['id'], connection: connectionId } })
  window.open(routeData.href, '_blank')
}

</script>

<template>
  <div class="panel-related-modules">

    <h4>Ähnliche Module:</h4>

    <div v-if="relatedModules && relatedModules.length > 0" class="related-modules-list">
      <div v-for="relatedModule in relatedModules" class="related-module-container"
        @click="openRelatedModule(relatedModule)">

        <PanelHeader v-if="relatedModule['externalModules'] && relatedModule['modulesLeipzig']"
          :external-modules="relatedModule['externalModules'].map(m => m.name)"
          :internal-modules="relatedModule['modulesLeipzig'].map(m => m.name)" :relatedModules="true"
          class="application-header" />

        <div class="application-info">
          <div class="date-block">
            <img src="../assets/icons/DecisionDate.svg" alt="DecisionDate">
            <p class="info-text">{{ parseRequestDate(relatedModule['application']['decisionDate']) }}</p>
          </div>

          <p class="info-text">{{ relatedModule['application']['courseLeipzig']['name'] }}</p>

          <div v-if="relatedModule['decisionFinal'] === 'accepted'">
            <img src="../assets/icons/ModuleAccepted.svg">
          </div>
          <div v-else-if="relatedModule['decisionFinal'] === 'asExamCertificate'">
            <img src="../assets/icons/ModuleAsExamCertificate.svg">
          </div>
          <div v-else-if="relatedModule['decisionFinal'] === 'denied'">
            <img src="../assets/icons/ModuleDenied.svg">
          </div>
        </div>

      </div>

    </div>

    <div v-else>
      <p>Es wurden keine ähnlichen Module gefunden.</p>
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;

.panel-related-modules {
  @include panelComponent();
}

.related-modules-list {
  @include verticalList(small);
}

.related-module-container {
  @include smallHighlightBox();
  @include verticalListItem($gray);
  flex-wrap: wrap-reverse;
}

.application-header {
  width: 60%;
}

.application-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.date-block {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.info-text {
  font-size: 0.9rem;
}
</style>
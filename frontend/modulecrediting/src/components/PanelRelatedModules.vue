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

const openRelatedModule = (module) => {
  const connectionId = module['id']
  const routeData = router.resolve({ name: redirectRouteName, params: { id: module['application']['id'], connection: connectionId } })
  window.open(routeData.href, '_blank')
}

</script>

<template>
  <div class="panel-related-modules">

    <h4>Ähnliche Module:</h4>

    <div v-if="relatedModules && relatedModules.length > 0" class="related-modules-list-container">
      <div v-for="module in relatedModules" class="single-related-module-container" @click="openRelatedModule(module)">

        <div v-if="module['decisionFinal'] === 'accepted'">
          <img src="../assets/icons/ModuleAccepted.svg">
        </div>
        <div v-else-if="module['decisionFinal'] === 'asExamCertificate'">
          <img src="../assets/icons/ModuleAsExamCertificate.svg">
        </div>
        <div v-else-if="module['decisionFinal'] === 'denied'">
          <img src="../assets/icons/ModuleDenied.svg">
        </div>
        <PanelHeader :external-modules="module['externalModules'].map(m => m.name)"
          :internal-modules="module['modulesLeipzig'].map(m => m.name)" :relatedModules="true" />

        <div class="date-block">
          <img src="../assets/icons/DecisionDate.svg" alt="DecisionDate">
          <p>{{ parseRequestDate(module['application']['decisionDate']) }}</p>
        </div>

        <p class="course">{{ module['application']['courseLeipzig']['name'] }}</p>
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

.related-modules-list-container {
  @include verticalList(small);
  width: 100%;
  overflow: hidden;
}

.single-related-module-container {
  @include verticalListItem($gray);

  width: 100%;
  padding: 0.5rem 1rem;

  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;

  overflow: hidden;
}

.left-side,
.right-side {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.date-block {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.course {
}
</style>
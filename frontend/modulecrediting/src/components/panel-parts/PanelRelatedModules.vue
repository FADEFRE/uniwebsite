<script setup>
import router from "@/router";
import { useRoute } from "vue-router";
import { ref, onBeforeMount } from "vue";
import { parseRequestDate } from "@/utils/date-utils";
import DateIcon from "@/assets/icons/DateIcon.vue";
import PanelHeader from "@/components/panel-parts/PanelHeader.vue";
import ModuleStatusIcon from "@/assets/icons/ModuleStatusIcon.vue";
import { getRelatedModuleConnections } from "@/requests/application-requests";

/*
displays list of related module connections to a specified connection (specified by prop connectionId)
 */

const props = defineProps({
  /* module connection id of connection to display related to */
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
  redirectRouteName = 'studyOfficeDetail'
} else if (type === 'chairman') {
  redirectRouteName = 'chairmanDetail'
} else {
  console.warn("PanelRelatedModules: component should only be used with route types 'study-office' or 'chairman'")
}

const openRelatedModule = (singleModule) => {
  const connectionId = singleModule['id']
  const routeData = router.resolve({
    name: redirectRouteName,
    params: { id: singleModule['application']['id'] },
    query: { highlight: connectionId }
  })
  window.open(routeData.href, '_blank')
}
</script>

<template>
  <div class="panel-container">

    <h4>Ähnliche Module:</h4>

    <div v-if="relatedModules && relatedModules.length > 0" class="related-modules-list">
      <div v-for="relatedModule in relatedModules" class="related-module-container"
        @click="openRelatedModule(relatedModule)">

        <div class="main-info">
          <ModuleStatusIcon :status-decision="relatedModule['decisionFinal']" size="small"/>

        <PanelHeader v-if="relatedModule['externalModules'] && relatedModule['modulesLeipzig']"
          :external-modules="relatedModule['externalModules'].map(m => m.name)"
          :internal-modules="relatedModule['modulesLeipzig'].map(m => m.name)" :relatedModules="true" />
        </div>
        

        <div class="additional-info">
          <div class="date-block">
            <DateIcon type="decision"/>
            <p class="info-text">{{ parseRequestDate(relatedModule['application']['decisionDate']) }}</p>
          </div>

          <p class="info-text">{{ relatedModule['application']['courseLeipzig']['name'] }}</p>
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
@use '@/assets/styles/components' as *;

.related-modules-list {
  @include verticalList(s);
}

.related-module-container {
  @include smallHighlightBox();
  @include verticalListItem($gray);
  
  @include breakpoint(m) {
    flex-direction: column-reverse;
    align-items: flex-start;
    gap: spacing(m);
  }
  
}

.main-info {
  width: 70%;
  display: flex;
  gap: spacing(m);

  @include breakpoint(m) {
    width: 100%;
  }
}

.additional-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: spacing(m);
}

.date-block {
  display: flex;
  align-items: center;
  gap: spacing(s);
}

.info-text {
  font-size: 0.9rem;
}
</style>
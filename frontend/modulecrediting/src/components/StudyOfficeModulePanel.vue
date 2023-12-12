<!--
documentation todo
-->

<script setup>
import AdministrativeModulePanel from "@/components/AdministrativeModulePanel.vue";
import AdministrativePanelSection from "@/components/AdministrativePanelSection.vue";
import { ref } from "vue";
import RelatedModuleConnections from "@/components/RelatedModuleConnections.vue";

const props = defineProps(['moduleConnectionData', 'internalModuleOptions'])
const studyOfficeData = {
  decision: props.moduleConnectionData.decisionSuggestion,
  comment: props.moduleConnectionData.commentStudyOffice
}

const general = ref()
const studyOffice = ref()

defineExpose({
  general, studyOffice
})
</script>

<template>
  <div>
    <AdministrativeModulePanel
      :module-connection-data="props.moduleConnectionData"
      :internal-module-options="props.internalModuleOptions"
      ref="general"
    >
      <template #relatedModuleConnections>
        <RelatedModuleConnections :module-connection-id="props.moduleConnectionData.id" />
      </template>
      <template #studyOffice>
        <AdministrativePanelSection :data="studyOfficeData" ref="studyOffice" />
      </template>
    </AdministrativeModulePanel>
  </div>
</template>

<style scoped>

</style>
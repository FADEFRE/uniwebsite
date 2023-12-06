<script setup>
import AdministrativeModulePanel from "@/components/AdministrativeModulePanel.vue";
import AdministrativePanelSection from "@/components/AdministrativePanelSection.vue";
import { ref } from "vue";

const props = defineProps(['moduleConnectionData', 'internalModuleOptions'])
const studyOfficeData = {
  decision: props.moduleConnectionData.decisionSuggestion,
  comment: props.moduleConnectionData.commentStudyOffice
}
const chairmanData = {
  decision: props.moduleConnectionData.decisionFinal,
  comment: props.moduleConnectionData.commentDecision
}

const general = ref()
const studyOffice = ref()
const chairman = ref()

defineExpose({
  general, chairman
})
</script>

<template>
  <div>

    <AdministrativeModulePanel
      :module-connection-data="props.moduleConnectionData"
      :internal-module-options="props.internalModuleOptions"
      ref="general"
    >

      <template #studyOffice>
        <AdministrativePanelSection :read-only="true" :data="studyOfficeData" ref="studyOffice" />
      </template>

      <template #chairman>
        <AdministrativePanelSection :data="chairmanData" ref="chairman" />
      </template>

    </AdministrativeModulePanel>

  </div>
</template>

<style scoped>

</style>
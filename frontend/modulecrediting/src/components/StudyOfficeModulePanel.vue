<!--
documentation todo
-->

<script setup>
import AdministrativeModulePanel from "@/components/AdministrativeModulePanel.vue";
import StudyOfficePanelBlock from "@/components/StudyOfficePanelBlock.vue";
import { ref } from "vue";

const props = defineProps(['moduleConnectionData', 'internalModuleOptions'])
const studyOfficeData = {
  decisionSuggestion: props.moduleConnectionData.decisionSuggestion,
  commentStudyOffice: props.moduleConnectionData.commentStudyOffice
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
      <template #studyOffice>
        <StudyOfficePanelBlock :data="studyOfficeData" ref="studyOffice" />
      </template>
    </AdministrativeModulePanel>
  </div>
</template>

<style scoped>

</style>
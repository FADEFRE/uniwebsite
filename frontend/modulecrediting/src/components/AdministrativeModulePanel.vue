<!--
documentation todo
-->

<script setup>
import PanelBase from "@/components/PanelBase.vue";
import PanelStudyOfficeFile from "@/components/PanelStudyOfficeFile.vue";
import PanelBaseInternalModules from "@/components/PanelBaseInternalModules.vue";
import PanelComment from "@/components/PanelComment.vue";
import { ref, onMounted, useSlots } from "vue";

// props
const props = defineProps(['moduleConnectionData', 'internalModuleOptions'])
const commentApplicant = props.moduleConnectionData.moduleApplication.commentApplicant ? props.moduleConnectionData.moduleApplication.commentApplicant : '-'

// refs
const base = ref()
const internalModules = ref()
const decisionSuggestion = ref()
const decisionSuggestionOptions = ref(['Ablehnen', 'Annehmen'])
const commentStudyOffice = ref()

// setting decision suggestion
if (props.moduleConnectionData.decisionSuggestion === 'ANGENOMMEN') {
  decisionSuggestion.value = 'Annehmen'
} else {
  if (props.moduleConnectionData.decisionSuggestion === 'ABGELEHNT') {
    decisionSuggestion.value = 'Ablehnen'
  }
}

onMounted(() => {
  internalModules.value.setup(
      props.internalModuleOptions,  // options
      props.moduleConnectionData.modulesLeipzig.map(module => module.moduleName)  // initials
  )
})

defineExpose({
  base, internalModules, decisionSuggestion, commentStudyOffice
})
</script>

<template>
  <div>
    <Panel toggleable class="module-application-panel">

      <template #header>
        <h2>{{ base?.moduleName }}</h2>
      </template>

      <div>
        <PanelBase
          :module-name="moduleConnectionData.moduleApplication.name"
          :university="moduleConnectionData.moduleApplication.university"
          :credit-points="moduleConnectionData.moduleApplication.points"
          :point-system="moduleConnectionData.moduleApplication.pointSystem"
          ref="base"
        >

          <template #file>
            <PanelStudyOfficeFile
                :file-id="moduleConnectionData.moduleApplication.pdfDocument.id"
                :file-name="moduleConnectionData.moduleApplication.pdfDocument.name"
                ref="file"
            />
          </template>

          <template #internalModules>
            <PanelBaseInternalModules :use-initials="true" ref="internalModules" />
          </template>

          <template #comment>
            <PanelComment :comment="commentApplicant" ref="comment"/>
          </template>

        </PanelBase>
      </div>

      <hr>

      <slot name="studyOffice" />

      <hr>

      <slot name="chairman" />

    </Panel>
  </div>
</template>

<style scoped>
.module-application-panel {
  margin: 10px;
}
</style>
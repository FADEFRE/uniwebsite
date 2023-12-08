<!--
documentation todo
-->

<script setup>
import PanelBase from "@/components/PanelBase.vue";
import PanelStudyOfficeFile from "@/components/PanelStudyOfficeFile.vue";
import PanelBaseInternalModules from "@/components/PanelBaseInternalModules.vue";
import PanelComment from "@/components/PanelComment.vue";
import { ref, computed, onMounted } from "vue";

// props
const props = defineProps(['moduleConnectionData', 'internalModuleOptions'])
const commentApplicant = props.moduleConnectionData.moduleApplication.commentApplicant ? props.moduleConnectionData.moduleApplication.commentApplicant : '-'

// refs
const base = ref()
const internalModules = ref()
const asExamCertificate = ref(props.moduleConnectionData.asExamCertificate)
const decisionSuggestion = ref()
const decisionSuggestionOptions = ref(['Ablehnen', 'Annehmen'])
const commentStudyOffice = ref()

const headerName = computed(() => {
  if (props.moduleConnectionData.asExamCertificate) {
    return base.value?.moduleName + ' <style color=yellow>(als Übungsschein)</style>'
  } else {
    return base.value?.moduleName
  }
})

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
  base, internalModules, asExamCertificate, decisionSuggestion, commentStudyOffice
})
</script>

<template>
  <div>
    <Panel toggleable class="module-application-panel">

      <template #header>
        <h2>
          {{ base?.moduleName }}
          <span v-if="props.moduleConnectionData.asExamCertificate" style="color: #d8413f">
            (als Übungsschein)
          </span>
        </h2>
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

        <div class="checkbox-container">
          <Checkbox v-model="asExamCertificate" :binary="true" class="checkbox" />
          <p>als Übungsschein anrechnen</p>
        </div>

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

.checkbox-container {
  display: inline-flex;
  justify-content: center;
  align-items: center;
}

.checkbox {
  margin: 5px;
}
</style>
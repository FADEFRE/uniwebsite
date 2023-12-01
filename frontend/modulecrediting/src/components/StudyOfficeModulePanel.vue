<!--
documentation todo
-->

<script setup>
import PanelBase from "@/components/PanelBase.vue";
import PanelStudyOfficeFile from "@/components/PanelStudyOfficeFile.vue";
import PanelBaseInternalModules from "@/components/PanelBaseInternalModules.vue";
import PanelCommentReadOnly from "@/components/PanelCommentDisplayOnly.vue";
import { ref, computed, onMounted } from "vue";
import PanelCommentWriteOnly from "@/components/PanelCommentWriteOnly.vue";

const props = defineProps(['moduleConnectionData', 'internalModuleOptions'])

const internalModules = ref()

onMounted(() => {
  internalModules.value.setup(
      props.internalModuleOptions,  // options
      props.moduleConnectionData.modulesLeipzig.map(module => module.moduleName)  // initials
  )
})

const decision = ref()
const decisionOptions = ref(['Ablehnen', 'Annehmen'])
const decisionStyle = computed(() => {
  if (decision.value === 'Ablehnen') {
    return 'reject-color'
  } else {
    return 'accept-color'
  }
})

const studyOfficeComment = ref()
</script>

<template>
  <div>
    <Panel toggleable class="module-application-panel">

      <template #header>
        <h2>{{ moduleConnectionData.moduleApplication.name }}</h2>
      </template>

      <div>
        <PanelBase
          :module-name="moduleConnectionData.moduleApplication.name"
          :university="moduleConnectionData.moduleApplication.university"
          :credit-points="moduleConnectionData.moduleApplication.points"
          :point-system="moduleConnectionData.moduleApplication.pointSystem"
        >

          <template #file>
            <PanelStudyOfficeFile
                :file-id="moduleConnectionData.moduleApplication.pdfDocument.id"
                :file-name="moduleConnectionData.moduleApplication.pdfDocument.name"
            />
          </template>

          <template #internalModules>
            <PanelBaseInternalModules :use-initials="true" ref="internalModules" />
          </template>

          <template #comment>
            <PanelCommentReadOnly :applicant-comment="moduleConnectionData.moduleApplication.commentApplicant"/>
          </template>

        </PanelBase>
      </div>

      <hr>

      <div class="study-office-container">
        <div>
          <SelectButton :allow-empty="false" v-model="decision" :options="decisionOptions" :class="decisionStyle" />
        </div>
        <div>
          <PanelCommentWriteOnly ref="studyOfficeComment" />
        </div>
      </div>

    </Panel>
  </div>
</template>

<style scoped>
.module-application-panel {
  margin: 10px;
}
</style>
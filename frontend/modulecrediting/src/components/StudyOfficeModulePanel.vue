<!--
documentation todo
-->

<script setup>
import PanelBase from "@/components/PanelBase.vue";
import PanelStudyOfficeFile from "@/components/PanelStudyOfficeFile.vue";
import PanelBaseInternalModules from "@/components/PanelBaseInternalModules.vue";
import PanelStudyOfficeComment from "@/components/PanelStudyOfficeComment.vue";
import { onMounted, ref } from "vue";

const props = defineProps(['moduleConnectionData', 'internalModuleOptions'])

const internalModules = ref()

onMounted(() => {
  internalModules.value.setup(
      props.internalModuleOptions,  // options
      props.moduleConnectionData.modulesLeipzig.map(module => module.moduleName)  // initials
  )
})
</script>

<template>
  <div>
    <Panel toggleable class="module-application-panel">

      <template #header>
        <h2>{{ moduleConnectionData.moduleApplication.name }}</h2>
      </template>

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
          <PanelStudyOfficeComment :applicant-comment="moduleConnectionData.moduleApplication.commentApplicant"/>
        </template>

      </PanelBase>

    </Panel>
  </div>
</template>

<style scoped>
.module-application-panel {
  margin: 10px;
}
</style>
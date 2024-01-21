<script setup>
import PanelComment from "@/components/PanelComment.vue";
import PanelHeader from "@/components/PanelHeader.vue";
import CustomPanel from "@/components/CustomPanel.vue";
import PanelInternalModules from "@/components/PanelInternalModules.vue";
import PanelDecisionBlock from "@/components/PanelDecisionBlock.vue";
import PanelExternalModules from "@/components/PanelExternalModules.vue";
import PanelFormalRejectionBlock from "@/components/PanelFormalRejectionBlock.vue";
import PanelDecision from "@/components/PanelDecision.vue";

const props = defineProps({
  readonly: {
    required: true,
    type: Boolean
  },
  selectableModules: {
    required: true,
    type: Array
  },
  connection: {
    required: true,
    type: Object,
    // todo validator
  }
})
</script>

<template>
  <CustomPanel>

    <template #header>
      <PanelHeader
          :external-modules="connection['externalModules'].map(module => module['name'])"
          :internal-modules="connection['modulesLeipzig'].map(module => module['name'])"
      />
    </template>

    <template #icons>
      <img v-if="connection['decisionFinal'] === 'accepted'" src="@/assets/icons/ModuleAccepted.svg">
      <img v-else-if="connection['decisionFinal'] === 'asExamCertificate'" src="@/assets/icons/ModuleAsExamCertificate.svg">
      <img v-else-if="connection['decisionFinal'] === 'denied'" src="@/assets/icons/ModuleDenied.svg">
    </template>

    <div>
      <PanelExternalModules
          :type="readonly ? 'readonly' : 'edit-full'"
          :modules-data="connection['externalModules']"
      />
      <PanelInternalModules
          :type="readonly ? 'readonly' : 'edit'"
          :options="selectableModules"
          :selected-modules="connection['modulesLeipzig'].map(m => m.name)"
      />
      <PanelComment
          v-if="connection['commentApplicant'] || readonly === false"
          :type="readonly ? 'readonly' : 'edit'"
          :comment="connection['commentApplicant']"
      />
      <PanelDecision type="single">
        <PanelFormalRejectionBlock
            v-if="connection['formalRejection']"
            type="readonly"
            :comment="connection['formalRejectionComment']"
        />
        <PanelDecisionBlock
            v-if="connection['decisionFinal'] !== 'unedited'"
            type="readonly"
            :display-decision="connection['decisionFinal']"
            :comment="connection['commentDecision']"
        />
      </PanelDecision>
    </div>

  </CustomPanel>
</template>

<style scoped lang="scss">

</style>
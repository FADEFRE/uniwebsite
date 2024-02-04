<script setup>
import PanelComment from "@/components/PanelComment.vue";
import PanelHeader from "@/components/PanelHeader.vue";
import CustomPanel from "@/components/CustomPanel.vue";
import PanelInternalModules from "@/components/PanelInternalModules.vue";
import PanelDecisionBlock from "@/components/PanelDecisionBlock.vue";
import PanelExternalModules from "@/components/PanelExternalModules.vue";
import PanelFormalRejectionBlock from "@/components/PanelFormalRejectionBlock.vue";
import PanelDecision from "@/components/PanelDecision.vue";
import { ref, computed } from "vue";

const props = defineProps({
  readonly: {
    required: true,
    type: Boolean
  },
  allowDelete: {
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

const emit = defineEmits(['deleteSelf'])

const id = props.connection['id']

const panelExternalModules = ref()
const panelInternalModules = ref()
const panelComment = ref()

const externalModules = computed(() => panelExternalModules.value?.externalModules)
const internalModules = computed(() => panelInternalModules.value?.selectedModules)
const commentApplicant = computed(() => panelComment.value?.comment)

const panelRef = ref()

const checkValidity = () => {
  const validity = panelExternalModules.value.checkValidity()
  panelRef.value.setCollapsed(validity)
  return validity
}

defineExpose({
  id,
  externalModules,
  internalModules,
  commentApplicant,
  checkValidity
})
</script>

<template>
  <CustomPanel ref="panelRef">

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
      <img v-if="allowDelete" src="@/assets/icons/Trash.svg" @click="emit('deleteSelf')" class="trash-icon">
    </template>

    <div>
      <PanelExternalModules
          :allow-text-edit="!readonly"
          :allow-file-edit="!readonly"
          :allow-delete="!readonly"
          :allow-add="!readonly"
          :modules-data="connection['externalModules']"
          ref="panelExternalModules"
      />
      <PanelInternalModules
          :allow-select="!readonly"
          :allow-delete="!readonly"
          :options="selectableModules"
          :selected-modules="connection['modulesLeipzig'].map(m => m.name)"
          ref="panelInternalModules"
      />
      <PanelComment
          v-if="connection['commentApplicant'] || readonly === false"
          :readonly="readonly"
          :comment="connection['commentApplicant']"
          ref="panelComment"
      />
      <PanelDecision type="single">
        <div v-if="!readonly">
          <PanelFormalRejectionBlock
              v-if="connection['formalRejection']"
              :readonly="true"
              :comment="connection['formalRejectionComment']"
          />
        </div>
        <div v-else>
          <PanelDecisionBlock
              v-if="connection['decisionFinal'] !== 'unedited'"
              :readonly="true"
              :display-decision="connection['decisionFinal']"
              :comment="connection['commentDecision']"
          />
          <p v-else>Es wurde noch keine Entscheidung getroffen.</p>
        </div>
      </PanelDecision>
    </div>

  </CustomPanel>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';



.trash-icon {
  @include trashIconAnimation();
}
</style>
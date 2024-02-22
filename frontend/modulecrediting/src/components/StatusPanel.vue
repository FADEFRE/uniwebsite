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
import ModuleStatusIcon from "../assets/icons/ModuleStatusIcon.vue";
import TrashIcon from "../assets/icons/TrashIcon.vue";

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
      <PanelHeader :external-modules="connection['externalModules'].map(singleModule => singleModule['name'])"
        :internal-modules="connection['modulesLeipzig'].map(singleModule => singleModule['name'])" />
    </template>

    <template #icons>
      <ModuleStatusIcon v-if="connection['decisionFinal'] !== 'unedited'" :status-decision="connection['decisionFinal']" />
      <TrashIcon v-if="allowDelete" @click="emit('deleteSelf')" background-color="white"/>
    </template>

    <div>
      <PanelExternalModules :allow-text-edit="!readonly" :allow-file-edit="!readonly" :allow-delete="!readonly"
        :allow-add="!readonly" :modules-data="connection['externalModules']" ref="panelExternalModules" />
      <PanelInternalModules :allow-select="!readonly" :allow-delete="!readonly" :options="selectableModules"
        :selected-modules="connection['modulesLeipzig'].map(m => m.name)" ref="panelInternalModules" />
      <PanelComment v-if="connection['commentApplicant'] || readonly === false" :readonly="readonly"
        :comment="connection['commentApplicant']" ref="panelComment" />
      <PanelDecision type="single">
        <div v-if="!readonly">
          <PanelFormalRejectionBlock v-if="connection['formalRejection']" :readonly="true"
            :comment="connection['formalRejectionComment']" />
        </div>
        <div v-else>
          <PanelDecisionBlock v-if="connection['decisionFinal'] !== 'unedited'" :readonly="true"
            :display-decision="connection['decisionFinal']" :comment="connection['commentDecision']" />
          <p v-else>Es wurde noch keine Entscheidung getroffen.</p>
        </div>
      </PanelDecision>
    </div>

  </CustomPanel>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>
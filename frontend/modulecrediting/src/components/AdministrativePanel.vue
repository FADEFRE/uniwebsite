<!--
panel representing one module connection, usable for administrative views
props:
  - selectableModules (array of internal modules that could be selected
  - connectionData (element of API responses moduleConnections)
exposes:
  - externalModules (array of objects describing externalModules
  - internalModules (array module names)
  - applicantComment (String)
displays:
  - toggleable panel with dynamic header
  - external module block, internal module block, comment block and decision block as panel content
-->

<script setup>
import { ref, computed } from "vue";
import PanelHeader from "@/components/PanelHeader.vue";
import PanelComment from "@/components/PanelComment.vue";
import PanelExternalModules from "@/components/PanelExternalModules.vue";
import PanelInternalModules from "@/components/PanelInternalModules.vue";
import PanelStatusIcons from "@/components/PanelStatusIcons.vue";
import PanelDecision from "@/components/PanelDecision.vue";
import PanelDecisionBlock from "@/components/PanelDecisionBlock.vue";
import CustomPanel from "@/components/CustomPanel.vue";
import PanelRelatedModules from "@/components/PanelRelatedModules.vue";
import PanelFormalRejectionBlock from "@/components/PanelFormalRejectionBlock.vue";

const props = defineProps({
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['study-office', 'chairman'].includes(value)
    }
  },
  readonly: {
    required: true,
    type: Boolean
  },
  selectableModules: {
    required: true,
    type: Array
  },
  connectionData: {
    required: true,
    type: Object,
    // todo validator
  }
})

const emit = defineEmits(['change'])

const id = props.connectionData['id']

const decisionSuggestion = props.connectionData['decisionSuggestion']
const decisionFinal = props.connectionData['decisionFinal']

const panelExternalModules = ref()
const panelInternalModules = ref()

const externalModules = computed(() => panelExternalModules.value?.externalModules)
const internalModules = computed(() => panelInternalModules.value?.selectedModules)

const formalRejection = ref(props.connectionData['formalRejection'])
const formalRejectionRef = ref()
const formalRejectionData = computed(() => {
  return {
    formalRejection: formalRejection.value,
    comment: formalRejectionRef.value?.comment ? formalRejectionRef.value?.comment : ''
  }
})

const setFormalRejection = () => {
  formalRejection.value = true;
  emit('change')
}

const unsetFormalRejection = () => {
  formalRejection.value = false;
  emit('change')
}

const studyOfficeDecisionData = ref()
const chairmanDecisionData = ref()

const panel = ref()

const setCollapsed = (value) => {
  panel.value.setCollapsed(value)
}

const checkValidity = () => {
  const validity = panelExternalModules.value.checkValidity()
  panel.value.setCollapsed(validity)
  return validity
}

defineExpose({
  id,
  externalModules,
  internalModules,
  formalRejectionData,
  studyOfficeDecisionData,
  chairmanDecisionData,
  setCollapsed,
  checkValidity
})
</script>

<template>
  <CustomPanel ref="panel">

    <template #header>
      <PanelHeader :external-modules="externalModules?.map(m => m.name).filter(name => name !== '')"
        :internal-modules="internalModules" />
    </template>

    <template #icons>
      <PanelStatusIcons :formal-rejection="formalRejection" :decision-suggestion="decisionSuggestion"
        :decision-final="decisionFinal" />
    </template>

    <PanelExternalModules :allow-text-edit="!readonly" :allow-file-edit="false" :allow-delete="false" :allow-add="false"
      :modules-data="connectionData['externalModules']" ref="panelExternalModules" @change="emit('change')" />
    <PanelInternalModules :allow-select="!readonly" :allow-delete="!readonly"
      :selected-modules="connectionData['modulesLeipzig'].map(m => m['name'])" :options="selectableModules"
      ref="panelInternalModules" @change="emit('change')" />
    <PanelComment :readonly="true" :comment="connectionData['commentApplicant']" />
    <PanelRelatedModules :connection-id="connectionData['id']" />

    <div v-if="formalRejection">
      <PanelDecision type="single">
        <PanelFormalRejectionBlock :readonly="readonly" :comment="connectionData['formalRejectionComment']"
          ref="formalRejectionRef" />
      </PanelDecision>
      <Button v-if="!readonly && type === 'study-office'" @click="unsetFormalRejection">Formfehler zurücknehmen</Button>
    </div>

    <div v-else>
      <PanelDecision type="study-office-chairman">
        <template #study-office>
          <PanelDecisionBlock :readonly="type !== 'study-office' || readonly" :display-decision="decisionSuggestion"
            :comment="connectionData['commentStudyOffice']" ref="studyOfficeDecisionData" @change="emit('change')" />
        </template>
        <template #chairman>
          <PanelDecisionBlock :readonly="type !== 'chairman' || readonly" :display-decision="decisionFinal"
            :comment="connectionData['commentDecision']" ref="chairmanDecisionData" @change="emit('change')" />
        </template>
      </PanelDecision>
      <Button v-if="!readonly && type === 'study-office'" @click="setFormalRejection">Als Formfehler markieren</Button>
    </div>

  </CustomPanel>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.connection-highlight {
  border-left: 1rem solid $dark-gray;
}
</style>

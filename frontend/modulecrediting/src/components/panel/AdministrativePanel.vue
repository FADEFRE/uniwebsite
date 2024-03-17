<script setup>
import { ref, computed } from "vue";
import PanelHeader from "@/components/panel-parts/PanelHeader.vue";
import PanelComment from "@/components/panel-parts/PanelComment.vue";
import PanelExternalModules from "@/components/panel-parts/PanelExternalModules.vue";
import PanelInternalModules from "@/components/panel-parts/PanelInternalModules.vue";
import PanelStatusIcons from "@/components/panel-parts/PanelStatusIcons.vue";
import PanelDecision from "@/components/panel-parts/PanelDecision.vue";
import PanelDecisionBlock from "@/components/panel-parts/PanelDecisionBlock.vue";
import CustomPanel from "@/components/panel/CustomPanel.vue";
import PanelRelatedModules from "@/components/panel-parts/PanelRelatedModules.vue";
import PanelFormalRejectionBlock from "@/components/panel-parts/PanelFormalRejectionBlock.vue";

/*
panel to display a module connection for internal use
allows edit of all data except description pdfs and applicant comment
contains decision section
 */

const props = defineProps({
  /* 'study-office' or 'chairman' */
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['study-office', 'chairman'].includes(value)
    }
  },
  /* controls if editable */
  readonly: {
    required: true,
    type: Boolean
  },
  /* array of Strings, module names that should be selectable as internal modules */
  selectableModules: {
    required: true,
    type: Array
  },
  /* data of module connection to display / edit, should contain ...
  *  Number id, array of objects externalModules, array of Strings modulesLeipzig, String commentApplicant
  *  String decisionSuggestion, String commentStudyOffice, String decisionFinal, String commentDecision,
  *  Boolean formalRejection, String formalRejectionComment */
  connectionData: {
    required: true,
    type: Object,
  },
  /* controls if related modules section should be shown */
  showRelatedConnections: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits([
  /* emitted when any data changes */
  'change'
])

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
  /* connection id */
  id,
  /* PanelExternalModules ref, see expose of PanelExternalModules */
  externalModules,
  /* PanelInternalModules ref, see expose of PanelInternalModules */
  internalModules,
  /* object containing Boolean formalRejection, String comment */
  formalRejectionData,
  /* PanelDecisionBlock ref, see expose of PanelDecisionBlock */
  studyOfficeDecisionData,
  /* PanelDecisionBlock ref, see expose of PanelDecisionBlock */
  chairmanDecisionData,
  /* function (Boolean value) to set panel collapsed value */
  setCollapsed,
  /* function () to check data validity, cascades call */
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
    <PanelComment v-if="connectionData['commentApplicant']" :readonly="true" :comment="connectionData['commentApplicant']" />
    <PanelRelatedModules v-if="showRelatedConnections" :connection-id="connectionData['id']" />

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

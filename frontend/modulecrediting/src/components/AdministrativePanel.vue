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
import PanelHeader from "@/components/PanelHeader.vue";
import PanelComment from "@/components/PanelComment.vue";
import PanelExternalModules from "@/components/PanelExternalModules.vue";
import PanelInternalModules from "@/components/PanelInternalModules.vue";
import { ref, computed } from "vue";
import PanelStatusIcons from "@/components/PanelStatusIcons.vue";
import PanelDecision from "@/components/PanelDecision.vue";
import PanelDecisionBlock from "@/components/PanelDecisionBlock.vue";
import CustomPanel from "@/components/CustomPanel.vue";

const props = defineProps({
  type: {
    required: true,
    // type: String,
    validator(value) {
      return ['study-office', 'chairman'].includes(value)
    }
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

const id = props.connectionData['id']

const decisionSuggestion = props.connectionData['decisionSuggestion'] !== 'unedited'
    ? props.connectionData['decisionSuggestion'] : undefined
const decisionFinal = props.connectionData['decisionFinal'] !== 'unedited'
    ? props.connectionData['decisionFinal'] : undefined

const panelExternalModules = ref()
const panelInternalModules = ref()

const externalModules = computed(() => panelExternalModules.value?.externalModules)
const internalModules = computed(() => panelInternalModules.value?.selectedModules)

const studyOfficeDecisionBlock = ref()
const chairmanDecisionBlock = ref()

/*
const administrativeData = computed(() => {
  const data = {}
  if (props.type === 'study-office') {
    data['decisionSuggestion'] = studyOfficeDecisionBlock.value.decision
    data['commentDecision'] = studyOfficeDecisionBlock.value.comment
  } else if (props.type === 'chairman') {
    data['decisionFinal'] = chairmanDecisionBlock.value.decision
    data['commentDecision'] = chairmanDecisionBlock.value.comment
  }
  return data
})
*/

defineExpose({
  id,
  externalModules,
  internalModules,
})
</script>

<template>
  <div>

    <CustomPanel>

      <template #header>
        <PanelHeader :external-modules="externalModules?.map(m => m.name).filter(name => name !== '')" :internal-modules="internalModules" />
      </template>

      <template #icons>
        <PanelStatusIcons :decision-suggestion="decisionSuggestion" :decision-final="decisionFinal" />
      </template>

      <PanelExternalModules
          type="edit"
          :modules-data="connectionData['moduleApplications']"
          ref="panelExternalModules"
      />
      <hr>
      <PanelInternalModules
          type="edit"
          :selected-modules="connectionData['modulesLeipzig'].map(m => m['name'])"
          :options="selectableModules"
          ref="panelInternalModules"
      />
      <hr>
      <PanelComment
          type="readonly"
          :comment="connectionData['commentApplicant']"
      />
      <hr>
      <PanelDecision>
        <template #study-office>
          <PanelDecisionBlock
              :type="type === 'study-office' ? 'edit' : 'readonly'"
              :display-decision="decisionSuggestion"
              :comment="connectionData['commentStudyOffice']"
              ref="studyOfficeDecisionBlock"
          />
        </template>
        <template #chairman>
          <PanelDecisionBlock
              :type="type === 'chairman' ? 'edit' : 'readonly'"
              :display-decision="decisionFinal"
              :comment="connectionData['commentDecision']"
              ref="chairmanDecisionBlock"
          />
        </template>
      </PanelDecision>

    </CustomPanel>

  </div>
</template>

<style scoped>

</style>

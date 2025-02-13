<script setup>
import { ref, computed } from "vue";
import PanelComment from "@/components/panel-parts/PanelComment.vue";
import PanelHeader from "@/components/panel-parts/PanelHeader.vue";
import CustomPanel from "@/components/panel/CustomPanel.vue";
import PanelInternalModules from "@/components/panel-parts/PanelInternalModules.vue";
import PanelDecisionBlock from "@/components/panel-parts/PanelDecisionBlock.vue";
import PanelExternalModules from "@/components/panel-parts/PanelExternalModules.vue";
import PanelFormalRejectionBlock from "@/components/panel-parts/PanelFormalRejectionBlock.vue";
import PanelDecision from "@/components/panel-parts/PanelDecision.vue";
import ModuleStatusIcon from "@/assets/icons/ModuleStatusIcon.vue";
import TrashIcon from "@/assets/icons/TrashIcon.vue";

/*
panel to display a module connection for student
edit of all fields displayed for student is allowed if prop readonly is true
 */

const props = defineProps({
  /* controls if editable */
  readonly: {
    required: true,
    type: Boolean
  },
  /* controls if panel can be deleted */
  allowDelete: {
    required: true,
    type: Boolean
  },
  /* array of Strings, module names that should be selectable as internal modules */
  selectableModules: {
    required: true,
    type: Array
  },
  /* data of module connection to display / edit, should contain ...
  *  Number id, array of objects externalModules, array of Strings modulesLeipzig, String commentApplicant,
  *  String decisionFinal, String commentDecision, Boolean formalRejection, String, formalRejectionComment */
  connection: {
    required: true,
    type: Object,
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
  /* connection id */
  id,
  /* PanelExternalModules ref, see expose of PanelExternalModules */
  externalModules,
  /* PanelInternalModules ref, see expose of PanelInternalModules */
  internalModules,
  /* comment applicant */
  commentApplicant,
  /* function () to check data validity, cascades call */
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
      <TrashIcon v-if="allowDelete" @click="emit('deleteSelf')" background-color="white"
                 :aria-label="$t('StatusPanel.AriaDeleteModule')" />
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
          <p v-else>{{ $t('StatusPanel.NotYetDecided') }}</p>
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
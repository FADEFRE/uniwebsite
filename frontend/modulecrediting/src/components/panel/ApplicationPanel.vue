<script setup>
import CustomPanel from "@/components/panel/CustomPanel.vue";
import PanelHeader from "@/components/panel-parts/PanelHeader.vue";
import PanelComment from "@/components/panel-parts/PanelComment.vue";
import PanelExternalModules from "@/components/panel-parts/PanelExternalModules.vue";
import PanelInternalModules from "@/components/panel-parts/PanelInternalModules.vue";
import { ref, computed } from "vue";
import TrashIcon from "@/assets/icons/TrashIcon.vue";

/*
panel to use for creating an application
allows edit of all fields provided
 */

const props = defineProps({
  /* array of Strings, module names that should be selectable as internal modules */
  selectableModules: {
    required: true,
    type: Array
  },
  /* controls if panel can be deleted */
  allowDelete: {
    required: true,
    type: Boolean
  }
})

const panelExternalModules = ref()
const panelInternalModules = ref()
const panelComment = ref()

const externalModules = computed(() => panelExternalModules.value?.externalModules)
const internalModules = computed(() => panelInternalModules.value?.selectedModules)
const commentApplicant = computed(() => panelComment.value?.comment)

const emit = defineEmits([
  /* emitted on delete icon click, delete must be handled by parent */
  'deleteSelf'
])

const panelRef = ref()

const checkValidity = () => {
  const validity = panelExternalModules.value.checkValidity()
  panelRef.value.setCollapsed(validity)
  return validity
}

defineExpose({
  /* PanelExternalModules ref, see expose of PanelExternalModules */
  externalModules,
  /* PanelInternalModules ref, see expose of PanelInternalModules */
  internalModules,
  /* String comment of applicant */
  commentApplicant,
  /* function () to check data validity, cascades call */
  checkValidity
})
</script>


<template>
  <CustomPanel :initial-collapsed-state="false" ref="panelRef">
    <!-- Header Content -->
    <template #header>
      <PanelHeader :external-modules="externalModules?.map(m => m.name).filter(name => name !== '')"
                   :internal-modules="internalModules" />
    </template>

    <!-- Icons Slot -->
    <template #icons>
      <TrashIcon v-if="allowDelete" @click="emit('deleteSelf')" backgroundColor="white"
                 :aria-label="$t('ApplicationPanel.AriaDeleteModule')" />
    </template>

    <!-- Panel Content -->
    <PanelExternalModules
        :allow-text-edit="true"
        :allow-file-edit="true"
        :allow-delete="true"
        :allow-add="true"
        :has-initial-new="true"
        ref="panelExternalModules"
    />
    <PanelInternalModules
        :allow-select="true"
        :allow-delete="true"
        :options="selectableModules"
        ref="panelInternalModules"
    />
    <PanelComment
        :readonly="false"
        ref="panelComment"
    />
  </CustomPanel>
</template>


<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>

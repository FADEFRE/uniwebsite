<!--
panel representing one module connection, usable for application
props:
  - selectableModules (array of internal modules that could be selected
emits:
  - deleteSelf (called on trash icon click)
exposes:
  - externalModules (array of objects describing externalModules
  - internalModules (array module names)
  - applicantComment (String)
displays:
  - toggleable panel with dynamic header and delete option
  - external module block, internal module block and commentary block as panel content
-->

<script setup>
import CustomPanel from "@/components/CustomPanel.vue";
import PanelHeader from "@/components/PanelHeader.vue";
import PanelComment from "@/components/PanelComment.vue";
import PanelExternalModules from "@/components/PanelExternalModules.vue";
import PanelInternalModules from "@/components/PanelInternalModules.vue";
import { ref, computed } from "vue";

const props = defineProps({
  selectableModules: {
    required: true,
    type: Array
  }
})

const panelExternalModules = ref()
const panelInternalModules = ref()
const panelComment = ref()

const externalModules = computed(() => panelExternalModules.value?.externalModules)
const internalModules = computed(() => panelInternalModules.value?.selectedModules)
const commentApplicant = computed(() => panelComment.value?.comment)

const emit = defineEmits(['deleteSelf'])

defineExpose({
  externalModules,
  internalModules,
  commentApplicant
})
</script>


<template>
  <div>
    <CustomPanel>
      <!-- Header Content -->
      <template #header>
        <PanelHeader :external-modules="externalModules?.map(m => m.name).filter(name => name !== '')"
          :internal-modules="internalModules" />
      </template>

      <!-- Icons Slot -->
      <template #icons>
        <img src="@/assets/icons/Trash.svg" @click="emit('deleteSelf')">
      </template>

      <!-- Panel Content -->
      <PanelExternalModules type="new" ref="panelExternalModules" />
      <PanelInternalModules type="new" :options="selectableModules" ref="panelInternalModules" />
      <PanelComment type="new" ref="panelComment" />
    </CustomPanel>
  </div>
</template>


<style scoped lang="scss">
</style>



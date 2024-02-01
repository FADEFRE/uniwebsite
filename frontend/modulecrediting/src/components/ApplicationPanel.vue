<!--
panel representing one module connection, usable for application
props:
  - selectableModules (array of internal modules that could be selected)
  - allowDelete (Boolean, defaults to false)
emits:
  - deleteSelf (called on trash icon click)
exposes:
  - externalModules (array of objects describing externalModules)
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
  },
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

const emit = defineEmits(['deleteSelf'])

const checkValidity = () => {
  panelExternalModules.value.checkValidity()
}

defineExpose({
  externalModules,
  internalModules,
  commentApplicant,
  checkValidity
})
</script>


<template>
  <div>
    <CustomPanel :initial-collapsed-state="false">
      <!-- Header Content -->
      <template #header>
        <PanelHeader :external-modules="externalModules?.map(m => m.name).filter(name => name !== '')"
          :internal-modules="internalModules" />
      </template>

      <!-- Icons Slot -->
      <template #icons>
        <img v-if="allowDelete" src="@/assets/icons/Trash.svg" @click="emit('deleteSelf')" class="trash-icon">
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
  </div>
</template>


<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';
.trash-icon {
  @include trashIconAnimation();
}
</style>



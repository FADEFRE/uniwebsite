<!--
module panel for SubmitApplicationView, built on PanelBase
displays:
- module, university, credit points, point system inputs VIA PanelBase
- file upload VIA PanelApplicationFile (PanelBase file slot)
- internal modules selection VIA PanelBaseInternalModules (PanelBase internalModules slot)
- comment VIA PanelApplicationComment (PanelBase comment slot)
functionality:
- extending PanelBase with file, internalModules and comment input
-->

<script setup>
import PanelBase from "@/components/PanelBase.vue";
import PanelApplicationFile from "@/components/PanelApplicationFile.vue";
import PanelBaseInternalModules from "@/components/PanelBaseInternalModules.vue";
import PanelApplicationComment from "@/components/PanelApplicationComment.vue";
import { ref, computed, watch, inject } from "vue";

const emit = defineEmits(['deletePanel'])

const deleteSelf = () => {
  emit('deletePanel')
}

const base = ref()
const panelHeading = computed(() => {
  return base.value?.moduleName || 'Neues Modul'
})

const file = ref()
const internalModules = ref()
const comment = ref()


defineExpose({
  base, file, internalModules, comment
})
</script>

<template>
  <div>
    <Panel toggleable class="module-application-panel">

      <template #header>
        <h2>{{ panelHeading }}</h2>
      </template>

      <template #icons>
        <button class="p-panel-header-icon p-link mr-2" @click="deleteSelf">
          <span class="pi pi-trash"></span>
        </button>
      </template>

      <PanelBase ref="base">
        <template #file>
          <PanelApplicationFile ref="file"/>
        </template>
        <template #internalModules>
          <PanelBaseInternalModules ref="internalModules"/>
        </template>
        <template #comment>
          <PanelApplicationComment ref="comment"/>
        </template>
      </PanelBase>

    </Panel>
  </div>
</template>

<style scoped>
.module-application-panel {
  margin: 10px;
}
</style>

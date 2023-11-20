<script setup>
import ModuleApplicationData from "@/components/ModuleApplicationData.vue";
import { ref, computed } from "vue";

const emit = defineEmits(['deletePanel'])
const deleteSelf = () => {
  emit('deletePanel')
}

const dataRef = ref(0)
const moduleName = computed(() => {
  return dataRef.value?.moduleName ? dataRef.value.moduleName : 'Neues Modul'
})

defineExpose({dataRef})
</script>

<template>
  <div>
    <Panel toggleable class="module-application-panel">

      <template #header>
        <h2>{{ moduleName }}</h2>
      </template>

      <template #icons>
        <button class="p-panel-header-icon p-link mr-2" @click="deleteSelf">
          <span class="pi pi-trash"></span>
        </button>
      </template>

      <ModuleApplicationData ref="dataRef"/>

    </Panel>
  </div>
</template>

<style scoped>
.module-application-panel {
  margin: 10px;
}
</style>

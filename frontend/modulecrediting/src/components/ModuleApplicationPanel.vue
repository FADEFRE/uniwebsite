<script setup>
import ModuleApplicationData from "@/components/ModuleApplicationData.vue";
import { ref, computed } from "vue";

const dataRef = ref(0)

const moduleName = computed(() => {
  return dataRef.value?.moduleName ? dataRef.value.moduleName : 'Neues Modul'
})
</script>

<template>
  <div>
    <Panel toggleable>

      <template #header>
        <h2>{{ moduleName }}</h2>
      </template>

      <template #icons>
        <button class="p-panel-header-icon p-link mr-2">
          <span class="pi pi-trash"></span>
        </button>
      </template>

      <ModuleApplicationData ref="dataRef"/>

    </Panel>
  </div>
</template>

<style scoped>

</style>

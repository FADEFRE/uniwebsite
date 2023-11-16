<script setup>
import ModuleApplicationData from "@/components/ModuleApplicationData.vue";
import { ref } from "vue";

const dataRef = ref(null)
</script>

<template>
  <div>
    <Panel toggleable>

      <template #header>
        <h2 v-if="dataRef.moduleName">{{ dataRef.moduleName }}</h2>
        <h2 v-else>Neues Modul</h2>
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

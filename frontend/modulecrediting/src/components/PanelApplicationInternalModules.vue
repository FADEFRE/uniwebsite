<!--
allows selection and deletion of internal modules
displays:
- selected dropdowns (varying amount)
- delete button next to every dropdown with content selected
- dropdown with placeholder for new selection
functionality:
- displays dropdown with placeholder at first
- adds a new placeholder dropdown with each selection
- deleting dropdowns with content selected
-->

<script setup>
import { ref, computed, inject } from 'vue';

// internal module
const internalModules = inject('modules')
const internalModulesModel = ref(['placeholder'])
const selectedInternalModules = computed(
    () => internalModulesModel.value.filter((item) => item !== 'placeholder')
)
const resetSelectedInternalModules = () => {
  console.log('reset in ModuleApplicationData')
  internalModulesModel.value = ['placeholder']
}

defineExpose({
  selectedInternalModules, resetSelectedInternalModules
})
</script>

<template>
  <div class="internal-module-container">
    <!-- Internal Module Selection -->
    <div v-for="n in internalModulesModel.length" class="internal-module-dropdown-item">
      <Dropdown
          v-model="internalModulesModel[n]"
          :options="internalModules"
          :placeholder="n === 1 ? 'Modul wählen' : 'Weiteres Modul wählen'"
          class="module-dropdown"
      />
      <Button
          text
          v-if="n < internalModulesModel.length"
          icon="pi pi-trash"
          @click="internalModulesModel.splice(n, 1)"
          class="internal-module-remove-button"
      />
    </div>
  </div>
</template>

<style scoped>
.internal-module-container {
  display: grid;
  justify-items: auto;
}

.internal-module-remove-button {
  color: #848484;
}
</style>
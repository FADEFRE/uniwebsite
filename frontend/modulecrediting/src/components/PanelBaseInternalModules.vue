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
restrictions:
- every element of initials has to be an element of options
-->

<script setup>
import { ref, computed } from 'vue';

const props = defineProps(['useInitials'])

// data
const internalModules = ref()
const internalModulesModel = ref(['placeholder'])
const selectedInternalModules = computed(
    () => internalModulesModel.value.filter((item) => item !== 'placeholder')
)

// used in SubmitApplicationView on course change
const resetInternalModules = (options) => {
  if (props.useInitials) {
    throw Error('PanelBaseInternalModules with initials is not allowed to reset')
  } else {
    internalModulesModel.value = ['placeholder']
    internalModules.value = options
  }
}

// used in StudyOfficeDetailView for setup
const setup = (options, initials) => {
  if (props.useInitials) {
    internalModules.value = options
    initials.unshift('placeholder')
    internalModulesModel.value = initials
  } else {
    throw Error('PanelBaseInternalModules without initials is not allowed to setup')
  }
}

defineExpose({
  selectedInternalModules, resetInternalModules, setup
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
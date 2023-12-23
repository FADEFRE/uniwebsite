<!--
choose modules form a list of options
props:
  - options (arrays of selectable modules)
exposes:
  - selectedModules
displays:
  - dropdown with filter, without selection shown
  - selected modules as separate list with delete icon
-->

<script setup>
import { ref } from "vue";

const props = defineProps({
  options: {
    required: true,
    type: Array,
  }
})

const selectedModules = ref([])
const addSelectedModule = (module) => {
  if (!selectedModules.value.includes(module)) {
    selectedModules.value.push(module)
  }
}
const removeSelectedModule = (index) => {
  selectedModules.value.splice(index, 1)
}

defineExpose({
  selectedModules
})
</script>

<template>
  <div>

    <h3>Module der Universität Leipzig</h3>

    <div>
      <Dropdown filter
          :options="options"
          placeholder="Modul auswählen"
          @change="e => addSelectedModule(e.value)"
      />
    </div>

    <div>
      <div v-for="(module, index) in selectedModules">
        <p>{{ module }}</p>
        <img src="../assets/icons/Trash.svg" @click="removeSelectedModule(index)">
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>
<!--
choose modules form a list of options
props:
  - type (may be 'new', 'edit' or 'readonly')
  - options (arrays of selectable modules, should be given if type is 'new' or 'edit')
  - selectedModules (optional, elements must be elements of options)
exposes:
  - selectedModules
displays:
  - dropdown with filter, without selection shown (if type is 'new' or 'edit')
  - selected modules as separate list with
  - delete icon for every selected module (if type is 'new' or 'edit')
-->

<script setup>
import { ref, onBeforeMount } from "vue";

const props = defineProps({
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['new', 'edit', 'readonly'].includes(value)  // new and edit behave the same
    }
  },
  options: {
    type: Array
  },
  selectedModules: {
    type: Array
  }
})

const selectedModules = ref(props.selectedModules || [])
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
  <div class="panel-internal-modules">

    <h4>Module der Universität Leipzig</h4>

    <div class="screen-split">
      <div class="module-dropdown" v-if="type === 'new' || type === 'edit'">
        <Dropdown filter :options="options" placeholder="Modul auswählen" @change="e => addSelectedModule(e.value)" />
      </div>

      <div class="module-list">
        <div v-for="(module, index) in selectedModules" class="module-list-item">
          <p>{{ module }}</p>
          <div v-if="type === 'new' || type === 'edit'">
            <img src="../assets/icons/Trash.svg" @click="removeSelectedModule(index)">
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped lang="scss">
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';

.panel-internal-modules {
  @include panelComponent();
}
.screen-split {
  @include screenSplit();
}
.module-dropdown {
  width: 50%;
}
.module-list{
  @include verticalList(small);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;
}
.module-list-item {
  @include smallHighlightBox();
  @include verticalListItem($gray);
  width: 100%;
  
  &:hover{
    background-color: $mid-gray;
  }
}
</style>
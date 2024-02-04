<!--
choose modules form a list of options
props:
  - allowSelect
  - allowDelete
  - options (should be given if allowSelect)
  - selectedModules (optional, elements must be elements of options)
exposes:
  - selectedModules
displays:
  - dropdown with filter, without selection shown (if allowSelect)
  - selected modules as separate list with
  - delete icon for every selected module (if allowDelete)
-->

<script setup>
import { ref } from "vue";

const props = defineProps({
  allowSelect: {
    required: true,
    type: Boolean,
  },
  allowDelete: {
    required: true,
    type: Boolean,
  },
  options: {
    type: Array
  },
  selectedModules: {
    type: Array
  }
})

const emit = defineEmits(['change'])

const selectedModules = ref(props.selectedModules || [])
const addSelectedModule = (module) => {
  if (!selectedModules.value.includes(module)) {
    selectedModules.value.push(module)
    emit('change')
  }
}
const removeSelectedModule = (index) => {
  selectedModules.value.splice(index, 1)
  emit('change')
}

defineExpose({
  selectedModules
})
</script>

<template>
  <div class="panel-internal-modules">

    <h4>Module der Universität Leipzig</h4>

    <div class="screen-split">

      <div class="module-dropdown" v-if="allowSelect">
        <Dropdown filter :options="options" placeholder="Modul auswählen" emptyMessage="Studiengang auswählen" emptyFilterMessage="Modul nicht gefunden" @change="e => addSelectedModule(e.value)">
          <template #filtericon>
            <img class="search-icon" src="@/assets/icons/SearchIcon.svg">
          </template>
          <template #dropdownicon>
              <img src="../assets/icons/ArrowWhite.svg">
            </template>
        </Dropdown>
      </div>

      <div class="module-list" :class="{ 'module-list-full': !allowSelect }">
        <div v-if="selectedModules.length > 0" v-for="(module, index) in selectedModules" class="module-list-item">
          <p>{{ module }}</p>
          <div v-if="allowDelete">
            <img src="../assets/icons/Trash.svg" @click="removeSelectedModule(index)" class="trash-icon">
          </div>
        </div>
        <div v-else-if="!allowSelect">
          <p>Es sind keine Module der Universität Leipzig ausgewählt.</p>
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
  
  @media only screen and (max-width: 700px) {
    flex-wrap: wrap;
  }
}
.module-dropdown {
  width: 50%;

  @media only screen and (max-width: 700px) {
    width: 100%;
  }
}
.module-list{
  @include verticalList(small);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;

  @media only screen and (max-width: 700px) {
    width: 100%;
  }
}
.module-list-full {
  width: 100%
}
.module-list-item {
  @include smallHighlightBox();
  @include verticalListItem($gray);
  width: 100%;
}
.search-icon {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: 0.5rem;
}
.trash-icon {
  @include trashIconAnimation();
  &:hover{
    background-color: $gray-hover;
  }
}

</style>
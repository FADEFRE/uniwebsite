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
import TrashIcon from "@/assets/icons/TrashIcon.vue";
import CustomDropdown from "@/components/util/CustomDropdown.vue";

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
const addSelectedModule = (singleModule) => {
  if (!selectedModules.value.includes(singleModule)) {
    selectedModules.value.push(singleModule)
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
  <div class="panel-container">

    <h4>Module der Universität Leipzig</h4>

    <div class="screen-split">

      <div class="module-dropdown" v-if="allowSelect">
        <CustomDropdown
            filter
            placeholder="Modul auswählen"
            emptyMessage="Studiengang auswählen"
            emptyFilterMessage="Modul nicht gefunden"
            :options="options"
            @change="e => addSelectedModule(e.value)"
        />
      </div>

      <div class="module-list" :class="{ 'module-list-full': !allowSelect }">
        <div v-if="selectedModules.length > 0" v-for="(module, index) in selectedModules" class="module-list-item">
          <p>{{ module }}</p>
          <TrashIcon v-if="allowDelete" @click="removeSelectedModule(index)"/>
        </div>
        <div v-else-if="!allowSelect">
          <p>Es sind keine Module der Universität Leipzig ausgewählt.</p>
        </div>
      </div>

    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.screen-split {
  @include screenSplit();

  @include breakpoint(s) {
    flex-wrap: wrap;
    flex-direction: column;
  }
}

.module-dropdown {
  width: 50%;

  @include breakpoint(s) {
    width: 100%;
  }
}

.module-list {
  @include verticalList(s);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;

  @include breakpoint(s) {
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
  right: spacing(s);
}

</style>
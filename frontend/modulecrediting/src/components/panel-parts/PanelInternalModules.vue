<script setup>
import { ref, computed } from "vue";
import TrashIcon from "@/assets/icons/TrashIcon.vue";
import CustomDropdown from "@/components/util/CustomDropdown.vue";

/*
list of internal modules,
contains a dropdown for selection
 */

const props = defineProps({
  /* controls if modules can be added */
  allowSelect: {
    required: true,
    type: Boolean,
  },
  /* controls if modules can be deleted */
  allowDelete: {
    required: true,
    type: Boolean,
  },
  /* array of Strings, selectable modules */
  options: {
    type: Array
  },
  /* array of Strings, selected modules to be displayed (initially) */
  selectedModules: {
    type: Array
  }
})

const emit = defineEmits([
  /* emitted if any data changes */
  'change'
])

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

const selectableModules = computed(() => {
  return props.options.filter(m => !selectedModules.value.includes(m))
})

const emptyMessageKey = computed(() => {
  if (props.options.length > 0) return 'PanelInternalModules.AllSelected'
  else return 'PanelInternalModules.SelectCourse'
})

defineExpose({
  /* array of Strings, names of selected internal modules */
  selectedModules
})
</script>

<template>
  <div class="panel-container">

    <h4>{{ $t('PanelInternalModules.ModulesUniLeipzig') }}</h4>

    <div class="screen-split">

      <div class="module-dropdown" v-if="allowSelect">
        <CustomDropdown
            filter
            :placeholder="$t('PanelInternalModules.ChooseModule')"
            :emptyMessage="$t(emptyMessageKey)"
            :emptyFilterMessage="$t('PanelInternalModules.ModuleNotFound')"
            :options="selectableModules"
            @change="e => addSelectedModule(e.value)"
        />
      </div>

      <div class="module-list" :class="{ 'module-list-full': !allowSelect }">
        <div v-if="selectedModules.length > 0" v-for="(module, index) in selectedModules" class="module-list-item">
          <p>{{ module }}</p>
          <TrashIcon v-if="allowDelete" @click="removeSelectedModule(index)"/>
        </div>
        <div v-else-if="!allowSelect">
          <p>{{ $t('PanelInternalModules.NoModulesSelected') }}</p>
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
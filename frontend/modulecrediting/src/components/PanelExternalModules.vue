<!--
list of external modules
props:
  - type (may be 'new', 'edit' or 'readonly', is cascaded to PanelExternalModulesItem)
      type 'new' allows for adding external modules
  - modulesData (should be given if type is 'edit' or 'readonly')
      modulesData should be array containing objects, each with properties moduleName, university, creditPoints, pointSystem, selectedFile
exposes:
  - externalModules (array similar to modulesData)
displays:
  - list of PanelExternModulesItem
  - add external module button if type is 'new'
-->

<script setup>
import PanelExternalModulesItem from "@/components/PanelExternalModulesItem.vue";
import { ref, reactive, onBeforeMount } from "vue";

const props = defineProps({
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['new', 'edit', 'readonly'].includes(value)
    }
  },
  modulesData: {
    type: Array,
    validator(value) {
      for (let e of value) {
        if (!e.moduleName) return false
        if (!e.university) return false
        if (!e.creditPoints) return false
        if (!e.pointSystem) return false
        if (!e.selectedFile) return false
      }
      return true
    }
  }
})

// checking modulesData prop
onBeforeMount(() => {
  if (props.type === 'edit' || props.type === 'readonly') {
    if (!props.modulesData) {
      console.warn(`PanelExternalModules: prop modulesData should be given if type is ${props.type}`)
    }
  }
})

// used if type is 'new'
const externalModulesCount = reactive({1: null})
const addExternalModule = () => {
  let nextKey = null
  if (Object.keys(externalModules).length === 0) {
    nextKey = 1
  } else {
    console.log(Object.keys(externalModulesCount))
    // noinspection JSCheckFunctionSignatures
    nextKey = Math.max(...Object.keys(externalModulesCount)) + 1
  }
  console.log(nextKey)
  externalModulesCount[nextKey] = null
}

// ref for all types
const externalModules = ref()

defineExpose({
  externalModules
})
</script>

<template>
  <div class="panel-external-modules">

    <h3>Anzurechnende Module</h3>

    <div v-if="type === 'new'" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="(_, i) in externalModulesCount"
          :key="i"
          :type="type"
          ref="externalModules"
          @delete-self="delete externalModulesCount[i]"
      />
      <Button @click="addExternalModule">Fremdmodul hinzufügen</Button>
      <small>Anrechnung mehrerer externer Module auf Module der Universität Leipzig</small>
    </div>


    <div v-else-if="type === 'edit' || type === 'readonly'" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="module in modulesData"
          :type="type"
          :module-name="module.moduleName"
          :university="module.university"
          :credit-points="module.creditPoints"
          :point-system="module.pointSystem"
          :selected-file="module.selectedFile"
          ref="externalModules"
          />
    </div>


  </div>
</template>

<style scoped lang="scss">

@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.panel-external-modules {
  @include panelComponent();
}

.external-modules-list {
  @include verticalList(small);
  width: 100%;
}
</style>
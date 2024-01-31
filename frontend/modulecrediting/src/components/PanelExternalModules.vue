<!--
list of external modules
props:
  - type (may be 'new', 'edit' or 'readonly', is cascaded to PanelExternalModulesItem)
      type 'new' allows for adding external modules
  - modulesData (should be given if type is 'edit' or 'readonly')
      modulesData should be array containing objects, each with properties name, university, creditPoints, pointSystem, selectedFile
exposes:
  - externalModules (array similar to modulesData)
displays:
  - list of PanelExternModulesItem
  - add external module button if type is 'new'
-->

<script setup>
import PanelExternalModulesItem from "@/components/PanelExternalModulesItem.vue";
import ButtonAdd from "./ButtonAdd.vue";
import { ref } from "vue";

const props = defineProps({
  allowTextEdit: {
    required: true,
    type: Boolean
  },
  allowFileEdit: {
    required: true,
    type: Boolean
  },
  modulesData: {
    type: Array,
    validator(value) {
      for (let e of value) {
        if (!e.name) return false
        if (!e.university) return false
        if (!e.points) return false
        if (!e.pointSystem) return false
        if (!e.pdfDocument) return false
      }
      return true
    }
  }
})

const emit = defineEmits(['change'])

// connection handling
const externalModulesList = ref([0])

const addExternalModule = () => {
  if (externalModulesList.value.length > 0) {
    const nextIndex = Math.max(...externalModulesList.value) + 1
    externalModulesList.value.push(nextIndex)
  } else {
    externalModulesList.value.push(0)
  }
}

const deleteExternalModule = (key) => {
  externalModulesList.value = externalModulesList.value.filter(el => el !== key)
}

// data ref
const externalModules = ref()

const checkValidity = () => {
  for (let externalModule of externalModules.value) {
    externalModule.checkValidity()
  }
}

defineExpose({
  externalModules, checkValidity
})
</script>

<template>
  <div class="panel-external-modules">

    <h4>Anzurechnende Module</h4>

    <div v-if="!modulesData" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="i in externalModulesList"
          :key="i"
          :allow-text-edit="allowTextEdit"
          :allow-file-edit="allowFileEdit"
          :allow-delete="externalModulesList.length > 1"
          ref="externalModules"
          @delete-self="deleteExternalModule(i)"
      />
      <ButtonAdd @click="addExternalModule">Fremdmodul hinzufuegen</ButtonAdd>
      <small>Anrechnung mehrerer externer Module auf Module der Universität Leipzig</small>
    </div>

    <div v-else class="external-modules-list">
      <PanelExternalModulesItem
          v-for="module in modulesData"
          :allow-text-edit="allowTextEdit"
          :allow-file-edit="allowFileEdit"
          :allow-delete="false"
          :id="module.id"
          :name="module.name"
          :university="module.university"
          :points="module.points"
          :point-system="module.pointSystem"
          :selected-file="module['pdfDocument']"
          ref="externalModules"
          @change="emit('change')"
          />
    </div>


  </div>
</template>

<style scoped lang="scss">

@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.panel-external-modules {
  @include panelComponent();
  border-top: none;
}

.external-modules-list {
  @include verticalList(small);
  width: 100%;
}
</style>
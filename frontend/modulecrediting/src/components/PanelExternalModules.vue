<!--
list of external modules
props:
  - allowTextEdit
  - allowFileEdit
  - allowDelete
  - allowAdd
  - hasInitialNew (defaults to false)
  - modulesData (optional)
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
import { ref, computed } from "vue";

const props = defineProps({
  allowTextEdit: {
    required: true,
    type: Boolean
  },
  allowFileEdit: {
    required: true,
    type: Boolean
  },
  allowDelete: {
    required: true,
    type: Boolean
  },
  allowAdd: {
    required: true,
    type: Boolean
  },
  hasInitialNew: {
    type: Boolean,
    default: false
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

// handle existing modules
const existingModulesList = ref(props.modulesData)

const deleteExistingModule = (id) => {
  existingModulesList.value = existingModulesList.value.filter(m => m.id !== id)
  emit('change')
}

const existingModulesRef = ref()

// handle new modules
const newModulesList = ref([])
if (props.hasInitialNew) newModulesList.value.push(0)

const addNewModule = () => {
  if (newModulesList.value.length > 0) {
    const nextIndex = Math.max(...newModulesList.value) + 1
    newModulesList.value.push(nextIndex)
  } else {
    newModulesList.value.push(0)
  }
  emit('change')
}

const deleteNewModule = (key) => {
  newModulesList.value = newModulesList.value.filter(k => k !== key)
  emit('change')
}

const newModulesRef = ref()

// concat external modules
const externalModules = computed(() => {
  let modulesArray = []
  if (existingModulesRef.value) modulesArray = modulesArray.concat(existingModulesRef.value)
  if (newModulesRef.value) modulesArray = modulesArray.concat(newModulesRef.value)
  return modulesArray
})

const checkValidity = () => {
  return externalModules.value.map(m => m.checkValidity()).every(Boolean)
}

defineExpose({
  externalModules, checkValidity
})
</script>

<template>
  <div class="panel-external-modules">

    <h4>Anzurechnende Module</h4>

    <div v-if="existingModulesList" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="module in existingModulesList"
          :key="module.id"
          :allow-text-edit="allowTextEdit"
          :allow-file-edit="allowFileEdit"
          :allow-delete="allowDelete && externalModules.length > 1"
          :id="module.id"
          :name="module.name"
          :university="module.university"
          :points="module.points"
          :point-system="module.pointSystem"
          :selected-file="module.pdfDocument"
          @delete-self="deleteExistingModule(module.id)"
          @change="emit('change')"
          ref="existingModulesRef"
      />
    </div>

    <div v-if="allowAdd" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="key in newModulesList"
          :key="key"
          :allow-text-edit="allowTextEdit"
          :allow-file-edit="allowFileEdit"
          :allow-delete="allowDelete && externalModules.length > 1"
          @delete-self="deleteNewModule(key)"
          @change="emit('change')"
          ref="newModulesRef"
      />
      <ButtonAdd @click="addNewModule">Fremdmodul hinzufuegen</ButtonAdd>
      <small>Anrechnung mehrerer externer Module auf Module der Universität Leipzig</small>
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
<script setup>
import { ref, computed } from "vue";
import ButtonAdd from "@/components/button/ButtonAdd.vue";
import PanelExternalModulesItem from "@/components/panel-parts/PanelExternalModulesItem.vue";

/*
list of external modules
 */

const props = defineProps({
  /* controls if text fields are editable */
  allowTextEdit: {
    required: true,
    type: Boolean
  },
  /* controls if description files are editable */
  allowFileEdit: {
    required: true,
    type: Boolean
  },
  /* controls if modules can be deleted from list, it is never possible to delete all modules from the list */
  allowDelete: {
    required: true,
    type: Boolean
  },
  /* controls if new modules can be added to list */
  allowAdd: {
    required: true,
    type: Boolean
  },
  /* should be set true if initially an empty module should be displayed */
  hasInitialNew: {
    type: Boolean,
    default: false
  },
  /* array of external modules to be displayed initially, each must contain ...
  *  String name, String university, String points, String pointSystem, PDF-file pdfDocument */
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

const emit = defineEmits([
    /* emitted when any data changes */
    'change'
])

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
  /* array of PanelExternalModuleItem refs, see expose of PanelExternalModulesItem */
  externalModules,
  /* function () to check data validity, cascades call */
  checkValidity
})
</script>

<template>
  <div class="panel-container" id="external-modules-container">

    <h4>{{ $t('PanelExternalModules.ModulesToBeCredited') }}</h4>

    <div v-if="existingModulesList" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="externalModule in existingModulesList"
          :key="externalModule.id"
          :allow-text-edit="allowTextEdit"
          :allow-file-edit="allowFileEdit"
          :allow-delete="allowDelete && externalModules.length > 1"
          :id="externalModule.id"
          :name="externalModule.name"
          :points="externalModule.points"
          :point-system="externalModule.pointSystem"
          :university="externalModule.university"
          :external-course="externalModule.externalCourse"
          :selected-file="externalModule.pdfDocument"
          @delete-self="deleteExistingModule(externalModule.id)"
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
      <ButtonAdd @click="addNewModule">{{ $t('PanelExternalModules.AddModule') }}</ButtonAdd>
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

#external-modules-container {
  border-top: none;
}

.external-modules-list {
  @include verticalList(s);
  width: 100%;
}
</style>
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
import { ref, onBeforeMount } from "vue";

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

// custom prop checker
if (props.type !== 'new') {
  if (!(props.modulesData.every(m => m.id))) {
    console.warn("PanelExternalModules: if type is 'edit' or 'readonly' every module in modulesData prop should have property id")
  }
}

// checking modulesData prop
onBeforeMount(() => {
  if (props.type === 'edit' || props.type === 'readonly') {
    if (!props.modulesData) {
      console.warn(`PanelExternalModules: prop modulesData should be given if type is ${props.type}`)
    }
  }
})

// connection handling
const modifiedModulesData = ref(props.modulesData || [{ id: "", name: "", points: "", pointSystem: "" }])

const externalModulesList = ref()
if (props.modulesData) {
  externalModulesList.value = [...Array(props.modulesData.length).keys()].map(i => i)
} else {
  externalModulesList.value = [0]
}

const addExternalModule = () => {
  const emptyModule = { id: "", name: "", points: "", pointSystem: "" }
  if (externalModulesList.value.length > 0) {
    const nextIndex = Math.max(...externalModulesList.value) + 1
    modifiedModulesData.value.push(emptyModule)
    externalModulesList.value.push(nextIndex)
  } else {
    modifiedModulesData.value = [emptyModule]
    externalModulesList.value.push(0)
  }
}

const deleteExternalModule = (key) => {
  console.log(key)
  externalModulesList.value = externalModulesList.value.filter(el => el !== key)
}

// data ref
const externalModules = ref()

defineExpose({
  externalModules
})
</script>

<template>
  <div class="panel-external-modules">

    <h4>Anzurechnende Module</h4>

    <div v-if="type === 'new'" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="i in externalModulesList"
          :key="i"
          :type="type"
          ref="externalModules"
          @delete-self="deleteExternalModule(i)"
      />
      <ButtonAdd @click="addExternalModule">Fremdmodul hinzufuegen</ButtonAdd>
      <small>Anrechnung mehrerer externer Module auf Module der Universität Leipzig</small>
    </div>

    <div v-else-if="type === 'edit'" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="i in externalModulesList"
          :key="i"
          :type="type"
          :id="modifiedModulesData[i].id"
          :name="modifiedModulesData[i].name"
          :university="modifiedModulesData[i].university"
          :points="modifiedModulesData[i].points"
          :point-system="modifiedModulesData[i].pointSystem"
          :selected-file="modifiedModulesData[i]['pdfDocument']"
          ref="externalModules"
          @delete-self="deleteExternalModule(i)"
      />
      <ButtonAdd @click="addExternalModule">Fremdmodul hinzufuegen</ButtonAdd>
      <small>Anrechnung mehrerer externer Module auf Module der Universität Leipzig</small>
    </div>


    <div v-else-if="type === 'readonly'" class="external-modules-list">
      <PanelExternalModulesItem
          v-for="module in modulesData"
          :type="type"
          :id="module.id"
          :name="module.name"
          :university="module.university"
          :points="module.points"
          :point-system="module.pointSystem"
          :selected-file="module['pdfDocument']"
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
  border-top: none;
}

.external-modules-list {
  @include verticalList(small);
  width: 100%;
}
</style>
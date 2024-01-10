<!--
single external module
props:
  - type (may be 'new', 'edit' or 'readonly')
      type 'new' allows editing all data and deleting self
      type 'edit' allows editing all data except selected file
      type 'readonly' allows no editing
  - moduleName (should be given if type is 'edit' or 'readonly')
  - university (should be given if type is 'edit' or 'readonly')
  - points (should be given if type is 'edit' or 'readonly')
  - pointSystem (should be given if type is 'edit' or 'readonly')
  - selectedFile (should be given if type is 'edit' or 'readonly')
emits:
  - deleteSelf (called on trash button click)
exposes:
  - moduleName
  - university
  - points
  - pointSystem
  - selectedFile
displays:
  - input fields for moduleName, university, creditPoints, pointSystem
  - drop zone for module description
  - delete icon (if type is 'new')
-->

<script setup>
import { ref, onBeforeMount } from "vue";

const props = defineProps({
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['new', 'edit', 'readonly'].includes(value)
    }
  },
  id: {
    type: Number,
  },
  name: {
    type: String
  },
  university: {
    type: String
  },
  points: {
    type: Number
  },
  pointSystem: {
    type: String
  },
  selectedFile: {
    type: Object
  }
})

const id = props.id ? props.id : undefined

// checking props
onBeforeMount(() => {
  const warn = (prop) => {
    console.warn(`PanelExternalModulesItem: prop ${prop} should be given if type is ${props.type}`)
  }

  if (props.type === 'edit' || props.type === 'readonly') {
    if (!props.name) warn('name')
    if (!props.university) warn('university')
    if (!props.points) warn('points')
    if (!props.pointSystem) warn('pointSystem')
    if (!props.selectedFile) warn('selectedFile')
  }
})

const emit = defineEmits(['deleteSelf'])

const readonly = props.type === 'readonly'

const name = ref(props.name || "")
const university = ref(props.university || "")
const points = ref(props.points || "")
const pointSystem = ref(props.pointSystem || "")

// file drop div
const selectedFile = ref(props.selectedFile)

const dropHandler = (e) => {
  if (props.type === 'new') {
    console.log("executing drop handler")
    e.preventDefault()
    if (e.dataTransfer.items) {
      const f = e.dataTransfer.items[0].getAsFile()
      if (f.type !== "application/pdf") {
        console.log("file type is not pdf")
        alert("Es muss eine PDF-Datei ausgewählt werden.")
      } else if (f.size > 10000000) {
        console.log("file size is greater than 10 MB")
        alert("Die Datei darf nicht größer als 10 MB sein.")
      } else {
        selectedFile.value = e.dataTransfer.items[0].getAsFile()
        console.log(`selected '${selectedFile.value.name}'`)

      }
    }
  }
}

const dragOverHandler = (e) => {
  if (props.type === 'new') {
    e.preventDefault()
  }
}

defineExpose({
  id, name, university, points, pointSystem, selectedFile
})
</script>

<template>
  <div class="external-modules-item">
    <div class="screen-split">
      <div class="left-side">
        <InputText :readonly="readonly" type="text" v-model="name" placeholder="Modulname" />
        <InputText :readonly="readonly" type="text" v-model="university" placeholder="Universität"/>
      </div>

      <div class="right-side">

        <div class="point-container">
          <InputText :readonly="readonly" type="text" v-model="points" placeholder="Punkte" />
          <InputText :readonly="readonly" type="text" v-model="pointSystem" placeholder="Punktesystem" />
        </div>

        <div class="file-drop-container" @dragover="dragOverHandler" @drop="dropHandler">
          <!-- todo add file dialog on click -->
          <div v-if="selectedFile?.name">
            <p>{{ selectedFile.name }}</p>
          </div>
          <div v-else class="file-drop-unselected">
            <p>Modulbeschreibung hochladen</p>
            <img src="../assets/icons/Upload.svg">
          </div>
        </div>

      </div>

    </div>

    <div v-if="type === 'new'">
      <img src="../assets/icons/Trash.svg" @click="emit('deleteSelf')">
    </div>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';


.external-modules-item {
  @include verticalListItem($gray);

  width: 100%;
  padding: 0.625rem 0.9375rem 0.625rem 1.25rem;

  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 0.9375rem;
}

.screen-split {
  @include screenSplit();
}

.left-side {
  @include verticalList(small);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;
  
}

.right-side {
  @include verticalList(small);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;
}

.point-container {
  display: flex;
  gap: 0.625rem;
}

.file-drop-container {
  border: 1px dashed $black;

  display: flex;
  padding: 0.625rem 0rem;
  justify-content: center;
}

.file-drop-unselected {
  display: flex;
  gap: 0.625rem;
}

</style>
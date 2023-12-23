<!--
single external module
props:
  - type (may be 'new', 'edit' or 'readonly')
      type 'new' allows editing all data and deleting self
      type 'edit' allows editing all data except selected file
      type 'readonly' allows no editing
  - moduleName (should be given if type is 'edit' or 'readonly')
  - university (should be given if type is 'edit' or 'readonly')
  - creditPoints (should be given if type is 'edit' or 'readonly')
  - pointSystem (should be given if type is 'edit' or 'readonly')
  - selectedFile (should be given if type is 'edit' or 'readonly')
emits:
  - deleteSelf (called on trash button click)
exposes:
  - moduleName
  - university
  - creditPoints
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
  moduleName: {
    type: String
  },
  university: {
    type: String
  },
  creditPoints: {
    type: Number
  },
  pointSystem: {
    type: String
  },
  selectedFile: {
    type: File
  }
})

// checking props
onBeforeMount(() => {
  const warn = (prop) => {
    console.warn(`PanelExternalModulesItem: prop ${prop} should be given if type is ${props.type}`)
  }

  if (props.type === 'edit' || props.type === 'readonly') {
    if (!props.moduleName) warn('moduleName')
    if (!props.university) warn('university')
    if (!props.creditPoints) warn('creditPoints')
    if (!props.pointSystem) warn('pointSystem')
    if (!props.selectedFile) warn('selectedFile')
  }
})

const emit = defineEmits(['deleteSelf'])

const readonly = props.type === 'readonly'

const moduleName = ref(props.moduleName)
const university = ref(props.university)
const creditPoints = ref(props.creditPoints)
const pointSystem = ref(props.pointSystem)

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
  moduleName, university, creditPoints, pointSystem, selectedFile
})
</script>

<template>
  <div>

    <div>
      <div>

        <div>
          <InputText :readonly="readonly" type="text" v-model="moduleName" placeholder="Modulname" />
          <InputText :readonly="readonly" type="text" v-model="university" placeholder="Universität" />
          <InputText :readonly="readonly" type="text" v-model="creditPoints" placeholder="Punkte" />
        </div>

        <div>
          <InputText :readonly="readonly" tyoe="text" v-model="pointSystem" placeholder="Punktesystem" />
          <div class="file-drop" @dragover="dragOverHandler" @drop="dropHandler">
            <!-- todo add file dialog on click -->
            <div v-if="selectedFile?.name">
              <p>{{ selectedFile.name }}</p>
            </div>
            <div v-else>
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

  </div>
</template>

<style scoped>
.file-drop {
  border-style: dashed;
}
</style>
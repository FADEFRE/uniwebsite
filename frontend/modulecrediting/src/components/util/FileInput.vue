<!--
file input drop area with file dialog on click
props:
  - readonly
  - selectedFile (File object)
exposes:
  - selectedFile (might be File Object OR object with properties name and id)
displays:
  - drop area
  - file name inside drop area if file is selected
-->

<script setup>
import { ref } from "vue";
import ButtonLink from "@/components/button/ButtonLink.vue";
import FileIcon from "@/assets/icons/FileIcon.vue";
import { url } from "@/scripts/url-config";

const props = defineProps({
  readonly: {
    required: true,
    type: Boolean
  },
  type: {
    required: true,
    type: String
  },
  selectedFile: {
    type: Object
  }
})

if (props.readonly && !props.selectedFile) {
  console.warn("FileInput: prop selectedFile should be given if prop readonly is true")
}

const openFile = () => {
  const fileLink = url + '/file/pdf-documents/' + props.selectedFile.id
  window.open(fileLink)
}

const fileInput = ref()
const selectedFile = ref()

const checkFile = (file) => {
  if (props.type === 'pdf') {

    if (file.type !== 'application/pdf') {
      alert('Die Modulbeschreibung muss eine PDF sein.')
      return false
    }
    if (file.size > 10000000) {
      alert('Die Modulbeschreibung darf nicht größer als 10 MB sein.')
      return false
    }
    return true

  } else if (props.type === 'json') {

    if (file.type !== 'application/json') {
      alert('Es muss eine JSON Datei ausgewählt werden.')
      return false
    }
    return true

  } else {

    console.warn('FileInput: prop type does not match any of the predefined values (defined in checkFile function)')
    alert('Es ist etwas schief gelaufen.')
    return false

  }
}

const openFileDialog = () => {
  if (!props.readonly) {
    if (fileInput.value) {
      fileInput.value.click()
    }
  }
}

const handleFiles = (e) => {
  if (!props.readonly) {
    const file = e.target.files[0]
    if (checkFile(file)) {
      selectedFile.value = file

      console.log(selectedFile.value);
    }
  }
}

const dragOverHandler = (e) => {
  if (!props.readonly) e.currentTarget.classList.add('edit-container-highlight')
  console.log(selectedFile.value);
}

const dragLeaveHandler = (e) => {
  if (!props.readonly) e.currentTarget.classList.remove('edit-container-highlight')
}

const dropHandler = (e) => {
  if (!props.readonly) {
    const file = e.dataTransfer.files[0]
    if (checkFile(file)) {
      selectedFile.value = file
    }
    e.currentTarget.classList.remove('edit-container-highlight')
  }
  console.log(selectedFile.value);
}

const isValid = ref(true)

const checkValidity = () => {
  // setting invalid styles
  isValid.value = Boolean(props.selectedFile || selectedFile.value)
  // return
  return isValid
}

defineExpose({
  selectedFile, checkValidity  // exposed selectedFile might be File Object OR object with properties name and id
})
</script>

<template>
    <div v-if="readonly" class="read-only-container" :class="{ 'invalid': !isValid }">
      <p class="read-only-file-name">{{ props.selectedFile?.name }}</p>
      <ButtonLink @click="openFile">PDF öffnen</ButtonLink>
    </div>
    
    <div v-else
         tabindex="0"
         @click="openFileDialog"
         @keydown.enter="openFileDialog"
         @dragover.prevent="dragOverHandler"
         @dragleave.prevent="dragLeaveHandler"
         @drop.prevent="dropHandler"
         :class="{ 'invalid': !isValid }"
         class="edit-container"
    >

      <p v-if="selectedFile?.name" class="ellipsis-text-overflow">{{ selectedFile?.name }}</p>

      <p v-else-if="props.selectedFile?.name" class="ellipsis-text-overflow">{{ props.selectedFile?.name }}</p>

      <div v-else class="file-drop-unselected">
        <p class="ellipsis-text-overflow">
          <slot />
        </p>
        <FileIcon type="upload"/>
      </div>
      <input type="file" ref="fileInput" @change="handleFiles" />

    </div>
    <small v-if="!isValid" class="invalid-text">Es muss eine Datei ausgewählt sein</small>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.read-only-container {
  width: 100%;

  display: flex;
  justify-content: space-between;

  transition: 0.1s ease-in-out;
   &:hover {
    background-color: $gray-hover;
  }
}

.read-only-file-name {
  @include ellipsisTextOverflow();
  padding: spacing(s) spacing(s);

  border: 2px solid $dark-gray;
  border-right: none;
  display: flex;
  flex-grow: 1;
  align-self: stretch;
  align-items: center;
  overflow: hidden;
}

.edit-container {
  width: 100%;
  border: 2px dashed $black;
  padding: spacing(s) 0;
  transition: 0.1s ease-in-out;
  cursor: pointer;

  &:hover, &:focus {
    background-color: $gray-hover;
  }

  display: flex;
  justify-content: center;
}

.invalid {
  border: 2px dashed $red;
}

.edit-container-highlight {
  background-color: $gray-hover;
}

.file-drop-unselected {
  display: flex;
  overflow: hidden;
  padding-right: spacing(s);
}

input {
  display: none;
}

.ellipsis-text-overflow {
  @include ellipsisTextOverflow();
  width: 100%;
  padding: 0 spacing(s);
}
</style>
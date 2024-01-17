<!--
file input drop area with file dialog on click
props:
  - readonly
  - selectedFile (File object)
exposes:
  - selectedFile
displays:
  - drop area
  - file name inside drop area if file is selected
-->

<script setup>
import { ref } from "vue";
import { url } from "@/scripts/url-config";
import ButtonLink from "@/components/ButtonLink.vue";
import ButtonAdd from "@/components/ButtonAdd.vue";

const props = defineProps({
  readonly: {
    required: true,
    type: Boolean,
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
const selectedFile = ref(props.selectedFile)

const checkFile = (file) => {
  if (file.type !== 'application/pdf') {
    alert('Die Modulbeschreibung muss eine PDF sein.')
    return false
  }
  if (file.size > 10000000) {
    alert('Die Modulbeschreibung darf nicht größer als 10 MB sein.')
    return false
  }
  return true
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
    }
  }
}

const dragOverHandler = (e) => {
  if (!props.readonly) e.currentTarget.classList.add('file-drop-highlight')
}

const dragLeaveHandler = (e) => {
  if (!props.readonly) e.currentTarget.classList.remove('file-drop-highlight')
}

const dropHandler = (e) => {
  if (!props.readonly) {
    const file = e.dataTransfer.files[0]
    if (checkFile(file)) {
      selectedFile.value = file
    }
    e.currentTarget.classList.remove('file-drop-highlight')
  }
}

defineExpose({
  selectedFile
})
</script>

<template>
  <div>
    <div
        @click="openFileDialog"
        @dragover.prevent="dragOverHandler"
        @dragleave.prevent="dragLeaveHandler"
        @drop.prevent="dropHandler"
        class="file-drop-container"
        :class="{ 'cursor-pointer': !props.readonly }"
    >

      <div v-if="readonly">
        <p>{{ selectedFile?.name }}</p>
        <ButtonLink @click="openFile">PDF öffnen</ButtonLink>
      </div>

      <div v-else-if="selectedFile?.name">
        <p>{{ selectedFile?.name }}</p>
      </div>

      <div v-else class="file-drop-unselected">
        <p>Modulbeschreibung hochladen</p>
        <img src="../assets/icons/Upload.svg">
      </div>

    </div>
    <input type="file" ref="fileInput" @change="handleFiles" />
  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

input {
  display: none;
}

.cursor-pointer {
  cursor: pointer
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

.file-drop-highlight {
  background-color: $white;
}
</style>
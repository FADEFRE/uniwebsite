<!--
file input
displays:
- file input
functionality:
- file input
-->

<script setup>
import { ref, computed } from "vue";

let selectedFiles = ref()
const handleFiles = (e) => {
  selectedFiles.value = e.target.files
}
const descriptionFile = computed(() => selectedFiles.value ? selectedFiles.value.item(0) : null)

defineExpose({
  descriptionFile
})
</script>

<template>
  <div class="file-input-container">
    <input type="file" accept=".pdf" @change="handleFiles">
  </div>
</template>

<style scoped>

</style>
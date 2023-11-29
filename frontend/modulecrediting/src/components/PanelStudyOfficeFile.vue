<!--
link to file
displays:
- file name as link
functionality:
- opens file in new tab on click
-->

<script setup>
import { url } from "@/scripts/url-config";

const props = defineProps(['fileID', 'fileName'])
const fileLink = url + '/pdf-documents' + props.fileID
</script>

<template>
  <div>
    <a :href="fileLink" target="_blank">{{ props.fileName }}</a>
  </div>
</template>

<style scoped>

</style>
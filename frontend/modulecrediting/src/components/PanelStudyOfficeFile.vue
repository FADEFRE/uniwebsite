<!--
link to file
displays:
- file name as link
functionality:
- opens file in new tab on click
-->

<script setup>
import { url } from "@/scripts/url-config";

const props = defineProps(['fileId', 'fileName'])
const fileLink = url + '/pdf-documents/' + props.fileId
</script>

<template>
  <div>
    <p>Modulbeschreibung: <a :href="fileLink" target="_blank">{{ props.fileName }}</a></p>
  </div>
</template>

<style scoped>

</style>
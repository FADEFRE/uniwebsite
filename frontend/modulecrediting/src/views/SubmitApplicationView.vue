<script setup>
import ModuleApplicationPanel from "@/components/ModuleApplicationPanel.vue";
import { ref } from 'vue'

const selectedCourse = ref()
const courses = ref([
  "B.Sc. Informatik"
])
</script>

<template>
  <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen"/>
  <ModuleApplicationPanel />
</template>

<style scoped>
</style>

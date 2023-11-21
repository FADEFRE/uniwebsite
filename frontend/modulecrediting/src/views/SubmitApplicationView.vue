<script setup>
import ModuleApplicationPanel from "@/components/ModuleApplicationPanel.vue";
import NewModuleApplicationButton from "@/components/NewModuleApplicationButton.vue";
import { ref, reactive } from 'vue'


const selectedCourse = ref()
const courses = ref([
  "B.Sc. Informatik"
])

const moduleApplicationPanels = reactive({
  1: null
})
const moduleApplicationPanelsRef = ref([])

const addModuleApplication = () => {
  let nextKey = null
  if (Object.keys(moduleApplicationPanels).length === 0) {
    nextKey = 1
  } else {
    // noinspection JSCheckFunctionSignatures
    nextKey = Math.max(...Object.keys(moduleApplicationPanels)) + 1
  }
  moduleApplicationPanels[nextKey] = null
}

const deleteModuleApplication = (key) => {
  delete moduleApplicationPanels[key]
}
</script>

<template>
  <div class="view-container">
    <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen" class="course-dropdown"/>
    <ModuleApplicationPanel
        v-for="(value, key) in moduleApplicationPanels"
        :key="key"
        ref="moduleApplicationPanelsRef"
        @deletePanel="deleteModuleApplication(key)"
    />
    <NewModuleApplicationButton @add-module-application="addModuleApplication" />
  </div>
</template>

<style scoped>
.view-container{
  margin: 10px 0 20px 0;
}

.course-dropdown {
  margin: 10px;
}
</style>

<script setup>
import ModuleApplicationPanel from "@/components/ModuleApplicationPanel.vue";
import NewModuleApplicationButton from "@/components/NewModuleApplicationButton.vue";
import { ref, reactive, computed, provide } from "vue"
import { postApplication } from "@/scripts/axios-requests";
import { url } from "@/scripts/url-config"
import axios from "axios";

// courses
const selectedCourse = ref()

let courseData = ref()
axios.get(url + '/courses-leipzig')
    .then(response => {
      courseData.value = response.data
    })
// todo error catching

const courses = computed(() => courseData.value ? courseData.value.map((obj) => obj.name) : [])
const modules = computed(() => {
  if (courseData.value && selectedCourse.value) {
    const selectedCourseObject = courseData.value.find((course) => course.name === selectedCourse.value)
    const moduleObjects = selectedCourseObject["modulesLeipzigCourse"]
    return moduleObjects.map((module) => module.moduleName).sort()
  } else {
    return []
  }
})
provide('modules', modules)

// module applications
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

const resetSelectedModules = () => {
  for (let panelRef of moduleApplicationPanelsRef.value) {
    panelRef.dataRef.resetSelectedInternalModules()
  }
}

// send button
const triggerPostApplication = () => {
  const applicationsObject = moduleApplicationPanelsRef.value.map((obj) => obj['dataRef'])
  postApplication(selectedCourse, applicationsObject)
}
</script>

<template>
  <div class="view-container">

    <!-- courses -->
    <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen" class="course-dropdown" @change="resetSelectedModules"/>

    <!-- module applications -->
    <ModuleApplicationPanel
        v-for="(value, key) in moduleApplicationPanels"
        :key="key"
        ref="moduleApplicationPanelsRef"
        @deletePanel="deleteModuleApplication(key)"
    />
    <NewModuleApplicationButton @add-module-application="addModuleApplication" />

    <!-- send button -->
    <Button @click="triggerPostApplication">Absenden</Button>
  </div>
</template>

<style scoped>
.course-dropdown {
  margin: 10px;
}
</style>

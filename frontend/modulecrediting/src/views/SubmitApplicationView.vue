<!--
user can edit and submit an application
displays:
- course selection dropdown
- module panels (varying amount) VIA ApplicationModulePanel
- add module button VIA NewApplicationModulePanel
- submit button
functionality:
- course selection
- adding and deleting module panels
- submitting i.e. triggering /applications POST-request
-->

<script setup>
import ApplicationModulePanel from "@/components/ApplicationModulePanel.vue";
import NewApplicationModulePanel from "@/components/NewApplicationModulePanel.vue";
import { useRouter } from "vue-router"
import { ref, reactive, computed, onBeforeMount } from "vue"
import { postApplication } from "@/scripts/axios-requests";
import { getCourseData } from "@/scripts/axios-requests"

const router = useRouter()

// courses
const selectedCourse = ref()

const courseData = ref(undefined)

onBeforeMount(() => {
  getCourseData()
      .then(data => {
        courseData.value = data
      })
      .catch(error => {
        console.log(error)
        courseData.value = 'error'
      })
})

const courses = computed(() => courseData.value ? courseData.value.map((obj) => obj.name) : [])

// module applications
const moduleApplicationPanels = reactive({
  1: 'moduleApplication'
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
  moduleApplicationPanels[nextKey] = 'moduleApplication'
}

const deleteModuleApplication = (key) => {
  delete moduleApplicationPanels[key]
}

const resetInternalModules = () => {
  // getting modules
  let modules = []
  if (courses.value && courses.value !== 'error' && selectedCourse.value) {
    const selectedCourseObject = courseData.value.find((course) => course.name === selectedCourse.value)
    const moduleObjects = selectedCourseObject["modulesLeipzigCourse"]
    modules = moduleObjects.map((module) => module.moduleName).sort()
  }
  // resetting internalModules dropdowns
  for (let panel of moduleApplicationPanelsRef.value) {
    panel.internalModules.resetInternalModules(modules)
  }
}

// send button
const triggerPostApplication = () => {
  // unwrapping data
  const applicationsObject = moduleApplicationPanelsRef.value.map((panel) => {
    return {
      moduleName: panel.base.moduleName,
      university: panel.base.university,
      creditPoints: panel.base.creditPoints,
      pointSystem: panel.base.pointSystem,
      descriptionFile: panel.file.descriptionFile,
      selectedInternalModules: panel.internalModules.selectedInternalModules,
      comment: panel.comment.comment,
    }
  })
  // post request
  postApplication(selectedCourse.value, applicationsObject)
      .then((id) => {
        // routing to status page for application
         router.push({ name: 'confirmation', params: { id: id } });
      })
}
</script>

<template>

  <div v-if="!courseData">
    <p>Lade Daten ...</p>
  </div>

  <div v-else-if="courseData === 'error'">
    <p>Fehler bei der Datenabfrage!</p>
  </div>

  <div v-else class="view-container">

    <!-- courses -->
    <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen" class="course-dropdown" @change="resetInternalModules"/>

    <!-- module applications -->
    <ApplicationModulePanel
        v-for="(value, key) in moduleApplicationPanels"
        :key="key"
        ref="moduleApplicationPanelsRef"
        @deletePanel="deleteModuleApplication(key)"
    />
    <NewApplicationModulePanel @add-module-application="addModuleApplication" />

    <!-- send button -->
    <button @click="triggerPostApplication" class="submit-button">Absenden</button>
  </div>
</template>

<style scoped>
.course-dropdown {
  margin: 10px;
}
button.submit-button {
  display: inline-block;
  padding: 10px 20px;
  background-color: #8AC2D1;
  color: #000;
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
  border: none; 
  cursor: pointer; 
  
}


</style>

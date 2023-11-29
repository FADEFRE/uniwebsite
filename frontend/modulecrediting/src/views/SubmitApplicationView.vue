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
import { ref, reactive, computed, provide } from "vue"
import { postApplication } from "@/scripts/axios-requests";
import { url } from "@/scripts/url-config"
import axios from "axios";

const router = useRouter()

// courses
const selectedCourse = ref()

let courseData = ref()
axios.get(url + '/courses-leipzig')
    .then(response => {
      courseData.value = response.data
    })
// todo error catching

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
  if (courseData.value && selectedCourse.value) {
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
      comment: panel.comment.applicantComment,
    }
  })
  // post request
  postApplication(selectedCourse.value, applicationsObject)
      .then((id) => {
        // routing to status page for application
        const routeData = router.resolve({name: 'statusDetail', params: {id: id}})
        window.open(routeData.href, '_top')
      })
}
</script>

<template>
  <div class="view-container">

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
    <Button @click="triggerPostApplication">Absenden</Button>
  </div>
</template>

<style scoped>
.course-dropdown {
  margin: 10px;
}
</style>

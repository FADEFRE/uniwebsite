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
import router from "@/router";
import { ref, onBeforeMount } from "vue";
import { getFormattedDate } from "@/scripts/date-utils";
import { getCoursesLeipzig, getModulesByCourse, postApplication } from "@/scripts/axios-requests";
import ApplicationPanel from "@/components/ApplicationPanel.vue";

const creationDate = new Date()

const courses = ref()
const selectedCourse = ref()

onBeforeMount(() => {
  getCoursesLeipzig()
      .then(data => courses.value = data)
})

const selectableModules = ref([])
const setSelectableModules = () => {
  getModulesByCourse(selectedCourse.value)
      .then(data => selectableModules.value = data)
}

const moduleConnections = ref([1])
const moduleConnectionsRef = ref()

const addModuleConnection = () => {
  const nextIndex = Math.max(...moduleConnections.value) + 1
  moduleConnections.value.push(nextIndex)
}

const deleteModuleConnection = (key) => {
  moduleConnections.value = moduleConnections.value.filter(el => el !== key)
}

const triggerPostApplication = () => {
  console.log(selectedCourse.value)
  console.log(moduleConnectionsRef.value)
  postApplication(selectedCourse.value, moduleConnectionsRef.value)
      .then(id => {
        router.push({name: 'confirmation', params: {id: id}})
      })
}
</script>

<template>
  <div>

    <div>
      <div>
        <img src="../assets/icons/CreationDate.svg">
        <p>{{ getFormattedDate(creationDate) }}</p>
      </div>

      <div>
        <Dropdown
            v-model="selectedCourse"
            :options="courses"
            placeholder="Studiengang wählen"
            @change="setSelectableModules"
        />
      </div>

      <div>
        <p>Status: Neu</p>
      </div>
    </div>

    <div>
      <ApplicationPanel
          :selectable-modules="selectableModules"
          v-for="item in moduleConnections"
          :key="item"
          @delete-self="deleteModuleConnection(item)"
          ref="moduleConnectionsRef"
      />
      <Button @click="addModuleConnection">Modulzuweisung hinzufügen</Button>
      <Button @click="triggerPostApplication">Absenden</Button>
    </div>

    {{moduleConnections}}
    {{moduleConnectionsRef}}

  </div>
</template>

<style scoped>

</style>

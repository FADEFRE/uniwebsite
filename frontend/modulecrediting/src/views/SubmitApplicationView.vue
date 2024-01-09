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
import SideInfoContainer from "@/components/SideInfoContainer.vue";
import ButtonAdd from "@/components/ButtonAdd.vue";
import ButtonLink from "@/components/ButtonLink.vue";

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
      router.push({ name: 'confirmation', params: { id: id } })
    })
}
</script>

<template>
  <div class="main">

    <div class="submit-application-container">
      <div class="application-overview-container">
        <div>
          <img src="../assets/icons/CreationDate.svg">
          <p>{{ getFormattedDate(creationDate) }}</p>
        </div>

        <div>
          <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen"
            @change="setSelectableModules" />
        </div>

        <div>
          <p>Status: Neu</p>
        </div>
      </div>

      <ApplicationPanel :selectable-modules="selectableModules" v-for="item in moduleConnections" :key="item"
        @delete-self="deleteModuleConnection(item)" ref="moduleConnectionsRef" />

      <ButtonAdd @click="addModuleConnection">Modulzuweisung hinzufügen</ButtonAdd>
      <ButtonLink @click="triggerPostApplication">Absenden</ButtonLink>
    </div>

    <div class="side-infos-container">
      <!--SideInfoContainerfür Antragprozess -->
      <SideInfoContainer :heading="'ANTRAGSPROZESS'">
        <ul class="list-container">
          <li class="list-item">Antrag online stellen</li>
          <li class="list-item">Über Vorgangsnummer online Status einsehen</li>
          <li class="list-item">Auf Entscheidung des PAV warten</li>
          <li class="list-item">Mit abgeschlossenem Antrag zum Studienbüro gehen</li>
        </ul>
      </SideInfoContainer>
      <SideInfoContainer :heading="'STUDIENBÜRO'">
        <ul class="list-container">
          <li class="list-item">Antrag online stellen</li>
          <li class="list-item">Über Vorgangsnummer online Status einsehen</li>
          <li class="list-item">Auf Entscheidung des PAV warten</li>
          <li class="list-item">Mit abgeschlossenem Antrag zum Studienbüro gehen</li>
        </ul>
      </SideInfoContainer>
    </div>


  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
  @include main();
}

.submit-application-container {
  @include verticalList(small);
  width: 100%;
}

.application-overview-container {
  @include applicationOverview();
}

.side-infos-container {
  @include verticalList(big);
  width: min-content;
}
</style>


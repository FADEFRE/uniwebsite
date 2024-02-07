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
import ApplicationOverview from "@/components/ApplicationOverview.vue";

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

const courseValid = ref(true)

const checkValidity = () => {
  courseValid.value = Boolean(selectedCourse.value)
  const connectionValidity = moduleConnectionsRef.value.map(c => c.checkValidity()).every(Boolean)
  return courseValid.value && connectionValidity
}

const triggerPostApplication = () => {
  if (checkValidity()) {
    postApplication(selectedCourse.value, moduleConnectionsRef.value)
      .then(id => {
        router.push({ name: 'confirmation', params: { id: id } })
      })
  }
}
</script>

<template>
  <div class="main">

    <div class="content-container split">

      <ApplicationOverview :creation-date="getFormattedDate(creationDate)" :last-edited-date="undefined"
        :decision-date="undefined" status="NEU">
        <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen"
          @change="setSelectableModules" :class="{ 'invalid': !courseValid }">
          <template #dropdownicon>
            <img src="../assets/icons/ArrowWhite.svg">
          </template>
        </Dropdown>
        <small v-if="!courseValid" class="invalid-text">Es muss ein Studiengang ausgewählt werden</small>
      </ApplicationOverview>

      <ApplicationPanel v-for="item in moduleConnections" :key="item" :selectable-modules="selectableModules"
        :allow-delete="moduleConnections.length > 1" ref="moduleConnectionsRef"
        @delete-self="deleteModuleConnection(item)" />

      <ButtonAdd @click="addModuleConnection">Modulzuweisung hinzufügen</ButtonAdd>
      <ButtonLink @click="triggerPostApplication" :fixed="true">Absenden</ButtonLink>
    </div>

    <div class="side-infos-list">
      <!--SideInfoContainerfür Antragprozess -->
      <SideInfoContainer :heading="'ANTRAGSPROZESS'">
        <ul>
          <li>Antrag online stellen</li>
          <li>Über Vorgangsnummer online Status einsehen</li>
          <li>Auf Entscheidung des PAV warten</li>
          <li>Mit abgeschlossenem Antrag zum Studienbüro gehen</li>
        </ul>
      </SideInfoContainer>
      <SideInfoContainer :heading="'STUDIENBÜRO'">
        <p>Fakultät für Mathematik und Informatik</p>
        <div class="main-info-container">
          <div class="info-group-container">
            <h4>Anschrift</h4>
            <ul>
              <li>Neues Augusteum</li>
              <li>Augustusplatz 10</li>
              <li>04109 Leipzig</li>
            </ul>
          </div>
          <div class="info-group-container">
            <h4>Kontakt</h4>
            <ul>
              <li>Telefon: +49 341 97-32165</li>
              <li>Telefax: +49 341 97-32193</li>
              <li>E-Mail: studienbuero@math.uni-leipzig.de</li>
            </ul>
          </div>
          <div class="info-group-container">
            <h4>Sprechzeiten</h4>
            <p>Dienstag und Donnerstag: 9:00 - 11:30 Uhr und 12:30 - 16:00 Uhr</p>
          </div>
          <a href="https://www.mathcs.uni-leipzig.de/studium/studienbuero">
            <ButtonLink>
              Zum Studienbüro
            </ButtonLink>
          </a>
        </div>

      </SideInfoContainer>
    </div>


  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;


.arrow-icon {
  transform: rotate(-90deg);
  transition: 0.1s ease-in-out;
}

.invalid-text {
  color: $red;
}
</style>


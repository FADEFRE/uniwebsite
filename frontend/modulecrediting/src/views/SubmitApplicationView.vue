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
import { getCoursesLeipzigName, getModulesByCourse, postApplication } from "@/scripts/axios-requests";
import ApplicationPanel from "@/components/ApplicationPanel.vue";
import SideInfoContainer from "@/components/SideInfoContainer.vue";
import ButtonAdd from "@/components/ButtonAdd.vue";
import ButtonLink from "@/components/ButtonLink.vue";
import ApplicationOverview from "@/components/ApplicationOverview.vue";
import ArrowIcon from "../assets/icons/ArrowIcon.vue";

const creationDate = new Date()

const courses = ref()
const selectedCourse = ref()

onBeforeMount(() => {
  getCoursesLeipzigName()
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

      <div class="explanation-container">
        <h2>Informationen zum Antrag</h2>
        <p>
          Zuerst muss der Studiengang ausgewählt werden, für den Module angerechnet werden sollen.
          Danach können beliebig viele Modulzuweisungen hinzugefügt werden (eine Modulzuweisung ist eine Zuweisung
          von Fremdmodulen, die für Module der Universität Leipzig angerechnet werden sollen). In jeder Modulzuweisung
          muss mindestens ein Fremdmodul (an einer anderen Universität absolviertes Modul) angegeben werden, füllen sie
          für dieses alle Felder aus, die Modulbeschreibung muss eine PDF-Datei sein, die nur das relevante Modul
          umfasst. Wählen sie Module der Universität Leipzig aus, für die die angegebenen Fremdmodule angerechnet
          werden sollen. Optionale kann noch ein Kommentar mit weiteren Informationen angegeben werden.
        </p>
      </div>

      <ApplicationOverview :creation-date="getFormattedDate(creationDate)" :last-edited-date="undefined"
        :decision-date="undefined" status="NEU">
        <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen"
          @change="setSelectableModules" :class="{ 'invalid': !courseValid }">
          <template #dropdownicon>
            <ArrowIcon direction="down"/>
          </template>
        </Dropdown>
        <small v-if="!courseValid" class="invalid-text">Es muss ein Studiengang ausgewählt werden</small>
      </ApplicationOverview>

      <ApplicationPanel v-for="item in moduleConnections" :key="item" :selectable-modules="selectableModules"
        :allow-delete="moduleConnections.length > 1" ref="moduleConnectionsRef"
        @delete-self="deleteModuleConnection(item)" />

      <div class="application-buttons-container">
        <ButtonAdd @click="addModuleConnection">Modulzuweisung hinzufügen</ButtonAdd>
        <ButtonLink @click="triggerPostApplication" :redButton="true">Absenden</ButtonLink>
      </div>
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
          <a href="https://www.mathcs.uni-leipzig.de/studium/studienbuero" target="_blank" >
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
@use '@/assets/styles/components' as *;

.explanation-container {
  @include verticalListItem($white);
  @include basicContainer();
}
</style>


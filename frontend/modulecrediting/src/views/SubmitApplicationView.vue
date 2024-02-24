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
import ApplicationProcessSideInfo from "@/components/ApplicationProcessSideInfo.vue";
import StudyOfficeSideInfo from "@/components/StudyOfficeSideInfo.vue";

const showInformation = ref(false)

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

      <div class="application-info-container">
        <h2>Informationen</h2>
        <div class="explanation-collapsed-container">
        <p>
          Wählen Sie den Studiengang aus, für den Sie sich Leistungen anrechnen lassen wollen.
          Tragen sie dann die Details der Leistungen ein. Hierfür können sie beliebig viele Modulzuweisungen hinzufügen.
        </p>
        <Button @click="showInformation = !showInformation">
          Erklärung anzeigen
          <ArrowIcon :direction="showInformation ? 'up' : 'down'" color="red" />
        </Button>
      </div>
        <div v-if="showInformation" class="explanation-expanded-container">
          <div class="explanation-item">
            <h3 class="h4">Modulzuweisung</h3>
            <ul class="points">
              <li>Ein oder mehrere Fremdmodule die als Modul(e) der Universität Leipzig angerechnet werden sollen.</li>
              <li>Es sollen nur konkret zusammengehörige Module in eine Modulzuweisung geschrieben werden.</li>
              <li>Für den gesamten Antrag können sie mehrere Modulzuweisungen hinzufügen.</li>
              <!-- todo add example -->
              <li>!!! Beispiel hinzufügen !!!</li>
            </ul>
          </div>
          <div class="explanation-item">
            <h3 class="h4">Fremdmodul</h3>
            <ul class="points">
              <li>Ein konkretes Modul, das an einer anderen Universität belegt wurde.</li>
              <li>Hierfür müssen die gegebenen Felder ausgefüllt werden.</li>
              <li>Geben Sie das Punktesystem an (LP, ECTS oder ähnliches).</li>
              <li>Als Modulbeschreibung muss eine PDF-Datei mit der offiziellen Beschreibung des einzelnen Modul
                hochgeladen werden.</li>
            </ul>
          </div>
          <div class="explanation-item">
            <h3 class="h4">Module der Universität Leipzig</h3>
            <ul class="points">
              <li>Wählen sie ein oder mehrere Modul(e) aus, für die Sie ihre Leistungen anrechnen lassen wollen.</li>
              <li>Sollten sie kein passendes Modul finden, besteht auch die Möglichkeit, keines auszuwählen.</li>
            </ul>
          </div>
          <div class="explanation-item">
            <h3 class="h4">Kommentar</h3>
            <ul class="points">
              <li>Hier können weitere Informationen zu dieser Modulzuweisung mitgeteilt werden, dies ist optional.</li>
            </ul>
          </div>
        </div>
      </div>

      <ApplicationOverview :creation-date="getFormattedDate(creationDate)" :last-edited-date="undefined"
        :decision-date="undefined" status="NEU">
        <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen"
          @change="setSelectableModules" :class="{ 'invalid': !courseValid }">
          <template #dropdownicon>
            <ArrowIcon direction="down" />
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
      <ApplicationProcessSideInfo />
      <StudyOfficeSideInfo />
    </div>


  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.explanation-collapsed-container {
  @include verticalList(s);
}
.explanation-expanded-container {
  @include verticalList(s);
  border-top: 2px solid $dark-gray;
  margin-top: spacing(s);
  padding-top: spacing(s);
}

.explanation-item {
}
</style>


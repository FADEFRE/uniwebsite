<!--
filter selectors
exposes:
  - searchString
  - dateType (may be 'creation', 'lastEdit' or 'decision')
  - earliestDate
  - statusTypes (array, may include 'new', 'open', 'studyOffice', 'pav', 'closed')
  - selectedCourse (may be course registered in database)
-->

<script setup>
import {ref, computed, onBeforeMount} from "vue";
import { getWeekAgo, getMonthAgo, getSixMonthAgo, getYearAgo } from "@/scripts/date-utils";
import { getCoursesLeipzig } from "@/scripts/axios-requests";

const searchString = ref()

const dateType = ref('creation')  // creation, lastEdit or decision
const dateOptions = ['Letzte Woche', 'Letzter Monat', 'Letzte 6 Monate', 'Letztes Jahr', 'Alle']
const selectedDateOption = ref('Letzter Monat')

const earliestDate = computed(() => {
  if (selectedDateOption.value === 'Letzte Woche') return getWeekAgo()
  if (selectedDateOption.value === 'Letzter Monat') return getMonthAgo()
  if (selectedDateOption.value === 'Letzte 6 Monate') return getSixMonthAgo()
  if (selectedDateOption.value === 'Letztes Jahr') return getYearAgo()
  return undefined
})

const statusNew = ref(true)
const statusStudyOffice = ref(true)
const statusChairman = ref(true)
const statusClosed = ref(true)

const statusTypes = computed(() => {
  const statusList = []
  if (statusNew.value) statusList.push('NEU')
  if (statusStudyOffice.value) statusList.push('STUDIENBÜRO')
  if (statusChairman.value) statusList.push('PRÜFUNGSAUSSCHUSS')
  if (statusClosed.value) statusList.push('ABGESCHLOSSEN')
  return statusList
})

let courses = ref([])
const course = ref()

onBeforeMount(() => {
  getCoursesLeipzig()
      .then(data => courses.value = data)
})

defineExpose({
  searchString, dateType, earliestDate, statusTypes, course
})

</script>

<template>
  <div class="filter-container">

    <h3>Filteroptionen</h3>

    <div>
      <h4>Suchen</h4>
      <InputText v-model="searchString" placeholder="Antrag suchen">
        <!-- todo place search icon -->
      </InputText>
      <small>Suchen nach: Vorgangsnummer</small>
    </div>

    <div>
      <h4>Allgemein</h4>
      <div>
        <div @click="dateType = 'creation'">
          <img src="../assets/icons/CreationDate.svg">
          <p v-if="dateType === 'creation'">Erstellt</p>
        </div>
        <div @click="dateType = 'lastEdit'">
          <img src="../assets/icons/LastEditedDate.svg">
          <p v-if="dateType === 'lastEdit'">Zuletzt bearbeitet</p>
        </div>
        <div @click="dateType = 'decision'">
          <img src="../assets/icons/DecisionDate.svg">
          <p v-if="dateType === 'decision'">Beschlossen</p>
        </div>
      </div>
      <div>
        <Dropdown v-model="selectedDateOption" :options="dateOptions" />
      </div>
    </div>

    <div>
      <!-- todo add correct styles -->
      <h4>Status</h4>
      <div :class="{ 'selected': statusNew }" @click="statusNew ? statusNew=false : statusNew=true">
        <p>NEU</p>
      </div>
      <div :class="{ 'selected': statusStudyOffice }" @click="statusStudyOffice ? statusStudyOffice=false : statusStudyOffice=true">
        <p>STUDIENBÜRO</p>
      </div>
      <div :class="{ 'selected': statusChairman }" @click="statusChairman ? statusChairman=false : statusChairman=true">
        <p>PRÜFUNGSAUSSCHUSS</p>
      </div>
      <div :class="{ 'selected': statusClosed }" @click="statusClosed ? statusClosed=false : statusClosed=true">
        <p>ABGESCHLOSSEN</p>
      </div>
    </div>

    <div>
      <h4>Studiengang</h4>
      <Dropdown show-clear
          v-model="course"
          :options="courses"
          placeholder="Studiengang auswählen"
      />
    </div>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.filter-container {
  @include basicContainer();
}
.selected {
  background-color: salmon;
}
</style>
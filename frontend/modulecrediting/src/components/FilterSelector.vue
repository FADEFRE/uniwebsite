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
import { ref, computed, onBeforeMount } from "vue";
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

const deleteCourse = (e) => {
  course.value = null;
}

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

    <div class="search-container">
      <h4>Suchen</h4>
      <InputText v-model="searchString" placeholder="Antrag suchen">
        <!-- todo place search icon -->
      </InputText>
      <small>Suchen nach: Vorgangsnummer, Modulname, Universität</small>
    </div>

    <div class="general-container">
      <h4>Allgemein</h4>
      <div class="date-filter-container">
        <div @click="dateType = 'creation'" class="date-block" :class="{ 'selected': dateType === 'creation' }">
          <img src="../assets/icons/CreationDate.svg">
          <p v-if="dateType === 'creation'">Erstellt</p>
        </div>
        <div @click="dateType = 'lastEdit'" class="date-block" :class="{ 'selected': dateType === 'lastEdit' }">
          <img src="../assets/icons/LastEditedDate.svg">
          <p v-if="dateType === 'lastEdit'">Zuletzt bearbeitet</p>
        </div>
        <div @click="dateType = 'decision'" class="date-block" :class="{ 'selected': dateType === 'decision' }">
          <img src="../assets/icons/DecisionDate.svg">
          <p v-if="dateType === 'decision'">Beschlossen</p>
        </div>
      </div>
      <div>
        <Dropdown v-model="selectedDateOption" :options="dateOptions" />
      </div>
    </div>

    <div class="status-list-container">
      <!-- todo add correct styles -->
      <h4>Status</h4>
      <div :class="{ 'selected': statusNew }" class="statusNew status-container"
        @click="statusNew ? statusNew = false : statusNew = true">
        <p class="overview-text">NEU</p>
      </div>
      <div :class="{ 'selected': statusStudyOffice }" class="statusStudyOffice status-container"
        @click="statusStudyOffice ? statusStudyOffice = false : statusStudyOffice = true">
        <p class="overview-text">STUDIENBÜRO</p>
      </div>
      <div :class="{ 'selected': statusChairman }" class="statusChairman status-container"
        @click="statusChairman ? statusChairman = false : statusChairman = true">
        <p class="overview-text">PRÜFUNGSAUSSCHUSS</p>
      </div>
      <div :class="{ 'selected': statusClosed }" class="statusClosed status-container"
        @click="statusClosed ? statusClosed = false : statusClosed = true">
        <p class="overview-text">ABGESCHLOSSEN</p>
      </div>
    </div>

    <div class="course-container">
      <h4>Studiengang</h4>
      <Dropdown show-clear v-model="course" :options="courses" placeholder="Studiengang auswählen">
              <template #clearicon>
                <img src="@/assets/icons/TrashWhite.svg" class="clear-icon" @click="deleteCourse">
              </template>
              <template #dropdownicon>
                  <img src="../assets/icons/ArrowWhite.svg">
              </template>
      </Dropdown>
    </div>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.clear-icon {
  opacity: 0.9;
  transition: 0.1s ease-in-out;
  &:hover{
    opacity: 1;
  }
}
.arrow-icon {
  opacity: 0.9;
  transition: 0.1s ease-in-out;
  &:hover{
    opacity: 1;
  }
}
.filter-container {
  @include basicContainer();
  width: 30%;
}

.general-container {
  @include verticalList(small);
  width: 100%;
}

.course-container {
  width: 100%;
}

.status-list-container {
  @include verticalList(small);
  gap: 0.2rem;
  width: 100%;
}

.date-filter-container {
  border: 2px $dark-gray solid;

  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  align-self: stretch;
}

.date-block {
  @include smallHighlightBox();
  width: 20%;
  display: flex;

  &.selected {
    background-color: $gray;
    width: 60%;
  }
}

.status-container {
  @include smallHighlightBox();
  background-color: $gray;
  width: 100%;
}

.overview-text {
  color: $dark-gray;
}

.p-inputtext {
  width: 100%;
}
.selected {
  & .overview-text {
    color: $white;
  }

  &.statusNew {
    background-color: $green;
  }

  &.statusStudyOffice {
    background-color: $orange;
  }

  &.statusChairman {
    background-color: $orange;
  }

  &.statusClosed {
    background-color: $red;
  }
}
</style>
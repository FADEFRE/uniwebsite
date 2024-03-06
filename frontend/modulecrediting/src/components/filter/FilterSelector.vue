<!--
filter selectors
exposes:
  - searchString
  - dateType (may be 'creationDate', 'lastEditedDate' or 'decisionDate')
  - earliestDate
  - statusTypes (array, may include 'new', 'open', 'studyOffice', 'pav', 'closed')
  - selectedCourse (may be course registered in database)
-->

<script setup>
import { ref, computed, onBeforeMount } from "vue";
import DateIcon from '@/assets/icons/DateIcon.vue';
import CustomDropdown from "@/components/util/CustomDropdown.vue";
import { getCoursesLeipzigName } from "@/requests/module-course-requests";
import { getWeekAgo, getMonthAgo, getSixMonthAgo, getYearAgo } from "@/utils/date-utils";

const searchString = ref()

const dateType = ref('creationDate')  // creationDate, lastEditedDate or decisionDate
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
const statusFormalRejection = ref(true)
const statusStudyOffice = ref(true)
const statusChairman = ref(true)
const statusClosed = ref(true)

const statusTypes = computed(() => {
  const statusList = []
  if (statusNew.value) statusList.push('NEU')
  if (statusFormalRejection.value) statusList.push('FORMFEHLER')
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
  getCoursesLeipzigName()
    .then(data => courses.value = data)
})

const setDateTypeCreation = () => {
  // setting dateType
  dateType.value = 'creationDate'
  // setting status
  statusNew.value = true
  statusFormalRejection.value = true
  statusStudyOffice.value = true
  statusChairman.value = true
  statusClosed.value = true
}

const setDateTypeLastEdit = () => {
  // setting dateType
  dateType.value = 'lastEditedDate'
  // setting status
  statusNew.value = false
  statusFormalRejection.value = true
  statusStudyOffice.value = true
  statusChairman.value = true
  statusClosed.value = true
}

const setDateTypeDecision = () => {
  // setting dateType
  dateType.value = 'decisionDate'
  // setting status
  statusNew.value = false
  statusFormalRejection.value = false
  statusStudyOffice.value = false
  statusChairman.value = false
  statusClosed.value = true
}

const toggleStatusNew = () => {
  statusNew.value = !statusNew.value
  // change dateType if necessary
  if (dateType.value === 'decisionDate' || dateType.value === 'lastEditedDate') {
    dateType.value = 'creationDate'
  }
}

const toggleStatusFormalRejection = () => {
  statusFormalRejection.value = !statusFormalRejection.value
  // change dateType if necessary
  if (dateType.value === 'decisionDate') {
    dateType.value = 'lastEditedDate'
  }
}

const toggleStatusStudyOffice = () => {
  statusStudyOffice.value = !statusStudyOffice.value
  // change dateType if necessary
  if (dateType.value === 'decisionDate') {
    dateType.value = 'lastEditedDate'
  }
}

const toggleStatusChairman = () => {
  statusChairman.value = !statusChairman.value
  // change dateType if necessary
  if (dateType.value === 'decisionDate') {
    dateType.value = 'lastEditedDate'
  }
}

const toggleStatusClosed = () => {
  statusClosed.value = !statusClosed.value
}

defineExpose({
  searchString, dateType, earliestDate, statusTypes, course
})

</script>

<template>
  <div class="filter-container">

    <h2 class="h3">Filteroptionen</h2>

    <div class="screen-split-container">

      <div class="left-side-container">
        <div class="single-filter-container">
          <h3 class="h4">Studiengang</h3>
          <CustomDropdown show-clear placeholder="Studiengang auswählen" :options="courses" v-model="course" @clear="deleteCourse" />
        </div>


        <div class="single-filter-container">
          <h3 class="h4">Suchen</h3>
          <div class="input-search-field-container">
            <InputText v-model="searchString" placeholder="Antrag suchen" class="white"/>
            <img src="../../assets/icons/SearchIcon.svg" class="search-icon">
          </div>

          <small>Suchen nach: Vorgangsnummer, Modulname, Universität</small>
        </div>

        <div class="single-filter-container">
          <h3 class="h4">Zeit</h3>
          <div class="date-filter-container">
            <div tabindex="0" @keydown.enter="setDateTypeCreation" @click="setDateTypeCreation" class="date-block" :class="{ 'selected': dateType === 'creationDate' }">
              <DateIcon type="creation"/>
              <p v-if="dateType === 'creationDate'">Erstellt</p>
            </div>
            <div tabindex="0" @keydown.enter="setDateTypeLastEdit" @click="setDateTypeLastEdit" class="date-block" :class="{ 'selected': dateType === 'lastEditedDate' }">
              <DateIcon type="lastEdited"/>
              <p v-if="dateType === 'lastEditedDate'">Zuletzt bearbeitet</p>
            </div>
            <div tabindex="0" @keydown.enter="setDateTypeDecision" @click="setDateTypeDecision" class="date-block" :class="{ 'selected': dateType === 'decisionDate' }">
              <DateIcon type="decision"/>
              <p v-if="dateType === 'decisionDate'">Beschlossen</p>
            </div>
          </div>
          <div>
            <CustomDropdown v-model="selectedDateOption" :options="dateOptions" />
          </div>
        </div>
      </div>

      <div class="right-side-container">
        <div class="single-filter-container">
          <h3 class="h4">Status</h3>
          <div tabindex="0" @keydown.enter="toggleStatusNew" @click="toggleStatusNew" :class="{ 'selected': statusNew }" class="statusNew status-container" >
            <p class="overview-text">NEU</p>
          </div>
          <div tabindex="0" @keydown.enter="toggleStatusFormalRejection" @click="toggleStatusFormalRejection"
              :class="{ 'selected': statusFormalRejection }" class="statusFormalRejection status-container">
            <p class="overview-text">FORMFEHLER</p>
          </div>
          <div tabindex="0" @keydown.enter="toggleStatusStudyOffice" @click="toggleStatusStudyOffice"
              :class="{ 'selected': statusStudyOffice }" class="statusStudyOffice status-container">
            <p class="overview-text">STUDIENBÜRO</p>
          </div>
          <div tabindex="0" @keydown.enter="toggleStatusChairman" @click="toggleStatusChairman" :class="{ 'selected': statusChairman }" class="statusChairman status-container">
            <p class="overview-text">PRÜFUNGSAUSSCHUSS</p>
          </div>
          <div tabindex="0" @keydown.enter="toggleStatusClosed" @click="toggleStatusClosed" :class="{ 'selected': statusClosed }" class="statusClosed status-container">
            <p class="overview-text">ABGESCHLOSSEN</p>
          </div>
        </div>
      </div>


    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.filter-container {
  @include basicContainer();
}

.screen-split-container {
  width: 100%;
  @include verticalList(s);

  @include breakpoint(l) {
    @include breakpoint-up(s) {
      @include screenSplit();
    }
  }
}


.left-side-container {
  @include verticalList(s);
  @include breakpoint(l) {
    @include breakpoint-up(s) {
    width: 50%;
    }
  }
}
.right-side-container {
  width: 100%;
  @include breakpoint(l) {
    @include breakpoint-up(s) {
    width: 50%;
    }
  }
}

.single-filter-container {
  @include verticalList(xs);
}


.input-search-field-container {
  @include searchFieldContainer();
}

.date-filter-container {
  border: 2px $dark-gray solid;

  display: flex;
  justify-content: space-between;
  align-items: center
}

.date-block {
  @include smallHighlightBox();
  width: 20%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: $white;
  cursor: pointer;

  &.selected {
    background-color: $gray;
    width: 60%;

    &:hover, &:focus {
      background-color: $gray-hover;
    }
  }

  &:hover, &:focus {
    background-color: $white-hover;
  }
}


.status-container {
  @include smallHighlightBox();
  background-color: $gray;
  width: 100%;
  transition: 0.1s ease-in-out;
  cursor: pointer;

  &:hover, &:focus {
    background-color: $gray-hover;
  }
}

.selected {
  .overview-text {
    color: $white;
  }

  &.statusNew {
    background-color: $green;

    &:hover, &:focus {
      background-color: $green-hover;
    }
  }

  &.statusStudyOffice,
  &.statusChairman {
    background-color: $orange;

    &:hover, &:focus {
      background-color: $orange-hover;
    }
  }

  &.statusFormalRejection,
  &.statusClosed {
    background-color: $red;

    &:hover, &:focus {
      background-color: $red-hover;
    }
  }
}

.overview-text {
  color: $dark-gray;
}
</style>
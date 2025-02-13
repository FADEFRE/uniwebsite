<script setup>
import { ref, computed, onBeforeMount } from "vue";
import ManagementListElement from "@/components/management/ManagementListElement.vue";
import LoadingContainer from "@/components/util/LoadingContainer.vue";
import { deleteCourseLeipzig, getCoursesLeipzigName, putUpdateCourseLeipzig } from "@/requests/module-course-requests";
import {consoleDebug} from "@/requests/consoleDebug";

/*
list of all courses
 */

const courses = ref();
const searchString = ref('');

const exists = ref(false)

onBeforeMount(() => {
    getCoursesLeipzigName()
        .then(data => courses.value = data)
})

const triggerEditCourseLeipzig = (existsVar, oldName, newName) => {
  putUpdateCourseLeipzig(oldName, newName)
      .then(_ => location.reload())
      .catch(error => {
        if (error.response.status === 409) {
          existsVar.value = true
          consoleDebug('color:red', 'handled 409 in AccountAdminCreate')
        }
      })
}

const triggerDeleteCourseLeipzig = (course) => {
    deleteCourseLeipzig(course)
        .then(_ => location.reload())
}

const filteredCourses = computed(() => {
    if (searchString.value === '') return courses.value;

    return courses.value.filter(course => {
        return course.toLocaleLowerCase().includes(searchString.value.toLocaleLowerCase());
    })
})

</script>

<template>
  <div class="view-course-container">
    <h2>ALLE STUDIENGÄNGE</h2>

    <div v-if="courses" class="view-course-list">

      <div class="search-container">
        <InputText v-model="searchString" placeholder="Studiengang suchen"></InputText>
        <img src="../../assets/icons/SearchIcon.svg" class="search-icon">
      </div>
      <div v-for="course in filteredCourses" class="course-list">
        <ManagementListElement
            :key="course"
            :name="course"
            :show-code="false"
            :edit-callback="triggerEditCourseLeipzig"
            :delete-callback="triggerDeleteCourseLeipzig"
            :exists="exists"
            exists-text="Name des Studiengangs existiert bereits"
        />
      </div>
      <small class="helper-text">Es gibt insgesamt {{ courses.length }} Studiengänge.</small>

    </div>
    <div v-else class="view-course-loading-container">
      <LoadingContainer />
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.view-course-container {
    @include basicContainer();
}

.view-course-list,
.view-course-loading-container {
    @include verticalList(s);
}

.search-container {
    @include searchFieldContainer();
}

.course-list {
  @include verticalList(s);
}
</style>
<script setup>
import { ref, computed, onBeforeMount } from "vue";
import ManagementListElement from "./ManagementListElement.vue";
import LoadingContainer from "@/components/LoadingContainer.vue";
import { getCoursesLeipzigName, putUpdateCourseLeipzig, deleteCourseLeipzig } from "@/scripts/axios-requests";

const courses = ref();
const searchString = ref('');

onBeforeMount(() => {
    getCoursesLeipzigName()
        .then(data => courses.value = data)
})

const triggerEditCourseLeipzig = (oldName, newName) => {
  putUpdateCourseLeipzig(oldName, newName)
      .then(_ => location.reload())
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

    <div v-if="courses">

      <div class="search-container">
        <InputText v-model="searchString" placeholder="Studiengang suchen"></InputText>
        <img src="@/assets/icons/SearchIcon.svg" class="search-icon">
      </div>
      <div v-for="course in filteredCourses" class="course-list">
        <ManagementListElement
            :key="course"
            :name="course"
            :show-code="false"
            :edit-callback="triggerEditCourseLeipzig"
            :delete-callback="triggerDeleteCourseLeipzig"
        />
      </div>

    </div>
    <div v-else>
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

.search-container {
    @include searchFieldContainer();
}

.course-list {
  @include verticalList(s);
}
</style>
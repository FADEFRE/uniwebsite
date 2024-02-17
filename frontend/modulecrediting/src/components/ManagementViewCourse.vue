<script setup>
import ManagementListElement from "./ManagementListElement.vue";
import { ref, onBeforeMount, computed } from "vue";
import { getCoursesLeipzigName, deleteCourseLeipzig } from "@/scripts/axios-requests";
import {putUpdateCourseLeipzig} from "../scripts/axios-requests";

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
        <div class="search-container">
            <InputText v-model="searchString" placeholder="Studiengang suchen"></InputText>
            <img src="@/assets/icons/SearchIcon.svg" class="search-icon">
        </div>
        <div v-for="course in filteredCourses" class="course-item">
            <ManagementListElement
                :name="course"
                :edit-callback="triggerEditCourseLeipzig"
                :delete-callback="triggerDeleteCourseLeipzig"
            />
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

.course-item {
    @include smallHighlightBox();
    @include verticalListItem($gray);
    width: 100%;
}


.icons-container {
    display: flex;
    align-items: center;
    gap: spacing(xs);
}

.trash-icon-container {
  @include smallHighlightBox();
  transition: 0.1s ease-in-out;
  &:hover {
    background-color: $gray-hover;
  }
}

.edit-icon {
    @include smallHighlightBox();
  transition: 0.1s ease-in-out;

    &:hover {
        background-color: $gray-hover;
    }
}
</style>
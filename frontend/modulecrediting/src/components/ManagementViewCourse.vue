<script setup>
import { getCoursesLeipzig } from "@/scripts/axios-requests";
import { ref, onBeforeMount } from "vue";

const courses = ref();

onBeforeMount(() => {
    getCoursesLeipzig()
        .then(data => courses.value = data)
})
</script>

<template>
    <div class="view-course-container">
        <h2>ALLE STUDIENGÄNGE</h2>
        <div class="search-container">
            <InputText v-model="searchString" placeholder="Studiengang suchen"></InputText>
            <img src="@/assets/icons/SearchIcon.svg" class="search-icon">
        </div>
        <div v-for="course in courses" class="course-item">
            <p>{{ course }}</p>
            <div class="icons-container">
                <img src="@/assets/icons/EditIcon.svg" class="edit-icon">
                <img src="@/assets/icons/Trash.svg" class="trash-icon">
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

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
    gap: 0.625rem;
}
.trash-icon {
    @include trashIconAnimation();

    &:hover {
        background-color: $gray-hover;
    }
}
.edit-icon {
     @include trashIconAnimation();
     &:hover {
        background-color: $gray-hover;
    }
}

</style>
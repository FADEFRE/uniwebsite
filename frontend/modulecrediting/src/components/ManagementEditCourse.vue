<script setup>
import { getCoursesLeipzig, getModulesNameCodeByCourse, getModulesNameCode, putCourseLeipzigEdit } from "@/scripts/axios-requests";
import ButtonLink from "@/components/ButtonLink.vue"
import { ref, onBeforeMount, computed } from "vue";

const courses = ref()
const selectedCourse = ref()

const allModules = ref([])
const selectedModules = ref([])

const deleteCourse = () => {
    selectedCourse.value = null;
    selectedModules.value = [];
};

onBeforeMount(() => {
    getCoursesLeipzig()
        .then(data => courses.value = data)
    getModulesNameCode()
        .then(data => allModules.value = data)
})


const setSelectedModules = () => {
    getModulesNameCodeByCourse(selectedCourse.value)
        .then(data => selectedModules.value = data)
}

const selectableModules = computed(() => {
    const selectedModuleNames = selectedModules.value.map((singleModule) => {
        return singleModule.name;
    });

    return allModules.value.filter((singleModule) => {
        return !selectedModuleNames.includes(singleModule.name);
    });
});



const addModuleToCourse = (selectedModule) => {
    // Ensure the module is not already in the selectedModules list
    if (!selectedModules.value.some(module => module.name === selectedModule.name)) {
        selectedModules.value.unshift(selectedModule);
    }
}

const removeModuleFromCourse = (selectedModule) => {
    // Filter out the module to be removed from the selectedModules list
    selectedModules.value = selectedModules.value.filter(module => module.name !== selectedModule.name);
}

const saveCourseLeipzig = () => {
    putCourseLeipzigEdit(selectedCourse.value, selectedModules.value)
        .then((coursename) => {
            deleteCourse()
        })
}
</script>

<template>
    <div class="management-edit-course-container">
        <div class="select-header-container">
            <h2>STUDIENGANG BEARBEITEN</h2>
            <Dropdown v-model="selectedCourse" :options="courses" placeholder="Studiengang wählen"
                @change="setSelectedModules" class="dropdown" v-if="!selectedCourse">
                <template #dropdownicon>
                    <img src="../assets/icons/ArrowWhite.svg">
                </template>
            </Dropdown>
            <div v-else class="saving-buttons-container">
                <ButtonLink @click="deleteCourse">Änderung verwerfen</ButtonLink>
                <ButtonLink @click="saveCourseLeipzig" :redButton="true">Speichern</ButtonLink>
            </div>
        </div>

        <div v-if="selectedCourse" class="screen-split">
            <div class="selectable-modules-container">
                <h4>Wählbare Module</h4>
                <div class="search-container">
                    <InputText v-model="searchString" placeholder="Modul suchen"></InputText>
                    <img src="@/assets/icons/SearchIcon.svg" class="search-icon">
                </div>

                <div v-for="singleModule in selectableModules" :key="singleModule.name"
                    @click="addModuleToCourse(singleModule)" class="single-selectable-module-item">
                    <div class="module-text-container">
                        <p>{{ singleModule.name }}</p>
                        <small>{{ singleModule.code }}</small>
                    </div>
                    <img src="@/assets/icons/ArrowRed.svg" class="arrow-icon">
                </div>

            </div>

            <div class="selected-modules-container">
                <h4>Module in {{ selectedCourse }}</h4>

                <div v-for="singleModule in selectedModules" @click="removeModuleFromCourse(singleModule)"
                    class="single-selected-module-item">
                    <img src="@/assets/icons/ArrowRed.svg" class="arrow-icon">
                    <div class="module-text-container">
                        <p>{{ singleModule.name }}</p>
                        <small>{{ singleModule.code }}</small>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.management-edit-course-container {
    @include basicContainer();
}

.select-header-container {
    width: 100%;

    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 1rem 1.5rem;
}

.dropdown {
    width: min-content;
}

.saving-buttons-container {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem
}


.screen-split {
    @include screenSplit();
    gap: 3rem;
    width: 100%;
}

.selectable-modules-container {
    @include verticalList(small);
    width: 100%;
}

.search-container {
    width: 100%;
    display: flex;
    flex-direction: column;
    position: relative;
}

.p-inputtext {
    &:hover {
        background-color: $white-hover;
    }
}

.search-icon {
    width: 1rem;
    height: 1rem;
    position: absolute;
    right: 1rem;
    top: 50%;
    transform: translateY(-50%);
}

.single-selectable-module-item {
    @include smallHighlightBox();
    @include verticalListItem($gray);
    width: 100%;
    transition: 0.1s ease-in-out;

    display: flex;
    justify-content: space-between;
    gap: 0.8rem;


    & .arrow-icon {
        @include rightArrow();
        transition: 0.1s ease-in-out;
    }

    & .module-text-container {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
    }

    &:hover {
        background-color: $gray-hover;

        & .arrow-icon {
            transform: translateX(0.15rem) rotate(-90deg);
        }
    }
}

.selected-modules-container {
    @include verticalList(small);
    width: 100%;
}

.single-selected-module-item {
    @include smallHighlightBox();

    width: 100%;
    background-color: $gray;
    border-right: 3px solid $red;
    transition: 0.1s ease-in-out;

    display: flex;
    justify-content: space-between;
    gap: 0.8rem;


    & .arrow-icon {
        transform: rotate(90deg);
        transition: 0.1s ease-in-out;
    }

    & .module-text-container {
        display: flex;
        flex-direction: column;
        align-items: flex-end;

        & p,
        small {
            text-align: right;
        }
    }

    &:hover {
        background-color: $gray-hover;

        & .arrow-icon {
            transform: translateX(-0.15rem) rotate(90deg);
        }
    }
}</style>
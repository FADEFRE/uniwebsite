<script setup>
import { ref, onBeforeMount, computed } from "vue";
import ButtonLink from "@/components/ButtonLink.vue"
import { getCoursesLeipzig, getModulesNameCodeByCourse, getModulesNameCode, putCourseLeipzigEdit } from "@/scripts/axios-requests";

const courses = ref()
const selectedCourse = ref()

const allModules = ref([])

// allModules of selected course
const addedModules = ref([])
const selectedModules = ref([])

const selectableModules = computed(() => {
    const selectedModuleNames = [...selectedModules.value, ...addedModules.value].map(singleModule => singleModule.name);

    const selectableModules = allModules.value.filter(singleModule => !selectedModuleNames.includes(singleModule.name));

    if (searchString.value === '') return selectableModules;

    return selectableModules.filter(singleModule => {
        return singleModule.name.toLocaleLowerCase().includes(searchString.value.toLocaleLowerCase());
    })
});

// search in selectable modules
const searchString = ref('');

onBeforeMount(() => {
    getCoursesLeipzig()
        .then(data => courses.value = data)
    getModulesNameCode()
        .then(data => allModules.value = data)
})

const discardEditCourseChanges = () => {
    selectedCourse.value = null;
    selectedModules.value = [];
    addedModules.value = [];
}

const setSelectedModules = () => {
    getModulesNameCodeByCourse(selectedCourse.value)
        .then(data => selectedModules.value = data)
}

const addModuleToCourse = (clickedModule) => {
    addedModules.value.unshift(clickedModule);
}

const removeModuleFromCourse = (clickedModule) => {
    selectedModules.value = selectedModules.value.filter(singleModule => singleModule.name !== clickedModule.name);
    addedModules.value = addedModules.value.filter(singleModule => singleModule.name !== clickedModule.name);
}

const saveCourseLeipzig = () => {
    putCourseLeipzigEdit(selectedCourse.value, [...selectedModules.value, ...addedModules.value])
        .then(_ => location.reload())
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
                <ButtonLink @click="discardEditCourseChanges">Änderung verwerfen</ButtonLink>
                <ButtonLink @click="saveCourseLeipzig" :redButton="true">Speichern</ButtonLink>
            </div>
        </div>

        <div v-if="selectedCourse" class="screen-split">
            <div class="selectable-modules-container">
                <h3>Wählbare Module</h3>
                <div class="input-search-field-container">
                    <InputText v-model="searchString" placeholder="Modul suchen" />
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

                <div v-if="selectableModules.length === 0">
                    <p>Alle Module sind dem Studiengang hinzugefügt.</p>
                </div>

            </div>

            <div class="selected-modules-container">
                <h3>Module in {{ selectedCourse }}</h3>

                <div v-for="singleModule in addedModules" @click="removeModuleFromCourse(singleModule)"
                    class="single-selected-module-item">
                    <img src="@/assets/icons/ArrowRed.svg" class="arrow-icon">
                    <div class="module-text-container">
                        <p>{{ singleModule.name }}</p>
                        <small>{{ singleModule.code }}</small>
                    </div>
                </div>

                <div v-if="addedModules.length" class="break-container">
                    <div class="break-line"></div>
                    <h4 class="break-text">NEU</h4>
                </div>

                <div v-for="singleModule in selectedModules" @click="removeModuleFromCourse(singleModule)"
                    class="single-selected-module-item">
                    <img src="@/assets/icons/ArrowRed.svg" class="arrow-icon">
                    <div class="module-text-container">
                        <p>{{ singleModule.name }}</p>
                        <small>{{ singleModule.code }}</small>
                    </div>
                </div>

                <div v-if="addedModules.length === 0 && selectedModules.length === 0">
                    <p>Es sind keine Module zum Studiengang hinzugefügt.</p>
                </div>

            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.management-edit-course-container {
    @include basicContainer();
}

.select-header-container {
    width: 100%;

    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    align-items: center;
    gap: spacing(m) spacing(xl);
}

.dropdown {
    width: min-content;
}

.saving-buttons-container {
    display: flex;
    flex-wrap: wrap;
    gap: spacing(m);
}


.screen-split {
    @include screenSplit();
}

.selectable-modules-container {
    @include verticalList(small);
    width: 100%;
}


.single-selectable-module-item {
    @include smallHighlightBox();
    @include verticalListItem($gray);
    width: 100%;
    transition: 0.1s ease-in-out;

    display: flex;
    justify-content: space-between;
    gap: spacing(m);


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

.break-container {
    width: 100%;
    margin: spacing(m) 0;
    padding: 0 spacing(m);
    position: relative;
}

.break-line {
    width: 100%;
    height: 2px;
    background-color: $dark-gray;
}

.break-text {
    width: 3rem;
    text-align: center;
    background-color: $white;
    position: absolute;
    left: 50%;
    top: -0.65rem;
    transform: translateX(-50%);
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
}

.input-search-field-container {
   @include searchFieldContainer();
}

</style>
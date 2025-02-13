<script setup>
import { ref, computed, onBeforeMount } from "vue";
import ButtonLink from "@/components/button/ButtonLink.vue"
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";
import CustomDropdown from "@/components/util/CustomDropdown.vue";
import LoadingContainer from "@/components/util/LoadingContainer.vue";
import {
  getCoursesLeipzigName,
  getModulesNameCode,
  getModulesNameCodeByCourse,
  putCourseLeipzigEdit
} from "@/requests/module-course-requests";

/*
allows to add / remove modules from courses,
course to edit can be selected, afterward editor for modules will show
 */

const courses = ref()
const selectedCourse = ref()

const allModules = ref([])

// allModules of selected course
const addedModules = ref([])
const selectedModules = ref()

const selectableModules = computed(() => {
  if (selectedModules) {
    const selectedModuleNames = [...selectedModules.value, ...addedModules.value].map(singleModule => singleModule.name);

    const selectableModules = allModules.value.filter(singleModule => !selectedModuleNames.includes(singleModule.name));

    return selectableModules;
  } else {
    return undefined
  }
});

// search in selectable modules
const searchStringSelected = ref('');
const searchStringSelectable = ref('');

const addedModulesDisplay = computed(() => {
  if (searchStringSelected.value === '') return addedModules.value;

  return addedModules.value.filter(singleModule => {
    return singleModule.name.toLocaleLowerCase().includes(searchStringSelected.value.toLocaleLowerCase());
  })
})

const selectedModulesDisplay = computed(() => {
  if (searchStringSelected.value === '') return selectedModules.value;

  return selectedModules.value.filter(singleModule => {
    return singleModule.name.toLocaleLowerCase().includes(searchStringSelected.value.toLocaleLowerCase());
  })
})

const selectableModulesDisplay = computed(() => {
  if (searchStringSelectable.value === '') return selectableModules.value;

  return selectableModules.value.filter(singleModule => {
    return singleModule.name.toLocaleLowerCase().includes(searchStringSelectable.value.toLocaleLowerCase());
  })
})

onBeforeMount(() => {
  getCoursesLeipzigName()
    .then(data => courses.value = data)
  getModulesNameCode()
    .then(data => allModules.value = data)
})

const discardEditCourseChanges = () => {
  selectedCourse.value = null;
  selectedModules.value = [];
  addedModules.value = [];
  searchStringSelected.value = '';
  searchStringSelectable.value = '';
}

const setSelectedModules = () => {
  selectedModules.value = undefined
  getModulesNameCodeByCourse(selectedCourse.value)
    .then(data => selectedModules.value = data)
}

const addModuleToCourse = (clickedModule) => {
  addedModules.value.unshift(clickedModule);
  searchStringSelected.value = '';
  searchStringSelectable.value = '';
}

const removeModuleFromCourse = (clickedModule) => {
  selectedModules.value = selectedModules.value.filter(singleModule => singleModule.name !== clickedModule.name);
  addedModules.value = addedModules.value.filter(singleModule => singleModule.name !== clickedModule.name);
  searchStringSelected.value = '';
  searchStringSelectable.value = '';
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
      <CustomDropdown
          v-if="!selectedCourse"
          placeholder="Studiengang wählen"
          :options="courses"
          v-model="selectedCourse"
          @change="setSelectedModules"
          class="dropdown"
      />
      <div v-else class="saving-buttons-container">
        <ButtonLink @click="discardEditCourseChanges">Änderung verwerfen</ButtonLink>
        <ButtonLink @click="saveCourseLeipzig" :redButton="true">Speichern</ButtonLink>
      </div>
    </div>

    <div v-if="selectedCourse" class="edit-container">

      <div v-if="courses && allModules.length > 0 && selectedModules && selectableModules" class="screen-split">
        <div class="modules-container">
          <h3 class="h4">Wählbare Module</h3>
          <div class="input-search-field-container">
            <InputText v-model="searchStringSelectable" placeholder="Wählbare Module suchen" class="white" />
            <img src="../../assets/icons/SearchIcon.svg" class="search-icon">
          </div>
          <TransitionGroup name="list-left" tag="div" class="modules-container">

            <div v-for="singleModule in selectableModulesDisplay" :key="singleModule.name"
              @click="addModuleToCourse(singleModule)" class="selectable module-item icon-hover-right">
              <div class="module-text-container">
                <p>{{ singleModule.name }}</p>
                <small>{{ singleModule.code }}</small>
              </div>
              <ArrowIcon color="red" direction="right" />
            </div>

          </TransitionGroup>

          <Transition name="empty-text">
            <div v-if="selectableModules.length === 0">
              <p>Alle Module sind dem Studiengang hinzugefügt.</p>
            </div>
          </Transition>

        </div>

        <div class="modules-container">
          <h3 class="h4">Module in {{ selectedCourse }} ({{ selectedModules.length + addedModules.length }})</h3>
          <div class="input-search-field-container">
            <InputText v-model="searchStringSelected" placeholder="Ausgewählte Module suchen" class="white" />
            <img src="../../assets/icons/SearchIcon.svg" class="search-icon">
          </div>
          <TransitionGroup name="list-right" tag="div" class="modules-container">
            <div v-for="singleModule in addedModulesDisplay" :key="singleModule.name"
              @click="removeModuleFromCourse(singleModule)" class="selected module-item icon-hover-left">
              <ArrowIcon color="red" direction="left" />
              <div class="module-text-container">
                <p>{{ singleModule.name }}</p>
                <small>{{ singleModule.code }}</small>
              </div>
            </div>
          </TransitionGroup>

          <Transition name="separator">
            <div v-if="addedModules.length" class="break-container">
              <div class="break-line"></div>
              <h4 class="break-text">NEU</h4>
            </div>
          </Transition>

          <TransitionGroup name="list-right" tag="div" class="modules-container">
            <div v-for="singleModule in selectedModulesDisplay" :key="singleModule.name"
              @click="removeModuleFromCourse(singleModule)" class="module-item selected icon-hover-left">
              <ArrowIcon color="red" direction="left" />
              <div class="module-text-container">
                <p>{{ singleModule.name }}</p>
                <small>{{ singleModule.code }}</small>
              </div>
            </div>
          </TransitionGroup>

          <Transition name="empty-text">
            <div v-if="addedModules.length === 0 && selectedModules.length === 0">
              <p>Es sind keine Module zum Studiengang hinzugefügt.</p>
            </div>
          </Transition>

        </div>
      </div>
      <div v-else>
        <LoadingContainer />
      </div>

    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.list-left-enter-active,
.list-left-leave-active,
.list-right-enter-active,
.list-right-leave-active {
  transition: all 0.25s ease !important;
  pointer-events: none;
}

.list-left-enter-from,
.list-left-leave-to {
  opacity: 0 !important;
  transform: translateX(7px) !important;
}

.list-right-enter-from,
.list-right-leave-to {
  opacity: 0 !important;
  transform: translateX(-7px) !important;
}

.separator-leave-active {
  transition-delay: 0.25s;
}

.empty-text-enter-from {
  opacity: 0;
}

.empty-text-enter-to {
  opacity: 1;
}

.empty-text-enter-active {
  transition-delay: 0.25s
}

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

.edit-container {
  width: 100%;
}

.screen-split {
  @include screenSplit();
}

.modules-container {
  @include verticalList(s);
  width: 100%;
}

.module-item {
  @include smallHighlightBox();
  @include verticalListItem($gray);

  width: 100%;
  transition: 0.1s ease-in-out;

  display: flex;
  justify-content: space-between;
  gap: spacing(m);

  & .module-text-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  &:hover, &:focus {
    background-color: $gray-hover;
  }

  &.selected {
    border-left: none;
    border-right: 3px solid $red;

    & .module-text-container {
      align-items: flex-end;
      & p, small {
        text-align: right;
      }
    }
  }
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



.input-search-field-container {
  @include searchFieldContainer();
}
</style>
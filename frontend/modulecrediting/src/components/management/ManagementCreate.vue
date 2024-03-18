<script setup>
import { ref } from 'vue';
import ButtonLink from "@/components/button/ButtonLink.vue";
import { postCourseLeipzig, postModuleLeipzig } from "@/requests/module-course-requests";
import {consoleDebug} from "@/requests/consoleDebug";

/*
creating a course / module (depending on prop type)
 */

const props = defineProps({
  /* 'course' or 'module' */
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['course', 'module'].includes(value)
    }
  }
});

const coursename = ref('');
const modulename = ref('');
const modulecode = ref('');

const courseNameEmpty = ref(false);
const courseExists = ref(false);
const moduleNameEmpty = ref(false);
const moduleExists = ref(false);

const createCourseLeipzig = () => {
  courseNameEmpty.value = false
  courseExists.value = false
  if (!coursename.value) {
    courseNameEmpty.value = true
    return
  }
  postCourseLeipzig(coursename.value)
      .then(_ => location.reload())
      .catch(error => {
        if (error.response.status === 409) {
          courseExists.value = true
          consoleDebug('color:red', 'handled 409 in AccountAdminCreate')
        }
      })
}

const createModuleLeipzig = () => {
  moduleNameEmpty.value = false
  moduleExists.value = false
  if (!modulename.value) {
    moduleNameEmpty.value = true
    return
  }
  postModuleLeipzig(modulename.value, modulecode.value)
      .then(_ => location.reload())
      .catch(error => {
        if (error.response.status === 409) {
          moduleExists.value = true
          consoleDebug('color:red', 'handled 409 in AccountAdminCreate')
        }
      })
}
</script>

<template>
  <div v-if="type === 'course'" class="management-create-container">
    <h2>Studiengang erstellen</h2>
    <div class="input-container">
      <InputText type="text" placeholder="Name des Studiengangs" v-model="coursename" class="white" :class="{ 'invalid': courseNameEmpty || courseExists }" />
      <small v-if="courseNameEmpty" class="invalid-text">Name des Studiengangs darf nicht leer sein</small>
      <small v-if="courseExists" class="invalid-text">Studiengang existiert bereits</small>
    </div>
    <ButtonLink @click="createCourseLeipzig">Studiengang hinzufügen</ButtonLink>
  </div>


  <div v-else-if="type === 'module'" class="management-create-container">
    <h2>Modul erstellen</h2>
    <div class="input-container">
      <div class="module-input-container">
        <InputText type="text" placeholder="Modulname" v-model="modulename" class="white" :class="{ 'invalid': moduleNameEmpty || moduleExists }" />
        <InputText type="text"  placeholder="Modulcode" v-model="modulecode" class="white" :class="{ 'invalid': moduleExists }" />
      </div>
      <small v-if="moduleNameEmpty" class="invalid-text">Modulname darf nicht leer sein</small>
      <small v-if="moduleExists" class="invalid-text">Modulname oder Modulcode existiert bereits</small>
    </div>
    <ButtonLink @click="createModuleLeipzig">Modul hinzufügen</ButtonLink>
  </div>
</template>


<style lang="scss" scoped>
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.management-create-container {
  @include basicContainer();
  @include verticalListItem($white);
  @include verticalList(l);
  width: 100%;
}

.module-input-container {
  @include screenSplit();
  width: 100%;
}

</style>
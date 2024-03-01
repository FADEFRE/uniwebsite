<script setup>
import { ref } from 'vue';
import ButtonLink from "@/components/button/ButtonLink.vue";
import { postCourseLeipzig, postModuleLeipzig } from "@/requests/module-course-requests";

const props = defineProps({
    type: {
        required: true,
        type: String
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
        } else {
          location.reload()
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
        } else {
          location.reload()
        }
      })
}
</script>

<template>
  <div v-if="type === 'course'" class="management-create-container">
    <h2>{{ $t('ManagementCreate.CreateCourse') }}</h2>
    <div class="input-container">
      <InputText type="text" :placeholder="$t('ManagementCreate.NameCourse')" v-model="coursename"
                 class="white" :class="{ 'invalid': courseNameEmpty || courseExists }" />
      <small v-if="courseNameEmpty" class="invalid-text">{{ $t('ManagementCreate.NameCourseEmpty') }}</small>
      <small v-if="courseExists" class="invalid-text">{{ $t('ManagementCreate.CourseExists') }}</small>
    </div>
    <ButtonLink @click="createCourseLeipzig">{{ $t('ManagementeCreat.AddCourse') }}</ButtonLink>
  </div>


  <div v-else-if="type === 'module'" class="management-create-container">
    <h2>{{ $t('ManagementCreate.CreateModule') }}</h2>
    <div class="input-container">
      <div class="module-input-container">
        <InputText type="text" :placeholder="$t('ManagementCreate.NameModule')" v-model="modulename"
                   class="white" :class="{ 'invalid': moduleNameEmpty || moduleExists }" />
        <InputText type="text"  :placeholder="$t('ManagementCreate.ModuleCode')" v-model="modulecode"
                   class="white" :class="{ 'invalid': moduleExists }" />
      </div>
      <small v-if="moduleNameEmpty" class="invalid-text">{{ $t('ManagementCreate.ModuleNameEmpty') }}</small>
      <small v-if="moduleExists" class="invalid-text">{{ $t('ManagementCreate.ModuleNameExists') }}</small>
    </div>
    <ButtonLink @click="createModuleLeipzig">{{ $t('ManagementCreate.AddModule') }}</ButtonLink>
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
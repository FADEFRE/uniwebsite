<script setup>
import router from "@/router";
import { ref, onBeforeMount } from "vue";
import { getFormattedDate } from "@/utils/date-utils";
import ApplicationPanel from "@/components/panel/ApplicationPanel.vue";
import ButtonAdd from "@/components/button/ButtonAdd.vue";
import ButtonLink from "@/components/button/ButtonLink.vue";
import CustomDropdown from "@/components/util/CustomDropdown.vue";
import ApplicationOverview from "@/components/abstract/ApplicationOverview.vue";
import SideInfoApplicationProcess from "@/components/side-info/SideInfoApplicationProcess.vue";
import SideInfoStudyOffice from "@/components/side-info/SideInfoStudyOffice.vue";
import ApplicationInfoBox from "@/components/info-box/ApplicationInfoBox.vue";
import { getCoursesLeipzigName, getModulesByCourse } from "@/requests/module-course-requests";
import { postApplication } from "@/requests/application-requests";

const creationDate = new Date()

const courses = ref()
const selectedCourse = ref()

onBeforeMount(() => {
  getCoursesLeipzigName()
      .then(data => courses.value = data)
})

const selectableModules = ref([])
const setSelectableModules = () => {
  getModulesByCourse(selectedCourse.value)
      .then(data => selectableModules.value = data)
}

const moduleConnections = ref([1])
const moduleConnectionsRef = ref()

const addModuleConnection = () => {
  const nextIndex = Math.max(...moduleConnections.value) + 1
  moduleConnections.value.push(nextIndex)
}

const deleteModuleConnection = (key) => {
  moduleConnections.value = moduleConnections.value.filter(el => el !== key)
}

const courseValid = ref(true)

const checkValidity = () => {
  courseValid.value = Boolean(selectedCourse.value)
  const connectionValidity = moduleConnectionsRef.value.map(c => c.checkValidity()).every(Boolean)
  return courseValid.value && connectionValidity
}

const triggerPostApplication = () => {
  if (checkValidity()) {
    postApplication(selectedCourse.value, moduleConnectionsRef.value)
        .then(id => {
          router.push({ name: 'confirmation', params: { id: id } })
        })
  }
}
</script>

<template>
  <div class="main">
    <h1 class="screen-reader-only">{{ $t('SubmitApplicationView.SRHeading') }}</h1>

    <div class="content-container split">

      <ApplicationInfoBox />

      <ApplicationOverview :creation-date="getFormattedDate(creationDate)" :last-edited-date="undefined"
                           :decision-date="undefined" status="NEU">
        <CustomDropdown
            :placeholder="$t('SubmitApplicationView.ChooseCourse')"
            :options="courses"
            v-model="selectedCourse"
            @change="setSelectableModules"
            :class="{ 'invalid': !courseValid }"
        />
        <small v-if="!courseValid" class="invalid-text">{{ $t('SubmitApplicationView.CourseEmpty') }}</small>
      </ApplicationOverview>

      <ApplicationPanel v-for="item in moduleConnections" :key="item" :selectable-modules="selectableModules"
                        :allow-delete="moduleConnections.length > 1" ref="moduleConnectionsRef"
                        @delete-self="deleteModuleConnection(item)" />

      <div class="application-buttons-container">
        <ButtonAdd @click="addModuleConnection">{{ $t('SubmitApplicationView.AddModule') }}</ButtonAdd>
        <ButtonLink @click="triggerPostApplication" :redButton="true">{{ $t('SubmitApplicationView.Submit') }}</ButtonLink>
      </div>
    </div>

    <aside class="side-infos-list">
      <SideInfoApplicationProcess />
      <SideInfoStudyOffice />
    </aside>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>


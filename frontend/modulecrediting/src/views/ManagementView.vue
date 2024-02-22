<script setup>
import ManagementCreateContainer from '../components/ManagementCreate.vue';
import ManagementEditCourse from '../components/ManagementEditCourse.vue';
import ManagementViewCourse from '../components/ManagementViewCourse.vue';
import ManagementViewModule from '../components/ManagementViewModule.vue';
import ManagementViewAdminChild from "./ManagementViewAdminChild.vue";
</script>


<template>
  <div class="main">
    <div class="create-container-list">

      <ManagementCreateContainer type="course" />
      <ManagementCreateContainer type="module" />
      <ManagementEditCourse />
      <ManagementViewCourse />
      <ManagementViewModule />

      <!-- todo add separator -->

      <router-view />

    </div>
  </div>
</template>


<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.create-container-list {
  @include verticalList(l);
  width: 100%;
}
</style>
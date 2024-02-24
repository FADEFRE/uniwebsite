<script setup>
import ManagementCreateContainer from '@/components/ManagementCreate.vue';
import ManagementEditCourse from '@/components/ManagementEditCourse.vue';
import ManagementViewCourse from '@/components/ManagementViewCourse.vue';
import ManagementViewModule from '@/components/ManagementViewModule.vue';
import MoveTop from '@/assets/icons/MoveTop.vue';

const scrollTop = () => {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}
</script>


<template>
  <div class="main">
    <h1 class="screen-reader-only">Verwaltung von Studiengängen und Modulen</h1>
    <div class="create-container-list">
      <router-view />

      <ManagementCreateContainer type="course" />
      <ManagementCreateContainer type="module" />
      <ManagementEditCourse />
      <div class="break-container">
              <div class="break-line"></div>
              <h4 class="break-text">ÜBERSICHT</h4>
        </div>
      <ManagementViewCourse />
      <ManagementViewModule />
    </div>

    <MoveTop class="move-top-button" @click="scrollTop"/>
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
  width: 7rem;
  text-align: center;
  background-color: $gray;
  position: absolute;
  left: 50%;
  top: -0.65rem;
  transform: translateX(-50%);
}

.move-top-button {
  position: fixed;
  bottom: spacing(m);
  right: spacing(s);
}
</style>
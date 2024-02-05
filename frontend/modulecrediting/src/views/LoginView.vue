<script setup>
import LoginContainer from '../components/LoginContainer.vue';
</script>


<template>
  <div class="main">
    <LoginContainer />
  </div>
</template>


<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;

.main {
  @include main();
  flex-direction: row !important;
}

</style>

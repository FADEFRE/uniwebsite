<script setup>
import LoginContainer from '../components/LoginContainer.vue';
</script>


<template>
  <div class="main">
    <LoginContainer />
  </div>
</template>


<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
  @include main();
  flex-direction: row !important;
}

</style>

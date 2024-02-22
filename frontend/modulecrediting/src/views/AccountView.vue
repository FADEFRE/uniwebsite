<script setup>
import AccountSettings from "../components/AccountSettings.vue";
</script>

<template>
  <div class="main">
    <div class="content-container">
      <AccountSettings />
      <router-view />
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>
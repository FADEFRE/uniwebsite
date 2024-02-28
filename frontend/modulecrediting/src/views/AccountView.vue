<script setup>
import AccountSettings from "@/components/account/AccountSettings.vue";
</script>

<template>
  <div class="main">
    <h1 class="screen-reader-only">Accountverwaltung</h1>
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
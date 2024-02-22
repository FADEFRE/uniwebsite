<script setup>
import AccountAdminCreate from "../components/AccountAdminCreate.vue";
import AccountAdminList from "../components/AccountAdminList.vue";
</script>

<template>
  <AccountAdminList />
  <AccountAdminCreate />
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>
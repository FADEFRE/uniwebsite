<script setup>
import AccountAdminCreate from "@/components/account/AccountAdminCreate.vue";
import AccountAdminList from "@/components/account/AccountAdminList.vue";
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
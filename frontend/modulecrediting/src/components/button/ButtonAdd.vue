<script setup>
/*
button with add icon

slots:
  default - button text
 */
</script>

<template>
    <Button class="button-container">
      <slot></slot>
      <img src="../../assets/icons/PlusIcon.svg" alt="">
    </Button>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>
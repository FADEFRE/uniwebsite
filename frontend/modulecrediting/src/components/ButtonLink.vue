<script setup>

const props = defineProps({
  fixed: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  },
  redButton: {
    type: Boolean,
    default: false
  }
})
</script>


<template>
  <div class="button-container">
    <Button :disabled="disabled" :class="{ ' fixed-button': fixed, 'disabled': disabled, 'red-button': redButton }">
      <slot />
      <img src="../assets/icons/ArrowWhite.svg" class="arrow-icon" alt="Arrow Icon">
    </Button>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>

<script setup>
import ArrowIcon from '@/assets/icons/ArrowIcon.vue';

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
    <Button
        role="link"
        :disabled="disabled"
        :class="{ 'fixed-button': fixed, 'disabled': disabled, 'red-button': redButton }"
        class="icon-hover-right"
    >
      <slot />
      <ArrowIcon direction="right" :hover="true" />
    </Button>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>

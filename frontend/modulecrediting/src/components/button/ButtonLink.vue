<script setup>
import ArrowIcon from '@/assets/icons/ArrowIcon.vue';

/*
button with link icon

slots:
  default - button text
 */

const props = defineProps({
  /* should be set to true, if button should be fixed */
  fixed: {
    type: Boolean,
    default: false
  },
  /* should be set to true, if button should be disabled */
  disabled: {
    type: Boolean,
    default: false
  },
  /* button will be displayed red if set to true, else button will be displayed gray */
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

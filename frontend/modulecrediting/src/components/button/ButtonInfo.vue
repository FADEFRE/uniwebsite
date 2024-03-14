<script setup>
import ArrowIcon from '@/assets/icons/ArrowIcon.vue';

/*
button to expand info

slots:
  default - button text
 */

const props = defineProps({
  collapsed: {
    type: Boolean,
    default: true
  }
})
</script>

<template>
    <Button class="info-box-button">
      <slot/>
      <ArrowIcon :direction="collapsed ? 'up' : 'down'" color="red" />
    </Button>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>
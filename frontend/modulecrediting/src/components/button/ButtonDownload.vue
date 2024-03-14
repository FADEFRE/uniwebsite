<script setup>
import FileIcon from '@/assets/icons/FileIcon.vue';

/*
button with download icon

slots:
  default - button text
 */

const props = defineProps({
  /* should be set to true, if button should be fixed */
  fixed: {
    type: Boolean,
    default: false
  }
})
</script>

<template>
  <div class="button-container">
    <Button :class="{ ' fixed-button': fixed }">
      <slot />
      <FileIcon type="download"/>
    </Button>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.button-container {
  display: flex;
  gap: spacing(s);
  width: max-content;
}
</style>

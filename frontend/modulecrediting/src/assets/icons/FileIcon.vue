<script setup>
/*
upload / download icon
 */

const props = defineProps({
  /* 'upload' or 'download' */
  type: {
    required: true,
    type: String
  }
})
</script>

<template>
  <svg v-if="type==='upload'" class="file-icon" viewBox="0 0 22 20" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
    <path d="M6 7L11 2L16 7" stroke="#D83D3B" stroke-width="2.5"/>
    <path d="M11 13V3" stroke="#D83D3B" stroke-width="2.5"/>
    <path d="M2 11V18H20V11" stroke="#D83D3B" stroke-width="2.5"/>
  </svg>

  <svg v-else class="file-icon" viewBox="0 0 22 19" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
    <path d="M16 6L11 11L6 6" stroke="#D83D3B" stroke-width="2.5"/>
    <path d="M11 10V0" stroke="#D83D3B" stroke-width="2.5"/>
    <path d="M2 10V17H20V10" stroke="#D83D3B" stroke-width="2.5"/>
  </svg>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.file-icon {
  width: rem(22px);
  height: rem(20px);
}
</style>
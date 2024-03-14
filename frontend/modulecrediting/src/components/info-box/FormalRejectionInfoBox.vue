<script setup>
/*
information about a formal rejection
 */
</script>

<template>
  <div class="application-info-container">
    <h2>{{ $t('FormalRejectionInfoBox.Heading') }}</h2>
    <p class="text-justify">
      {{ $t('FormalRejectionInfoBox.Text') }}
    </p>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>
<script setup>

</script>

<template>
  <div class="sideinfo-container">
    <h3 class="heading">{{ $t('homepage.sideInfo.applicationProcess') }}</h3>
    <ul class="points">
      <li>
        <p>{{ $t('homepage.sideInfo.submitApplication') }}</p>
      </li>
      <li>
        <p>{{ $t('homepage.sideInfo.viewStatus') }}</p>
      </li>
      <li>
        <p>{{ $t('homepage.sideInfo.goToStudy') }}</p>
      </li>
    </ul>
  </div>
</template>

<style lang="scss" scoped>
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.sideinfo-container {
  @include basicContainer();
  width: 100%;
}

.heading {
  width: max-content;
}
</style>

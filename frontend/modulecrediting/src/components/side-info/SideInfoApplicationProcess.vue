<script setup>
/*
information about the steps of an application process,
use aside main content
 */
</script>

<template>
  <div class="side-info-container">
    <h2 class="heading h3">{{ $t('SideInfoApplicationProcess.ApplicationProcess') }}</h2>
    <ul class="points">
      <li>
        <p>{{ $t('SideInfoApplicationProcess.SubmitApplication') }}</p>
      </li>
      <li>
        <p>{{ $t('SideInfoApplicationProcess.ViewStatus') }}</p>
      </li>
      <li>
        <p>{{ $t('SideInfoApplicationProcess.GoToStudy') }}</p>
      </li>
    </ul>
  </div>
</template>

<style lang="scss" scoped>
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.side-info-container {
  @include basicContainer();
  width: 100%;
}

.heading {
  width: max-content;
}
</style>

<script setup>

</script>

<template>
  <div class="sideinfo-container">
    <h3 class="heading">{{ $t('homepage.sideInfo.applicationProcess') }}</h3>
    <ul class="list-container">
      <li class="list-item">
        <p>{{ $t('homepage.sideInfo.submitApplication') }}</p>
      </li>
      <li class="list-item">
        <p>{{ $t('homepage.sideInfo.viewStatus') }}</p>
      </li>
      <li class="list-item">
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

:slotted(.main-info-container) {
  @include verticalList(l);
}

:slotted(.info-group-container){
  display: flex;
  flex-direction: column;
  gap: spacing(xs);
}

.heading {
  width: max-content;
  text-transform: uppercase;
}
</style>

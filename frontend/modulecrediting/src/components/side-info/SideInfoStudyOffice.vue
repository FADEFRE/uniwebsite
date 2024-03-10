<script setup>

import ButtonLink from "@/components/button/ButtonLink.vue";
</script>

<template>
  <div class="side-info-container">
    <h2 class="heading h3">{{ $t('SideInfoStudyOffice.Studyoffice') }}</h2>
    <p>{{ $t('SideInfoStudyOffice.fmi') }}</p>
    <div class="main-info-container">
      <div class="info-group-container">
        <h3 class="h4">{{ $t('SideInfoStudyOffice.address') }}</h3>
        <ul class="points">
          <li>Neues Augusteum</li>
          <li>Augustusplatz 10</li>
          <li>04109 Leipzig</li>
        </ul>
      </div>
      <div class="info-group-container">
        <h3 class="h4">{{ $t('SideInfoStudyOffice.contact') }}</h3>
        <ul class="points">
          <li>Telefon: +49 341 97-32165</li>
          <li>Telefax: +49 341 97-32193</li>
          <li>E-Mail: studienbuero@math.uni-leipzig.de</li>
        </ul>
      </div>
      <div class="info-group-container">
        <h3 class="h4">{{ $t('SideInfoStudyOffice.officeHours') }}</h3>
        <p>{{ $t('SideInfoStudyOffice.ActualOfficeHours') }}</p>
      </div>
      <a href="https://www.mathcs.uni-leipzig.de/studium/studienbuero" target="_blank" >
        <ButtonLink>
          {{ $t('SideInfoStudyOffice.ToStudyOffice') }}
        </ButtonLink>
      </a>
    </div>
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

.main-info-container {
  @include verticalList(m);
}

.heading {
  width: max-content;
}
</style>

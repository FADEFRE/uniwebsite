<script setup>
import ButtonLink from "@/components/button/ButtonLink.vue";
</script>

<template>
  <div class="side-info-container">
    <h2 class="heading h3">{{ $t('SideInfoStudyOffice.StudyOffice') }}</h2>
    <p>{{ $t('SideInfoStudyOffice.Faculty') }}</p>
    <div class="main-info-container">
      <div class="info-group-container">
        <h3 class="h4">{{ $t('SideInfoStudyOffice.Address') }}</h3>
        <ul class="points">
          <li>Neues Augusteum</li>
          <li>Augustusplatz 10</li>
          <li>04109 Leipzig</li>
        </ul>
      </div>
      <div class="info-group-container">
        <h3 class="h4">{{ $t('SideInfoStudyOffice.Contact') }}</h3>
        <ul class="points">
          <li>{{ $t('SideInfoStudyOffice.Phone') }}: +49 341 97-32165</li>
          <li>{{ $t('SideInfoStudyOffice.Fax') }}: +49 341 97-32193</li>
          <li>{{ $t('SideInfoStudyOffice.Email') }}: studienbuero@math.uni-leipzig.de</li>
        </ul>
      </div>
      <div class="info-group-container">
        <h3 class="h4">{{ $t('SideInfoStudyOffice.OfficeHours') }}</h3>
        <p>{{ $t('SideInfoStudyOffice.OfficeHourTimes') }}</p>
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

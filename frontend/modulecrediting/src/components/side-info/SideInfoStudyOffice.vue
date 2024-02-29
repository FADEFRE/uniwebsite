<script setup>

import ButtonLink from "@/components/button/ButtonLink.vue";
</script>

<template>
  <div class="sideinfo-container">
    <h2 class="heading h3">Studienbüro</h2>
    <p>Fakultät für Mathematik und Informatik</p>
    <div class="main-info-container">
      <div class="info-group-container">
        <h3 class="h4">Anschrift</h3>
        <ul class="points">
          <li>Neues Augusteum</li>
          <li>Augustusplatz 10</li>
          <li>04109 Leipzig</li>
        </ul>
      </div>
      <div class="info-group-container">
        <h3 class="h4">Kontakt</h3>
        <ul class="points">
          <li>Telefon: +49 341 97-32165</li>
          <li>Telefax: +49 341 97-32193</li>
          <li>E-Mail: studienbuero@math.uni-leipzig.de</li>
        </ul>
      </div>
      <div class="info-group-container">
        <h3 class="h4">Sprechzeiten</h3>
        <p>Dienstag und Donnerstag: 9:00 - 11:30 Uhr und 12:30 - 16:00 Uhr</p>
      </div>
      <a href="https://www.mathcs.uni-leipzig.de/studium/studienbuero" target="_blank" >
        <ButtonLink>
          Zum Studienbüro
        </ButtonLink>
      </a>
    </div>
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

.main-info-container {
  @include verticalList(m);
}

.heading {
  width: max-content;
}
</style>

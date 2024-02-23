<script setup>

import ButtonLink from "@/components/ButtonLink.vue";
</script>

<template>
  <div class="sideinfo-container">
    <h3 class="heading">Studienbüro</h3>
    <p>Fakultät für Mathematik und Informatik</p>
    <div class="main-info-container">
      <div class="info-group-container">
        <h4>Anschrift</h4>
        <ul>
          <li>Neues Augusteum</li>
          <li>Augustusplatz 10</li>
          <li>04109 Leipzig</li>
        </ul>
      </div>
      <div class="info-group-container">
        <h4>Kontakt</h4>
        <ul>
          <li>Telefon: +49 341 97-32165</li>
          <li>Telefax: +49 341 97-32193</li>
          <li>E-Mail: studienbuero@math.uni-leipzig.de</li>
        </ul>
      </div>
      <div class="info-group-container">
        <h4>Sprechzeiten</h4>
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

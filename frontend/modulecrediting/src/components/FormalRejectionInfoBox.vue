<script setup>

</script>

<template>
  <div class="formal-rejection-info-container">
    <h2>Formfehler</h2>
    <p>
      Aufgrund von Formfehlern wurde ihr Antrag abgelehnt.
      Bitte korrigieren sie rot markierte Modulverknüpfungen.
      Anschließend drücken sie auf "neu einreichen", um den Antrag neu einzureichen.
    </p>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.formal-rejection-info-container {
  @include verticalListItem($white);
  @include basicContainer();

}
</style>
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
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.formal-rejection-info-container {
  @include verticalListItem($white);
  @include basicContainer();

}
</style>
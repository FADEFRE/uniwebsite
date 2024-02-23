<script setup>

</script>

<template>
  <div class="formal-rejection-info-container">
    <h2>Formfehler</h2>
    <p>
      Ihr Antrag wurde aufgrund von Formfehlern zurückgewiesen.
      Es sind die Modulzuweisungen rot markiert, die Formfehler enthalten.
      In der Modulzuweisung finden sie unten eine Erklärung des konkreten Fehlers.
      Bitte korrigieren sie alle angegebenen Fehler.
      Anschließend können sie ihren Antrag neu einreichen.
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

  margin-bottom: spacing(m);
}
</style>
<script setup>
import { ref } from "vue";

const moduleName = ref()
const university = ref()
const creditPoints = ref()

defineExpose({moduleName})
</script>

<template>
  <div class="main-container">
    <div class="module-container external">

      <h3>Anzurechnendes Modul:</h3>
      <div class="text-input-container">
        <p>Modulname:</p>
        <InputText type="text" v-model="moduleName"/>
      </div>
      <div class="text-input-container">
        <p>Universität:</p>
        <InputText type="text" v-model="university"/>
      </div>
      <div class="text-input-container">
        <p>Leistungspunkte:</p>
        <InputText type="text" v-model="creditPoints"/>
      </div>

    </div>
    <Divider layout="vertical" />
    <div class="module-container internal">

      <h3>Modul der Uni Leipzig:</h3>

    </div>

  </div>
</template>

<style scoped>
.main-container > * {
  display: inline-block;
}

.text-input-container > * {
  display: inline-block;
  padding: 10px;
}

.module-container {
  width: 48%;
}
</style>

<!--
base modul panel
displays:
- module text input
- university text input
- credit points text input
- point system text input
- file SLOT CONTENT
- internalModules SLOT CONTENT
- comment SLOT CONTENT
functionality:
- module, university, credit points, point system input
- providing layout with exchangeable slot content
-->

<script setup>
import { ref } from "vue";

// external module
const moduleName = ref()
const university = ref()
const creditPoints = ref()
const pointSystem = ref()

defineExpose({
  moduleName, university, creditPoints, pointSystem
})
</script>

<template>
  <div>

    <!-- External Module -->
    <div class="module-container" id="external">

      <h3 class="module-heading">Anzurechnendes Modul:</h3>
      <!-- Module Name -->
      <div class="text-input-container">
        <p>Modulname:</p>
        <InputText type="text" v-model="moduleName"/>
      </div>
      <!-- University -->
      <div class="text-input-container">
        <p>Universität:</p>
        <InputText type="text" v-model="university"/>
      </div>
      <!-- Credit Points -->
      <div class="text-input-container">
        <p>Leistungspunkte:</p>
        <InputText type="text" v-model="creditPoints"/>
      </div>
      <!-- Point System -->
      <div class="text-input-container">
        <p>Punktesystem:</p>
        <InputText type="text" v-model="pointSystem"/>
      </div>
      <!-- File -->
      <slot name="file">
        <!-- file slot should receive either instance of PanelApplicationFile or todo -->
        File Slot
      </slot>
    </div>

    <div class="module-container" id="internal">
      <!-- Internal Modules -->
      <h3 class="module-heading">Module der Uni Leipzig:</h3>
      <slot name="internalModules">
        <!-- internalModules slot should receive either instance of PanelInternalModulesSelection or todo -->
        Internal Modules Slot
      </slot>
      <!-- Comment -->
      <h3 class="module-heading">Anmerkungen:</h3>
      <slot name="comment">
        <!-- comment slot should receive either instance of PanelApplicationComment or todo -->
        Comment Slot
      </slot>
    </div>

  </div>
</template>

<style scoped>
.module-container {
  display: inline-grid;
  width: 50%;
}

.module-heading {
  text-decoration-line: underline;
}

.text-input-container > * {
  display: inline-block;
  margin: 5px;
}

.text-input-container > p {
  width: 18%;
  text-align: center;
}

.internal-module-dropdown-item > * {
  margin: 5px;
}
</style>

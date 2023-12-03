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
- can be used with or without initial data as props
-->

<script setup>
import { ref } from "vue";

const props = defineProps(['moduleName', 'university', 'creditPoints', 'pointSystem'])

// external module
const moduleName = ref(props.moduleName)
const university = ref(props.university)
const creditPoints = ref(props.creditPoints)
const pointSystem = ref(props.pointSystem)

defineExpose({
  moduleName,
  university,
  creditPoints,
  pointSystem
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
        <InputText type="text" v-model="moduleName" class="text-input" />
      </div>
      <!-- University -->
      <div class="text-input-container">
        <p>Universität:</p>
        <InputText type="text" v-model="university" class="text-input" />
      </div>
      <!-- Credit Points -->
      <div class="text-input-container">
        <p>Leistungspunkte:</p>
        <InputText type="text" v-model="creditPoints" class="text-input" />
      </div>
      <!-- Point System -->
      <div class="text-input-container">
        <p>Punktesystem:</p>
        <InputText type="text" v-model="pointSystem" class="text-input" />
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

.text-input-container>* {
  display: inline-block;
  margin: 5px;
}

.text-input-container>p {
  width: 18%;
  text-align: center;
}

/* Anpassung der Schriftgröße */
.text-input {
  font-size: 16px;
  width: calc(100% - 60px);
  /* Dynamische Breite des Textfeldes */
}

/* Anpassung der Texteingabefelder an die Fenstergröße */
@media screen and (max-width: 768px) {
  .text-input {
    width: calc(100% - 40px);
    /* Ändern Sie die Breite für kleinere Bildschirme */
  }
}
</style>

<!--
status icons for administrative panel header
props:
  - decisionSuggestion (optional, may be 'accepted', 'asExamCertificate' or 'denied')
  - decisionFinal (optional, may be 'accepted', 'asExamCertificate' or 'denied')
displays:
  - suggestion icon, arrow, final decision icon
-->

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    decisionSuggestion: {
      type: String,
      validator(value) {
        return ['accepted', 'asExamCertificate', 'denied'].includes(value)
      }
    },
    decisionFinal: {
      type: String,
      validator(value) {
        return ['accepted', 'asExamCertificate', 'denied'].includes(value)
      }
    }
});
</script>

<template>
  <div class="panel-status-icons">

    <div>
      <img v-if="decisionSuggestion === 'accepted'" src="@/assets/icons/ModuleAccepted.svg">
      <img v-else-if="decisionSuggestion === 'asExamCertificate'" src="@/assets/icons/ModuleAsExamCertificate.svg">
      <img v-else-if="decisionSuggestion === 'denied'" src="@/assets/icons/ModuleDenied.svg">
      <img v-else src="@/assets/icons/moduleUnedited.svg">
    </div>

    <img src="../assets/icons/ArrowDark.svg" class="arrow-icon">

    <div>
      <img v-if="decisionFinal === 'accepted'" src="@/assets/icons/ModuleAccepted.svg">
      <img v-else-if="decisionFinal === 'asExamCertificate'" src="@/assets/icons/ModuleAsExamCertificate.svg">
      <img v-else-if="decisionFinal === 'denied'" src="@/assets/icons/ModuleDenied.svg">
      <img v-else src="@/assets/icons/moduleUnedited.svg">
    </div>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';
.panel-status-icons {
    display: flex;
    justify-content: center;
    gap: 0.9375rem;
}
.arrow-icon {
    @include rightArrow();
}
</style>

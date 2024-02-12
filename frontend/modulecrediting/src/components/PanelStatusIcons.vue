<!--
status icons for administrative panel header
props:
  - decisionSuggestion (optional, may be 'accepted', 'asExamCertificate' or 'denied')
  - decisionFinal (optional, may be 'accepted', 'asExamCertificate' or 'denied')
displays:
  - suggestion icon, arrow, final decision icon
-->

<script setup>
const props = defineProps({
    formalRejection: {
      type: Boolean
    },
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

    <div v-if="formalRejection" class="panel-status-icons">
      <img src="@/assets/icons/ModuleFormalRejection.svg">
    </div>

    <div v-else  class="panel-status-icons">
      <div>
        <img v-if="decisionSuggestion === 'accepted'" src="@/assets/icons/ModuleAccepted.svg">
        <img v-else-if="decisionSuggestion === 'asExamCertificate'" src="@/assets/icons/ModuleAsExamCertificate.svg">
        <img v-else-if="decisionSuggestion === 'denied'" src="@/assets/icons/ModuleDenied.svg">
        <img v-else src="@/assets/icons/ModuleUnedited.svg">
      </div>

      <img src="@/assets/icons/ArrowDark.svg" class="arrow-icon">

      <div>
        <img v-if="decisionFinal === 'accepted'" src="@/assets/icons/ModuleAccepted.svg">
        <img v-else-if="decisionFinal === 'asExamCertificate'" src="@/assets/icons/ModuleAsExamCertificate.svg">
        <img v-else-if="decisionFinal === 'denied'" src="@/assets/icons/ModuleDenied.svg">
        <img v-else src="@/assets/icons/ModuleUnedited.svg">
      </div>
    </div>

</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
.panel-status-icons {
    display: flex;
    justify-content: center;
    gap: spacing(m);
    margin-right: spacing(s);
}
.arrow-icon {
    @include rightArrow();
}
</style>

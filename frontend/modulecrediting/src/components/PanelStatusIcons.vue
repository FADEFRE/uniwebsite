<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    decisionSuggestion: String,
    decisionFinal: String
});
</script>

<template>
    <div class="panel-status-icons">
        <img v-if="decisionFinal === 'accepted'" src="@/assets/icons/ModuleAccepted.svg" alt="Accepted Icon">
        <img v-else-if="decisionFinal === 'asExamCertificate'" src="@/assets/icons/ModuleAsExamCertificate.svg"
            alt="Exam Certificate Icon">
        <img v-else-if="decisionFinal === 'denied'" src="@/assets/icons/ModuleDenied.svg" alt="Denied Icon">
        <img v-else src="@/assets/icons/moduleUnedited.svg" alt="Module Unedited Icon">
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

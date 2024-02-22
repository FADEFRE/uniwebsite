<!--
status icons for administrative panel header
props:
  - decisionSuggestion (optional, may be 'accepted', 'asExamCertificate' or 'denied')
  - decisionFinal (optional, may be 'accepted', 'asExamCertificate' or 'denied')
displays:
  - suggestion icon, arrow, final decision icon
-->

<script setup>
import ArrowIcon from '@/assets/icons/ArrowIcon.vue';
import ModuleStatusIcon from '../assets/icons/ModuleStatusIcon.vue';

const props = defineProps({
    formalRejection: {
      type: Boolean
    },
    decisionSuggestion: {
      type: String,
      validator(value) {
        return ['accepted', 'asExamCertificate', 'denied', 'unedited'].includes(value)
      }
    },
    decisionFinal: {
      type: String,
      validator(value) {
        return ['accepted', 'asExamCertificate', 'denied', 'unedited'].includes(value)
      }
    }
});
</script>

<template>

    <div v-if="formalRejection" class="panel-status-icons">
      <ModuleStatusIcon status-decision="formalRejection"/>
    </div>

    <div v-else  class="panel-status-icons">
      <ModuleStatusIcon :status-decision="decisionSuggestion"/>
      <ArrowIcon direction="right" color="dark-gray"></ArrowIcon>
      <ModuleStatusIcon :status-decision="decisionFinal"/>
    </div>

</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.panel-status-icons {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: spacing(m);
    margin-right: spacing(s);
}
</style>

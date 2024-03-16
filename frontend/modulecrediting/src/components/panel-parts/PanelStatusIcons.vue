<script setup>
import ArrowIcon from '@/assets/icons/ArrowIcon.vue';
import ModuleStatusIcon from '@/assets/icons/ModuleStatusIcon.vue';

/*
icons indicating internal decision status
either prop formalRejection should be set (to display a formal rejection)
or props decisionSuggestion and decisionFinal should be set (to display study-office and chairman decision)
 */

const props = defineProps({
  /* should be set true to indicate formal rejection */
  formalRejection: {
    type: Boolean
  },
  /* 'accepted', 'asExamCertificate', 'denied' or 'unedited' (omit if formalRejection) */
  decisionSuggestion: {
    type: String,
    validator(value) {
      return ['accepted', 'asExamCertificate', 'denied', 'unedited'].includes(value)
    }
  },
  /* 'accepted', 'asExamCertificate', 'denied' or 'unedited' (omit if formalRejection) */
  decisionFinal: {
    type: String,
    validator(value) {
      return ['accepted', 'asExamCertificate', 'denied', 'unedited'].includes(value)
    }
  }
})
</script>

<template>

  <div v-if="formalRejection" class="panel-status-icons">
    <ModuleStatusIcon status-decision="formalRejection"/>
  </div>

  <div v-else class="panel-status-icons">
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

  @include breakpoint(s) {
    gap: spacing(s);
  }
}
</style>

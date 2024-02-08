<script setup>
import { ref, watch } from "vue";
const props = defineProps({
  readonly: {
    required: true,
    type: Boolean
  },
  displayDecision: {
    type: String,
    validator(value) {
      return ['accepted', 'asExamCertificate', 'denied'].includes(value)
    }
  },
  comment: {
    type: String
  }
})

const emit = defineEmits(['change'])

const decisionOptions = [
  { value: 'accepted', label: 'Annehmen', class: 'highlight-accepted' },
  { value: 'asExamCertificate', label: 'Übungsschein', class: 'highlight-asExamCertificate' },
  { value: 'denied', label: 'Ablehnen', class: 'highlight-denied' }
];

const decision = ref(props.displayDecision);
const comment = ref(props.comment);

watch([comment, decision], () => emit('change'))

defineExpose({
  decision,
  comment
})
</script>

<!-- todo accessibility -->
<template>
  <div class="panel-decision-block">


    <div v-if="!readonly">
      <SelectButton :allow-empty="false" :options="decisionOptions" optionLabel="label" optionValue="value" v-model="decision"/>
    </div>

    <div v-else class="readonly-decision-container">

      <div class="icon-container" :class="{ 'highlight': displayDecision === 'accepted' }">
        <img v-if="displayDecision === 'accepted'" src="../assets/icons/ModuleAccepted.svg">
        <img v-else src="../assets/icons/ModuleAcceptedGray.svg">
      </div>

      <div class="icon-container" :class="{ 'highlight': displayDecision === 'asExamCertificate' }">
        <img v-if="displayDecision === 'asExamCertificate'" src="../assets/icons/ModuleAsExamCertificate.svg">
        <img v-else src="../assets/icons/ModuleAsExamCertificateGray.svg">
      </div>
      <div class="icon-container" :class="{ 'highlight': displayDecision === 'denied' }">
        <img v-if="displayDecision === 'denied'" src="../assets/icons/ModuleDenied.svg">
        <img v-else src="../assets/icons/ModuleDeniedGray.svg">
      </div>
    </div>

    <textarea :readonly="readonly" v-model="comment" @change="emit('change')"></textarea>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.panel-decision-block {
  display: flex;
  width: 100%;
  align-self: stretch;

  border: 2px solid $dark-gray;
}

.readonly-decision-container {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.icon-container {
  padding: 0.5rem 1.25rem;
  border-right: 2px solid $black;

  &.highlight {
    background-color: $gray;
  }
}

:deep(.p-button) {
  width: 100%;
}

:deep(.p-button:first-child.p-highlight) {
  background-color: $green;
  &:hover {
    background-color: $green-hover;
  }
}
:deep(.p-button:nth-child(2).p-highlight) {
  background-color: $orange;
  &:hover {
    background-color: $orange-hover;
  }
}
:deep(.p-button:last-child.p-highlight) {
  background-color: $red;
  &:hover {
    background-color: $red-hover;
  }
}
</style>
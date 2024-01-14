<script setup>
import { ref } from "vue";
const props = defineProps({
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['edit', 'readonly'].includes(value)
    }
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


const decisionOptions = [
  { value: 'accepted', label: 'Annehmen', class: 'highlight-accepted' },
  { value: 'asExamCertificate', label: 'Übungsschein', class: 'highlight-asExamCertificate' },
  { value: 'denied', label: 'Ablehnen', class: 'highlight-denied' }
];

const decision = ref(props.displayDecision);
const comment = ref(props.comment);

defineExpose({
  decision,
  comment
})
</script>

<!-- todo accessibility -->
<template>
  <div class="panel-decision-block">


    <div v-if="type === 'edit'">
      <SelectButton :allow-empty="false" v-model="decision" :options="decisionOptions" optionLabel="label"
        optionValue="value">
        <template #option="optionProps">
          <div :class="optionProps.option.class">{{ optionProps.option.label }}</div>
        </template>
      </SelectButton>
    </div>

    <div v-else-if="type === 'readonly'" class="readonly-decision-container">

      <div class="icon-container">
        <img v-if="displayDecision === 'accepted'" src="../assets/icons/ModuleAccepted.svg">
        <img v-else src="../assets/icons/ModuleAcceptedGray.svg">
      </div>

      <div class="icon-container">
        <img v-if="displayDecision === 'asExamCertificate'" src="../assets/icons/ModuleAsExamCertificate.svg">
        <img v-else src="../assets/icons/ModuleAsExamCertificateGray.svg">
      </div>
      <div class="icon-container">
        <img v-if="displayDecision === 'denied'" src="../assets/icons/ModuleDenied.svg">
        <img v-else src="../assets/icons/ModuleDeniedGray.svg">
      </div>
    </div>

    <textarea v-model="comment" :readonly="type === 'readonly'"></textarea>
  </div>
</template>

<style scoped lang="scss">
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';


.panel-decision-block {
  display: flex;
  width: 100%;
  align-self: stretch;
}

.readonly-decision-container {
  display: flex;
  flex-direction: column;
}
.icon-container {
  @include smallHighlightBox();
  border: 1px $dark-gray solid;
}

textarea {
  width: 100%;
  resize: none;
  padding: 0.625rem;
}

:deep(.p-button) {
  width: 100%;
}

.highlight-accepted {
  background-color: $green;
}

.highlight-asExamCertificate {
  background-color: $orange;
}

.highlight-denied {
  background-color: $red;
}
</style>
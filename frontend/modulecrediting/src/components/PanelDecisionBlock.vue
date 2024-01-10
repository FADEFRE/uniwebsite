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
      return ['accept', 'asExamCertificate', 'denied'].includes(value)
    }
  },
  comment: {
    type: String
  }
})

const decision = ref()
if (props.displayDecision) decision.value = props.displayDecision

const decisionOptions = [
  { value: 'accepted', label: 'Annehmen' },
  { value: 'asExamCertificate', label: 'Übungsschein'},
  { value: 'denied', label: 'Ablehnen'}
]
</script>

<!-- todo accessibility -->
<template>
  <div>

    <div>

      <div v-if="type === 'edit'">
        <SelectButton :allow-empty="false" v-model="decision" :options="decisionOptions" optionLabel="label" optionValue="value" />
      </div>

      <div v-else-if="type === 'readonly'">

        <img v-if="displayDecision === 'accept'" src="../assets/icons/ModuleAccepted.svg">
        <img v-else src="../assets/icons/ModuleAcceptedGray.svg">

        <img v-if="displayDecision === 'asExamCertificate'" src="../assets/icons/ModuleAsExamCertificate.svg">
        <img v-else src="../assets/icons/ModuleAsExamCertificateGray.svg">

        <img v-if="displayDecision === 'denied'" src="../assets/icons/ModuleDenied.svg">
        <img v-else src="../assets/icons/ModuleDeniedGray.svg">

      </div>
    </div>

    <div>
      <textarea :readonly="type === 'readonly'">{{ comment }}</textarea>
    </div>

  </div>
</template>

<style scoped>

</style>
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
  { value: 'accepted', label: 'Annehmen' },
  { value: 'asExamCertificate', label: 'Übungsschein'},
  { value: 'denied', label: 'Ablehnen'}
]

const decision = ref(props.displayDecision)
const comment = ref(props.comment)

defineExpose({
  decision,
  comment
})
</script>

<!-- todo accessibility -->
<template>
  <div>

    <div>

      <div v-if="type === 'edit'">
        <SelectButton :allow-empty="false" v-model="decision" :options="decisionOptions" optionLabel="label" optionValue="value" />
      </div>

      <div v-else-if="type === 'readonly'">

        <img v-if="displayDecision === 'accepted'" src="../assets/icons/ModuleAccepted.svg">
        <img v-else src="../assets/icons/ModuleAcceptedGray.svg">

        <img v-if="displayDecision === 'asExamCertificate'" src="../assets/icons/ModuleAsExamCertificate.svg">
        <img v-else src="../assets/icons/ModuleAsExamCertificateGray.svg">

        <img v-if="displayDecision === 'denied'" src="../assets/icons/ModuleDenied.svg">
        <img v-else src="../assets/icons/ModuleDeniedGray.svg">

      </div>
    </div>

    <div>
      <textarea v-model="comment" :readonly="type === 'readonly'" />
    </div>

  </div>
</template>

<style scoped>

</style>
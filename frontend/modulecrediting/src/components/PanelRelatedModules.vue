<!--
list of related module connections
props:
  - relatedModules (Array)
      each item should have properties: decision, decisionDate, university, course, externalModules, internalModules
displays:
  - list of related module connections
-->

<script setup>
import PanelHeader from "@/components/PanelHeader.vue";

const props = defineProps({
  relatedModules: {
    required: true,
    type: Array,
    validator(value) {
      return value.every((relatedModule) => {
        return relatedModule.decision
            && relatedModule.decisionDate
            && relatedModule.university
            && relatedModule.course
            && relatedModule.externalModules
            && relatedModule.internalModules
      })
    }
  }
})
</script>

<template>
  <div>
    <h3>Ähnliche Module:</h3>
    <div v-for="module in relatedModules">

      <div>
        <div v-if="module.decision === 'accepted'">
          <img src="../assets/icons/ModuleAccepted.svg">
        </div>
        <div v-else-if="module.decision === 'asExamCertificate'">
          <img src="../assets/icons/ModuleAsExamCertificate.svg">
        </div>
        <div v-else-if="module.decision === 'denied'">
          <img src="../assets/icons/ModuleDenied.svg">
        </div>
      </div>

      <PanelHeader :internal-modules="module.internalModules" :external-modules="module.externalModules" />

      <div>
        <img src="../assets/icons/DecisionDate.svg">
        <p>{{ module.decisionDate }}</p>
      </div>

      <p>{{ module.university }}</p>

      <p>{{ module.course }}</p>

    </div>
  </div>
</template>

<style scoped>

</style>
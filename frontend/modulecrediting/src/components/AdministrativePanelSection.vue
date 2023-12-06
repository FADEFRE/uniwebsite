<!--
block with elements for study office
displays:
- select button
- comment field
functionality:
- get decision suggestion and comment input
-->

<script setup>
import PanelComment from "@/components/PanelComment.vue";
import { ref, onMounted } from "vue";

const props = defineProps(['readOnly', 'data'])
// data is optional if used has to be of type object with properties decision and comment

const decision = ref()
if (props.data?.decision) {
  if (props.data.decision === 'ABGELEHNT') {
    decision.value = 'Ablehnen'
  } else {
    if (props.data.decision === 'ANGENOMMEN') {
      decision.value = 'Annehmen'
    }
  }
}

const decisionSuggestionOptions = ['Ablehnen', 'Annehmen']

const commentData= ref()
if (props.data?.comment) {
  commentData.value = props.data.comment
}
const comment = ref()

defineExpose({
  decision, comment
})
</script>

<template>
  <div class="study-office-container">
    <div>
      <!-- todo change disabled style -->
      <SelectButton
          :disabled="props.readOnly"
          :allow-empty="false"
          v-model="decision"
          :options="decisionSuggestionOptions"
      />
    </div>
    <div>
      <PanelComment :read-only="props.readOnly" :comment="commentData" ref="comment" />
    </div>
  </div>
</template>

<style scoped>

</style>
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
// data is optional if used has to be of type object with properties decisionSuggestion and commentStudyOffice

const decisionSuggestion = ref()
if (props.data?.decisionSuggestion) {
  if (props.data.decisionSuggestion === 'ABGELEHNT') {
    decisionSuggestion.value = 'Ablehnen'
  } else {
    if (props.data.decisionSuggestion === 'ANGENOMMEN') {
      decisionSuggestion.value = 'Annehmen'
    }
  }
}

const decisionSuggestionOptions = ['Ablehnen', 'Annehmen']

const comment = ref()
if (props.data?.commentStudyOffice) {
  comment.value = props.data.commentStudyOffice
}
const commentStudyOffice = ref()

const selectButtonClick = (event) => {
  if (props.readOnly) event.stopImmediatePropagation()
}

defineExpose({
  decisionSuggestion, commentStudyOffice
})
</script>

<template>
  <div class="study-office-container">
    <div>
      <SelectButton
          :allow-empty="false"
          v-model="decisionSuggestion"
          :options="decisionSuggestionOptions"
          @click="selectButtonClick"
      />
    </div>
    <div>
      <PanelComment :comment="comment" ref="commentStudyOffice" />
    </div>
  </div>
</template>

<style scoped>

</style>
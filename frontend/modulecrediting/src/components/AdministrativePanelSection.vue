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
    <hr>
    <div class="study-office-content">
      <div v-if="props.readOnly">

        <div class="icon-container">
          <div v-if="data.decision === 'ANGENOMMEN'">
            <span class="pi pi-check" style="color: green; font-size: 1.5rem"></span>
          </div>

          <div v-else-if="data.decision === 'ABGELEHNT'">
            <span class="pi pi-times" style="color:red; font-size: 1.5rem;"></span>
          </div>
        </div>

      </div>
      <div v-else>

        <SelectButton
            :allow-empty="false"
            v-model="decision"
            :options="decisionSuggestionOptions"
        />

      </div>
      <div>
        <PanelComment :read-only="props.readOnly" :comment="commentData" ref="comment" />
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
<script setup>
import { ref, computed, watch } from "vue";
import ModuleStatusIcon from "@/assets/icons/ModuleStatusIcon.vue";
const props = defineProps({
  readonly: {
    required: true,
    type: Boolean
  },
  displayDecision: {
    type: String,
    validator(value) {
      return ['accepted', 'asExamCertificate', 'denied', 'unedited'].includes(value)
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

<template>
  <div class="panel-decision-block">

    <div v-if="!readonly">
      <SelectButton :allow-empty="false" :options="decisionOptions" optionLabel="label" optionValue="value" v-model="decision" />
    </div>

    <div v-else class="readonly-decision-container">

      <div class="icon-container" :class="{ 'highlight': displayDecision === 'accepted' }">
        <ModuleStatusIcon statusDecision="accepted" :gray="displayDecision !== 'accepted'" size="small"/>
      </div>

      <div class="icon-container" :class="{ 'highlight': displayDecision === 'asExamCertificate' }">
        <ModuleStatusIcon statusDecision="asExamCertificate" :gray="displayDecision !== 'asExamCertificate'" size="small"/>
      </div>
      <div class="icon-container" :class="{ 'highlight': displayDecision === 'denied' }">
        <ModuleStatusIcon statusDecision="denied" :gray="displayDecision !== 'denied'" size="small"/>
      </div>
    </div>

    <textarea :readonly="readonly" :placeholder="$t('PanelDecisionBlock.reason')" v-model="comment" @change="emit('change')" class="white"></textarea>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.panel-decision-block {
  display: flex;
  width: 100%;

  border: 2px solid $dark-gray;
}


:deep(.p-button) {
  width: 100%;
}

:deep(.p-button:first-child.p-highlight) {
  background-color: $green;
  &:hover, &:focus {
    background-color: $green-hover;
  }
}
:deep(.p-button:nth-child(2).p-highlight) {
  background-color: $orange;
  &:hover, &:focus {
    background-color: $orange-hover;
  }
  
}
:deep(.p-button:last-child.p-highlight) {
  background-color: $red;
  &:hover, &:focus {
    background-color: $red-hover;
  }
}


.readonly-decision-container {
  display: flex;

  flex-direction: column;
  justify-content: space-around;
  border-right: 2px solid $black;
  min-height: rem(118px);
  width: spacing(xxxl);
}

.icon-container {
  height: 33%;
  display: flex;
  align-items: center;
  justify-content: center;
  align-self: stretch;
  &.highlight {
    background-color: $gray;
  }
}

</style>
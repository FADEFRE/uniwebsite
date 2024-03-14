<script setup>
/*
decision section
this component controls layout, the below mentioned slots should hold the actual decision logic

slots:
  default - should be used if prop type is 'single'
  study-office - should be used if prop type is 'study-office-chairman', displayed left
  chairman - should be used if prop type is 'study-office-chairman', displayed right
 */

const props = defineProps({
  /* 'single' or 'study-office-chairman' */
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['single', 'study-office-chairman'].includes(value)
    }
  }
})
</script>

<template>
  <div class="panel-container">
    <h4>{{ $t('PanelDecision.Decision') }}</h4>

    <div v-if="type === 'single'" class="decision-container">
      <slot />
    </div>

    <div v-else-if="type === 'study-office-chairman'" class="decision-split-container">
      <slot name="study-office" />
      <slot name="chairman" />
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.decision-split-container {
  @include screenSplit();
  width: 100%;
  height: min-content;

  @include breakpoint(m) {
    flex-wrap: wrap;
    flex-direction: column;
  }
}

.decision-container {
  width: 100%;
  height: min-content;
}
</style>
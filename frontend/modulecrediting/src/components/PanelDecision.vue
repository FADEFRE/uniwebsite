<!--
displays decision blocks via slots
props:
  - type (may be 'single' or 'study-office-chairman')
slots:
  - default (used if type === 'single')
  - study-office (used if type === 'study-office-chairman')
  - chairman (used if type === 'study-office-chairman')
-->

<script setup>
const props = defineProps({
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
  <div class="panel-decision">
    <h4>Entscheidung</h4>

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

.panel-decision {
  @include panelComponent();
}

.decision-split-container {
  @include screenSplit();
  width: 100%;
  height: min-content;

  @media only screen and (max-width: 1000px) {
    flex-wrap: wrap;
  }
}

.decision-container {
  width: 100%;
  height: min-content;
}
</style>
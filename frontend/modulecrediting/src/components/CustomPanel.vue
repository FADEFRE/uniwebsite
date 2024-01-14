<!--
modified PrimeVue Panel component
uses custom toggle icon
slots header, icons and default are passed on to PrimeVue Panel
-->

<script setup>
import { ref } from "vue";

const arrowStyle = ref('arrow-icon arrow-up')

const toggleArrowStyle = (collapsed) => {
  if (collapsed) {
    arrowStyle.value = 'arrow-icon'
  } else {
    arrowStyle.value = 'arrow-icon arrow-up'
  }
}
</script>

<template>
  <Panel toggleable @update:collapsed="toggleArrowStyle">
    <template #header>
      <slot name="header"></slot>
    </template>
    <template #icons>
      <slot name="icons"></slot>
    </template>
    <template #togglericon>
      <img src="@/assets/icons/ArrowRed.svg" :class="arrowStyle">
    </template>
    <slot></slot>
  </Panel>
</template>

<style scoped lang="scss">
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';

:deep(.p-panel) {
  width: 100%;
  background-color: $white;
}

:deep(.p-panel-header) {
  width: 100%;
  padding: 1.25rem;
  background-color: $white;

  border: none;
  border-top: 2px solid $dark-gray;
  border-bottom: 2px solid $dark-gray;

  display: flex;
  padding: 1.25rem;
  justify-content: space-between;
  align-items: center;
}

:deep(.p-panel-icons) {
  display: flex;
  align-items: center;
  gap: 1.875rem;
  margin-left: 0.625rem;
}

:deep(.p-panel-content) {
  border: none;
  padding: 1.25rem 6.25rem;
  border-bottom: 2px solid $dark-gray;

  @include verticalList(small);

}

.arrow-icon {
  transition: 0.2s ease-in-out;
}

.arrow-up {
  transform: rotate(180deg);
}
</style>
<!--
modified PrimeVue Panel component
uses custom toggle icon
slots header, icons and default are passed on to PrimeVue Panel
-->

<script setup>
import { ref } from "vue";

const collapsed = ref()
</script>

<template>
  <Panel toggleable :collapsed="collapsed">
    <template #header>
      <slot name="header"></slot>
    </template>
    <template #icons>
      <slot name="icons"></slot>
      <img src="@/assets/icons/ArrowRed.svg" @click="collapsed = !collapsed">

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

:deep(.p-panel-toggler) {
  display: none !important;
}

:deep(.p-panel-content) {
  border: none;
  padding: 1.25rem 6.25rem;
  border-bottom: 2px solid $dark-gray;

  @include verticalList(small);

}
</style>
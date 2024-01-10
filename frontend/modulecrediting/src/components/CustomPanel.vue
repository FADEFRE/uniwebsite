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
      <slot name="header" />
    </template>
    <template #icons>
      <slot name="icons" />
      <img src="@/assets/icons/ArrowRed.svg" @click="collapsed = !collapsed">
    </template>
    <slot />
  </Panel>
</template>

<style scoped>
:deep(.p-panel-toggler) {
  display: none !important;
}
</style>
<!--
shows mapping of external modules on internal modules
props:
  - externalModules (Array of module names)
  - internalModules (Array of module names)
displays:
  - list of external modules
  - arrow icon
  - list of internal modules
-->

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  externalModules: {
    type: Array
  },
  internalModules: {
    type: Array
  }
});
</script>

<template>
  <div class="panel-header">
    <div class="external-modules">
      <h3 v-if="externalModules">{{ externalModules.join(', ') }}</h3>
      <h3 v-else>...</h3>
    </div>

    <div class="arrow-icon">
      <img src="@/assets/icons/ArrowRed.svg" alt="Arrow Icon" />
    </div>

    <div class="internal-modules">
      <h3 v-if="internalModules">{{ internalModules.join(', ') }}</h3>
      <h3 v-else>...</h3>
    </div>
  </div>
</template>

<style scoped lang="scss">
.panel-header {
  display: flex;
  align-items: center;
  gap: 0.625rem;
}
.arrow-icon {
  transform: rotate(-90deg);
}
</style>

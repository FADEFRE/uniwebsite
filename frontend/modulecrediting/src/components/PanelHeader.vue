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
      <p v-if="externalModules">{{ externalModules.join(', ') }}</p>
      <p v-else>...</p>
    </div>

    <div class="arrow-icon">
      <img src="@/assets/icons/ArrowRed.svg" alt="Arrow Icon" />
    </div>

    <div class="internal-modules">
      <p v-if="internalModules">{{ internalModules.join(', ') }}</p>
      <p v-else>...</p>
    </div>
  </div>
</template>

<style scoped>
.panel-header {
    display: flex;
    align-items: center;
}
</style>

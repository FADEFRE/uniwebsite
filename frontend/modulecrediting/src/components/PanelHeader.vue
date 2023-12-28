<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    externalModules: {
      required: true,
      type: Array
    },
    internalModules: {
      required: true,
      type: Array
    }
});
</script>

<template>
  <div class="panel-header">
    <div class="external-modules">
      <p>{{ externalModules.join(', ') }}</p>
    </div>

    <div class="arrow-icon">
      <img src="@/assets/icons/ArrowRed.svg" alt="Arrow Icon" />
    </div>

    <div class="internal-modules">
      <p>{{ internalModules.join(', ') }}</p>
    </div>
  </div>
</template>

<style scoped>
.panel-header {
    display: flex;
    align-items: center;
}
</style>

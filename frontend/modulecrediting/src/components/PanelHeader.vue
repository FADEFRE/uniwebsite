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
import { computed } from "vue";

const props = defineProps({
  externalModules: {
    type: Array
  },
  internalModules: {
    type: Array
  },
  relatedModules: {
    type: Boolean
  }
});

const textStyle = computed(() => {
  if (props.relatedModules) return "overview-text";
  return "";
})
</script>

<template>
  <div class="panel-header">
    <div class="modules-container">
      <template v-if="externalModules && externalModules.length">
        <h3 v-for="(externalModule, index) in externalModules" :class="textStyle" class="single-module" :key="index">
          {{ externalModule }}<span v-if="index < externalModules.length - 1">,</span>
        </h3>
      </template>
      <h3 v-else>...</h3>
    </div>

    <div class="arrow-icon">
      <img src="@/assets/icons/ArrowRed.svg" alt="Arrow Icon" />
    </div>

    <div class="modules-container">
        <template v-if="internalModules && internalModules.length">
          <h3 v-for="(internalModule, index) in internalModules" :class="textStyle" class="single-module" :key="index">
            {{ internalModule }}<span v-if="index < internalModules.length - 1">,</span>
          </h3>
        </template>
        <h3 v-else>...</h3>
      </div>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.panel-header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 0.625rem;
}

.modules-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.3rem;
}

.single-module {
}

.arrow-icon {
  @include rightArrow();
  // Ensure the arrow icon doesn't cause unnecessary wrapping
}

.overview-text {
  text-transform: none;
}
</style>

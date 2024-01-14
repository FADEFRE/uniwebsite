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
    <div class="external-modules">
      <h3 v-if="externalModules && externalModules.length" :class="textStyle">{{ externalModules.join(', ') }}</h3>
      <h3 v-else>...</h3>
    </div>

    <div class="arrow-icon">
      <img src="@/assets/icons/ArrowRed.svg" alt="Arrow Icon" />
    </div>

    <div class="internal-modules">
      <h3 v-if="internalModules && internalModules.length" :class="textStyle">{{ internalModules.join(', ') }}</h3>
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

.arrow-icon {
  @include rightArrow();
}
.overview-text {
  text-transform: none;
}

</style>

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

</script>

<template>
  <div class="panel-heading-container">
    <div class="modules-container">
      <h3 v-if="externalModules && externalModules.length" :class="{ 'related-modules-text': relatedModules }"
        class="single-module">
        {{ externalModules.join(', ') }}
      </h3>
      <h3 v-else>...</h3>
    </div>

    <div class="arrow-icon">
      <img src="@/assets/icons/ArrowRed.svg" alt="Arrow Icon" />
    </div>

    <div class="modules-container">
      <h3 v-if="internalModules && internalModules.length" :class="{ 'related-modules-text': relatedModules }"
        class="single-module">
        {{ internalModules.join(', ') }}
      </h3>
      <h3 v-else>...</h3>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;

.panel-heading-container {
  display: flex;
  gap: spacing(s);
  width: 100%;
  overflow: hidden;
}

.modules-container {
  overflow: hidden;

}

.single-module {
  @include ellipsisTextOverflow();
}

.arrow-icon {
  @include rightArrow();
  // Ensure the arrow icon doesn't cause unnecessary wrapping
}
</style>

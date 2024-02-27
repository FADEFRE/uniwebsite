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
import ArrowIcon from "../assets/icons/ArrowIcon.vue";

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
  <h3 class="panel-heading-container">
    <span class="modules-container">
      <span v-if="externalModules && externalModules.length" :class="{ 'related-modules-text': relatedModules }"
        class="single-module">
        {{ externalModules.join(', ') }}
      </span>
      <span v-else>(Anzurechnende Module)</span>
    </span>

    <span class="arrow-icon">
      <ArrowIcon color="red" direction="right" aria-label="wird angerechnet für" />
    </span>

    <span class="modules-container">
      <span v-if="internalModules && internalModules.length" :class="{ 'related-modules-text': relatedModules }"
        class="single-module">
        {{ internalModules.join(', ') }}
      </span>
      <span v-else>(Module der Universität Leipzig)</span>
    </span>
  </h3>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

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
  display: block;
}
</style>

<script setup>
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";

/*
header component describing external and internal modules selected,
displays comma-separated list of external module names mapped to comma-separated list of internal module names
 */

const props = defineProps({
  /* array of Strings, external module names */
  externalModules: {
    type: Array
  },
  /* array of Strings, internal module names */
  internalModules: {
    type: Array
  },
  /* should be set true to apply different styling for displaying related modules */
  relatedModules: {
    type: Boolean
  }
})
</script>

<template>
  <h3 class="panel-heading-container">
    <span class="modules-container">
      <span v-if="externalModules && externalModules.length" :class="{ 'related-modules-text': relatedModules }"
        class="single-module">
        {{ externalModules.join(', ') }}
      </span>
      <span v-else class="single-module">({{ $t('PanelHeader.ModulesToBeCredited') }})</span>
    </span>
    
    <div class="second-row">
    <div class="arrow-icon-container" :aria-label="$t('PanelHeader.AriaCreditedFor')">
      <ArrowIcon color="red" direction="right" :size="relatedModules ? 'small' : 'mid'"/>
    </div>

    <span class="modules-container">
      <span v-if="internalModules && internalModules.length" :class="{ 'related-modules-text': relatedModules }"
        class="single-module">
        {{ internalModules.join(', ') }}
      </span>
      <span v-else class="single-module">({{ $t('PanelHeader.ModulesUniLeipzig') }})</span>
    </span>
  </div>
  </h3>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.panel-heading-container {
  width: 100%;

  display: flex;
  gap: spacing(s);
  
  overflow: hidden;

  @include breakpoint(m) {
    @include verticalList(none);
  }
}

.second-row {
  display: flex;
  gap: spacing(s);
}

.modules-container {
  overflow: hidden;
}

.single-module {
  @include ellipsisTextOverflow();
  display: block;
}

.arrow-icon-container {
  display: flex;
  align-items: center;
}
</style>

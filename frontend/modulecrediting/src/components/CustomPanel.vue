<!--
modified PrimeVue Panel component
uses custom toggle icon
slots header, icons and default are passed on to PrimeVue Panel
-->

<script setup>
import { ref, computed } from "vue";

const props = defineProps({
  initialCollapsedState: {
    type: Boolean,
    default: true
  }
})

const d_collapsed = ref(props.initialCollapsedState)
const arrowStyle = computed(() => d_collapsed.value ? 'arrow-icon' : 'arrow-icon arrow-up')

const setCollapsed = (value) => {
  d_collapsed.value = value
}

defineExpose({
  setCollapsed
})
</script>

<template>
  <div class="custom-panel-container">
    <Panel toggleable v-model:collapsed="d_collapsed">
      <template #header>
        <slot name="header"></slot>
      </template>
      <template #icons>
        <slot name="icons"></slot>
      </template>
      <template #togglericon>
        <img src="@/assets/icons/ArrowRed.svg" :class="arrowStyle">
      </template>
      <slot></slot>
    </Panel>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;

:deep(.p-panel) {
  @include boxShadow(big);
  width: 100%;
}

:deep(.p-panel-header) {
  width: 100%;
  padding: 1.25rem;
  background-color: $white;

  border: none;
  border-top: 2px solid $dark-gray;
  border-bottom: 2px solid $dark-gray;

  display: flex;
  justify-content: space-between;
  align-items: center;
}

:deep(.p-panel-icons) {
  display: flex;
  align-items: center;
  margin-left: 0.5rem;
}

:deep(.p-panel-content) {
  border: none;
  padding: 1.25rem 9%;
  border-bottom: 2px solid $dark-gray;

  @include verticalList(small);

  @media only screen and (max-width: 1350px) {
    padding: 1.25rem 5%;
  }

  @media only screen and (max-width: 700px) {
    padding: 1.25rem 1.5rem;
  }
}


:deep(.p-panel-header-icon) {
  @include smallHighlightBox();
  background-color: $white;

  &:hover {
    background-color: $white-hover;
  }
}

.arrow-icon {
  transition: 0.3s ease-in-out;
}

.arrow-up {
  transform: rotate(180deg);
}
</style>
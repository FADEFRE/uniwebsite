<script setup>
import { ref, computed } from "vue";
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";

/*
customized PrimeVue Panel,
has customized toggle icon

slots:
  header - use to access the PrimeVue Panel header slot
  icons - use to access the PrimeVue Panel icons slot
  default - use to access the PrimeVue Panel default slot (content)
 */

const props = defineProps({
  /* should be set false if the panel should be initially collapsed */
  initialCollapsedState: {
    type: Boolean,
    default: true
  }
})

const d_collapsed = ref(props.initialCollapsedState)

const setCollapsed = (value) => {
  d_collapsed.value = value
}

const ariaLabelKey = computed(() => d_collapsed.value ? 'CustomPanel.AriaOpenPanel' : 'CustomPanel.AriaClosePanel')

defineExpose({
  /* function (Boolean value) to set collapsed value of the panel */
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
        <span :aria-label="$t(ariaLabelKey)">
          <ArrowIcon :direction="d_collapsed ? 'down' : 'up'" color="red"/>
        </span>
      </template>
      <slot></slot>
    </Panel>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

:deep(.p-panel) {
  width: 100%;
}

:deep(.p-panel-header) {
  width: 100%;
  padding: spacing(l);
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
  margin-left: spacing(s);
  gap: spacing(m);

  @include breakpoint(m) {
    gap: spacing(s);
  }
}

:deep(.p-panel-content) {
  border: none;
  padding: spacing(l) 9%;
  border-bottom: 2px solid $dark-gray;


  @include breakpoint(xl) {
    padding: spacing(m) 5%;
  }

  @include breakpoint(s) {
    padding: spacing(l);
  }
}


:deep(.p-panel-header-icon) {
  @include smallHighlightBox();
  background-color: $white;

  &:hover, &:focus {
    background-color: $white-hover;
  }
}
</style>
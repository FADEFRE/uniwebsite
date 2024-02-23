<script setup>
import ArrowIcon from '@/assets/icons/ArrowIcon.vue';

const props = defineProps({
  fixed: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  },
  redButton: {
    type: Boolean,
    default: false
  },
  external: {
    type: Boolean,
    default: false
  }
})
</script>


<template>
    <Button
        role="link"
        :disabled="disabled"
        :class="{ ' fixed-button': fixed, 'disabled': disabled, 'red-button': redButton }"
        class="icon-hover-right external"
    >
      <slot />
      <ArrowIcon direction="right" :hover="true"/>
      <small v-if="external" class="helper-text">(externer Link)</small>
    </Button>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.p-button.external {
  position: relative;
  padding-bottom: spacing(m);
}

small {
  position: absolute;
  bottom: spacing(xs);
  left: spacing(s);
  text-transform: none;
  font-size: 0.75rem;
  font-weight: 430;
  color: $white;
  

}
</style>

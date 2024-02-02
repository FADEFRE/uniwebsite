<script setup>
import { computed } from 'vue';

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
  }
})

const style = computed(() => {
  return props.primaryButton ? "button-link-container primary-button" : "button-link-container";
});
</script>


<template>
  <div class="button-link-container">
    <Button :disabled="disabled" :class="{ ' fixed-button': fixed, 'disabled': disabled, 'red-button': redButton}">
      <slot />
      <img src="../assets/icons/ArrowWhite.svg" class="arrow-icon" alt="Arrow Icon">
    </Button>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';

.button-link-container {
  &:hover {
    .arrow-icon {
      transform: translate(0.15rem) rotate(-90deg);
    }
  }
}

.p-button {
  display: flex;
  gap: 0.625rem;
}

.arrow-icon {
  transform: rotate(-90deg);
  transition: 0.1s ease-in-out;
}

.red-button {
  background-color: $red;
  &:hover {
    background-color: $red-hover;
  }
}

.fixed-button {
  background-color: $red;
  position: fixed;
  z-index: 3;
  bottom: 2rem;
  right: 1rem;

  &:hover {
    background-color: $red-hover;
  }
}



.disabled {
  opacity: 0.5;

}
</style>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  primaryButton: {
    type: Boolean,
    default: false
  },
  disabled: {
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
    <Button :disabled="disabled" :class="{ ' primary-button': primaryButton, 'disabled': disabled }">
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

.primary-button {
  background-color: $red;
  position: fixed;
  bottom: 2rem;
  right: 2rem;

  &:hover {
    background-color: $dark-red;
  }
}

.disabled {
  background-color: $mid-gray;
  color: rgb(163, 163, 163);

  & .arrow-icon {
    display: none;
  }

  &:hover {
    background-color: $mid-gray;
    color: rgb(182, 182, 182);
  }
}
</style>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  primaryButton: {
    type: Boolean,
    default: false
  }
})

const style = computed(() => {
  return props.primaryButton ? "button-container primary-button" : "button-container";
});
</script>


<template>
  <div>
    <Button :class="style">
      <slot />
      <img src="../assets/icons/ArrowWhite.svg" class="arrow-icon" alt="Arrow Icon">
    </Button>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';

.button-container {
  display: flex;
  gap: 0.625rem;

  &:hover {
    .arrow-icon {
      transform: translate(0.15rem) rotate(-90deg);
    }
  }
}

.arrow-icon {
  transform: rotate(-90deg);
  transition: 0.1s ease-in-out;
}

.primary-button {
  background-color: $red;

  &:hover {
    background-color: $dark-red;
  }
}
</style>

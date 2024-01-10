<script setup>
import { useRouter } from "vue-router";
import { defineProps, computed } from 'vue';

const props = defineProps({
  routeName: {
    required: true,
    type: String
  },
  primaryButton: {
    type: Boolean,
    default: false
  }
})

const router = useRouter();

const openLink = () => {
  router.push({ name: props.routeName });
}

const style = computed(() => {
  return props.primaryButton ? "button-container primary-button" : "button-container";
});
</script>


<template>
  <div>
    <Button @click="openLink" :class="style">
      <slot></slot>
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

<script setup>
const props = defineProps({
  header: {
    required: true,
    type: String,
  },
  text: {
    required: true,
    type: String
  }
})
</script>

<template>
  <div class="homepage-container">
    <h2>{{ header }}</h2>
    <p class="text-justify">{{ text }}</p>
    <div class="input-button-with-invalid-container">
      <slot></slot>
    </div>
  </div>
</template>


<style lang="scss" scoped>
@use '@/assets/styles/global' as *;
@use '@/assets/styles/util' as *;
@use '@/assets/styles/components' as *;


.homepage-container {
  @include basicContainer();
  @include verticalListItem($white);
  @include verticalList(l);
}

.input-button-with-invalid-container {
  display: flex;
  flex-direction: column;
  gap: spacing(xs);

  @include breakpoint(xs) {
    width: 100%;
  }
}

:slotted(.input-button-container) {
  width: 100%;
  display: flex;
  flex-direction: row;
  @include breakpoint(xs) {
    flex-direction: column;
  }
}

:deep(.p-inputtext) {
  width: 100%;
  max-width: 250px;

  @include breakpoint(xs) {
    max-width: 100%;
  }

  text-align: center;
  font-family: 'Jost';
  font-weight: 500;
  letter-spacing: 0.3rem;
}
</style>


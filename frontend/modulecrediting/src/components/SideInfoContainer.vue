<script setup>
const props = defineProps(['heading'])
</script>

<template>
    <div class="sideinfo-container">
        <h3>{{ heading }}</h3>
        <div>
            <slot></slot>
        </div>
    </div>
</template>

<style lang="scss" scoped>
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.sideinfo-container {
    max-width: 330px;
    background-color: $white;
    padding: 0.9375rem 1.5625rem;

    display: flex;
    flex-direction: column;
    gap: 1rem;
}

:slotted(.list-container) {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 0.625rem;
}

:slotted(.list-item) {
  position: relative;
  padding-left: 1rem;
  list-style: none;
}

:slotted(.list-item::after) {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background-color: $red;
}
</style>

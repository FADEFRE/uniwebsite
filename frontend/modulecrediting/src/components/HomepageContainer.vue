<script setup>
const props = defineProps(['header', 'text'])
</script>

<template>
    <div class="homepage-container">
        <div>
            <h2 v-if="header">{{ header }}</h2>
            <h2 v-else>Platzhalter heading</h2>
        </div>
        <div v-if="text">
            <p>{{ text }}</p>
        </div>
        <div v-else>
            <p>Platzhalter Text</p>
        </div>
        <div class="input-button-with-invalid-container">
            <slot></slot>
        </div>

    </div>
</template>


<style lang="scss" scoped>
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.homepage-container {
    @include basicContainer();
    @include verticalListItem($white);
    @include verticalList(big);
}

.input-button-with-invalid-container {
    display: flex;
    flex-direction: column;
    gap: 0.2rem;
}

:slotted(.input-button-container) {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
}

:deep(.p-inputtext) {
    background-color: $gray;
    border: none;

    text-align: center;
    font-family: 'Jost';
    font-weight: 500;
    letter-spacing: 0.3125rem;
    width: 100%;
    max-width: 250px;

    &.invalid {
        border: 2px solid $red;
    }
}
:slotted(.invalid-text) {
  color: $red;
}
</style>


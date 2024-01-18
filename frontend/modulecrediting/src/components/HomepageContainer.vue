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
        <div class="input-button-container">
            <slot></slot>    
        </div>
        
    </div>
</template>


<style lang="scss" scoped>
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.homepage-container {
    @include verticalListItem($white);
    display: flex;
    padding: 0.9375rem 1.25rem;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    gap: 1.25rem;
    align-self: stretch;
}

.input-button-container{
    width: 100%;
    display: flex;

    
}

:deep(.p-inputtext) {
    background-color: $gray;
    border: none;
    
    text-align: center;
    font-family: 'Jost';
    font-size: 1.25rem;
    font-weight: 500;
    letter-spacing: 0.3125rem;
}
</style>


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
        <div>
            <slot></slot>
        </div>
    </div>
</template>

<style scoped lang="scss">

.homepage-container {
    background-color: #fff;
    border-left: 3px solid var(--Red, #D83D3B);

    display: flex;
    padding: 0.9375rem 1.25rem;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    gap: 1.25rem;
    align-self: stretch;
}
</style>

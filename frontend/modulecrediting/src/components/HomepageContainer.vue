<script setup>
const props = defineProps(['header', 'text'])
</script>

<template>
    <div>
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

<style>

</style>

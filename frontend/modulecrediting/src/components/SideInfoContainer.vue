<script setup>
const props = defineProps(['heading'])
</script>

<template>
    <div>
        <h3>{{ heading }}</h3>
        <div>
            <slot></slot>
        </div>
    </div>
</template>

<style scoped>

</style>

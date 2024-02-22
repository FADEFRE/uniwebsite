<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';


const props = defineProps(['customTitle', 'customMessage']);
</script>

<template>
    <div class="main">
        <ErrorContainer :customTitle="'400'" :customMessage="'Bad Request'" />
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>

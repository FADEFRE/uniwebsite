
<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';


const props = defineProps(['customTitle', 'customMessage']);
</script>

<template>
    <div class="main">
        <ErrorContainer :customTitle="'503'" :customMessage="'Server Unavailable'" />
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>
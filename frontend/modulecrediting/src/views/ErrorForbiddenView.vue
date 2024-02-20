<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';
</script>

<template>
    <div class="main centered">
        <ErrorContainer
            customTitle="403 - Forbidden"
            customMessage="Du darfst nicht auf diese Adresse zugreifen"
        />
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
</style>

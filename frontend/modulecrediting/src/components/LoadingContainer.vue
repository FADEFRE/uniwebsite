<script setup>
</script>

<template>
    <div class="loading-container">
        <div class="loader"></div>
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;

.loading-container {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%);
}

.loader {
    border: 5px solid rgba($color: $dark-gray, $alpha: 0.1);
    border-top: 5px solid $red;
    border-radius: 50%;
    width: 50px;
    height: 50px;
    animation: spin 2s linear infinite;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}</style>
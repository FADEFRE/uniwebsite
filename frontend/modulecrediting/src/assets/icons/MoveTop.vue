<script setup>
import ArrowIcon from './ArrowIcon.vue';

const scrollTop = () => {
  document.getElementById('app').scrollIntoView({ behavior: 'smooth' })
}
</script>

<template>
    <div class="move-top-button" @click="scrollTop">
        <ArrowIcon color="white" direction="up" size="big" />
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.move-top-button {
    background-color: $dark-gray;
    width: 3rem;
    height: 3rem;

    transition: 0.1s ease-in-out;

    &:hover {
        background-color: $dark-gray-hover;
        cursor: pointer;
    }

    display: flex;
    justify-content: center;
    align-items: center;
}
</style>

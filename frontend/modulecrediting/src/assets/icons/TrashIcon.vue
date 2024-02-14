<script setup>
const props = defineProps({
    color: {
        type: String,
        default: 'dark-gray'
    }
});
</script>

<template>
    <svg class="trash-icon" :class="color" viewBox="0 0 17 19" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M2.5 4.97768V18H14.5V4.97768L17 4.9941L13 4.97768M0 4.9941L4 4.97768M4 4.97768L5.5 1.01369L11.5 1L13 4.97768M4 4.97768H8.5H13M6.42308 8.60815V14.6852M10.5769 8.60815V14.6852" stroke-width="2" />
    </svg>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.trash-icon {
    width: rem(17px);
    height: rem(19px);

    transition: 0.1s ease-in-out;
}

.white {
    stroke: $white-hover;
}

.dark-gray {
    stroke: $dark-gray;
}

.icon-hover-white:hover .white {
    stroke: $white;
}
</style>
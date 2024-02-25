<script setup>
const props = defineProps({
    color: {
        type: String,
        default: 'white'
    },
    type: {
        type: String,
        required: true
    },
    hover: {
        type: Boolean,
        default: false
    }
});
</script>

<template>
    <svg v-if="type === 'login'" class="auth-icon" viewBox="0 0 19 22" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M6 6L11 11L6 16" :class="color" stroke-width="2.5" />
        <path d="M10 11H0" :class="color" stroke-width="2.5" />
        <path d="M10 20H17V2H10" :class="color" stroke-width="2.5" />
    </svg>

    <svg v-else class="auth-icon" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg">
<path d="M12.5 5.5L17.5 10.5L12.5 15.5" :class="color" stroke-width="2.5"/>
<path d="M6.5 10.5H16.5" :class="color" stroke-width="2.5"/>
<path d="M8.5 1.5L1.5 1.5L1.5 19.5H8.5" :class="color" stroke-width="2.5"/>
</svg>

</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.auth-icon {
    width: rem(20px);
    height: rem(20px);

    transition: 0.1s transform ease-in-out;
}
.white {
    stroke: $white;
}

.dark-gray {
    stroke: $dark-gray;
}

.icon-hover-right:hover .auth-icon,
.icon-hover-right:focus .auth-icon{
    transform: translateX(spacing(xs));
}
</style>
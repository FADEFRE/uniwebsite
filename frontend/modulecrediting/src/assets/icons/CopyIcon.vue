<script setup>
const props = defineProps({
    disabled: {
        type: Boolean
    }
});

</script>

<template>
    <svg class="copy-icon" :class="{'disabled': disabled}" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="1" y="7" width="10" height="10" stroke="#262A31" stroke-width="2" />
        <rect x="7" y="1" width="10" height="10" fill="white" stroke="#262A31" stroke-width="2" />
        <title>{{ $t('CopyIcon.Copy') }}</title>
    </svg>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
.copy-icon {
    width: rem(18px);
    height: rem(18px);
    transition: 0.1s ease-in-out;

    &:hover{
        width: rem(19px);
        height: rem(19px);
    }
}
.disabled {
    stroke: $gray;
}
</style>

    
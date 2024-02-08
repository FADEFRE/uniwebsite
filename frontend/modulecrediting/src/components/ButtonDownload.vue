
<script setup>
const props = defineProps({
    fixed: {
        type: Boolean,
        default: false
    }
})
</script>

<template>
    <div class="button-container">
        <Button :class="{ ' fixed-button': fixed }">
            Antrag herunterladen
            <img src="../assets/icons/Download.svg">
        </Button>
    </div>
    
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.button-container {
    display: flex;
    gap: 0.5rem;
}
</style>
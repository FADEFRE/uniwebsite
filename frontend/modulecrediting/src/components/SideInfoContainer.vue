<script setup>
const props = defineProps(['heading'])
</script>

<template>
    <div class="sideinfo-container">
        <h3 class="heading">{{ heading }}</h3>
            <slot></slot>
    </div>
</template>

<style lang="scss" scoped>
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.sideinfo-container {
    @include basicContainer();
    width: 100%;
}

:slotted(.main-info-container) {
    @include verticalList(l);
}

:slotted(.info-group-container){
    display: flex;
    flex-direction: column;
    gap: spacing(xs);
}

.heading {
    width: max-content;
    text-transform: uppercase;
}
</style>

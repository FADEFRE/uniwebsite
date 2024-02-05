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

.sideinfo-container {
    @include basicContainer();
    width: 100%;
}

:slotted(.main-info-container) {
    @include verticalList(big);
}

:slotted(.info-group-container){
    display: flex;
    flex-direction: column;
    gap: 0.3rem;
}

.heading {
    width: max-content;
    text-transform: uppercase;
}

:slotted(ul) {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
}

:slotted(li) {
    position: relative;
    padding-left: 1rem;
    list-style: none;
    width: 100%;

    &::before {
        content: '';
        position: absolute;
        top: 0.55rem;
        left: 0;
        width: 5px;
        height: 5px;
        border-radius: 50%;
        background-color: $red;
    }
}


</style>

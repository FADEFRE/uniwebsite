<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps(['customTitle', 'customMessage']);

</script>

<template>
    <div class="error-container">
        <div class="text-container">
            <h3>{{ customTitle }}</h3>
            <p>{{ customMessage }}</p>
        </div>
        <slot></slot>
    </div>
</template>

<style lang="scss" scoped>
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.error-container {
    @include singleContainer();
    @include verticalList(big);
    width: min-content;
    display: flex;
    align-items: center;
    gap: spacing(xl);
}
.text-container {
    display: flex;
    flex-direction: column;
    gap: spacing(m);
}

h3 {
    width: max-content;
}
p {
    text-align: center;
}

</style>

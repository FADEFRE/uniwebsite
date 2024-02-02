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
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';

.error-container {
    @include singleContainer();
    @include verticalList(big);
    width: min-content;
    display: flex;
    align-items: center;
    gap: 2rem;
}
.text-container {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

h3 {
    width: max-content;
}
p {
    text-align: center;
}

</style>

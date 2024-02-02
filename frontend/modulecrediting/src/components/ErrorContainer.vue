<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps(['customTitle', 'customMessage']);

</script>

<template>
    <div class="error-container">
        <h3>{{customTitle}}</h3>
        <p>{{customMessage}}</p>
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
}

h3 {
    width: max-content;
}
p {
    text-align: center;
}

</style>

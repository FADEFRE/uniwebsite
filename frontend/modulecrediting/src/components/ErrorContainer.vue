<script setup>
import { defineProps } from 'vue';
import ButtonLink from '@/components/ButtonLink.vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps(['customTitle', 'customMessage']);
const errorTexts = {
    title: 'Fehler 404: Seite nicht gefunden',
    message: 'Leider kann die gewünschte Seite nicht angezeigt werden.',
};
</script>

<template>
    <div class="error-container">
        <h3>{{ customTitle || errorTexts.title }}</h3>
        <p>{{ customMessage || errorTexts.message }}</p>
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

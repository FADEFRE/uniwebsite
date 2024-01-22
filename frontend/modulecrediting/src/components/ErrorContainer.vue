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
        <div>
            <h2>{{ customTitle || errorTexts.title }}</h2>
            <p>{{ customMessage || errorTexts.message }}</p>
            
            <div class="button-container">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.error-container {
    background-color: white;
    display: flex;
    padding: 0.9375rem 1.25rem;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1.25rem;
    align-self: stretch;
    max-width: 27.42188rem;
    max-height: 11.25rem;
    margin: 5rem auto 0;
}

.button-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.25rem;
}
</style>

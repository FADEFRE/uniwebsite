<script setup>
import { useRouter } from 'vue-router';
import ButtonLink from '../components/ButtonLink.vue';

const router = useRouter();
const redirectToHome = () => {
    router.push({ name: 'home' });
};
</script>

<template>
    <div class="error-container">
        <div>
            <h2>Fehler 404: Seite nicht gefunden</h2>
            <p>Leider kann die gewünschte Seite nicht angezeigt werden.</p>
        </div>
        <div class="button-container">
            <ButtonLink @click="redirectToHome">Zur Startseite</ButtonLink>
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



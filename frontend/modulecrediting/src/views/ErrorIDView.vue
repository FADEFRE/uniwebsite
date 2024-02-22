<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';
import { useRouter } from 'vue-router';
import ButtonLink from "@/components/ButtonLink.vue";

const router = useRouter();
const props = defineProps(['customTitle', 'customMessage']);

const goBack = () => {
    router.go(-2);
};

</script>

<template>
    <div class="main centered">
        <ErrorContainer :customTitle="'Antragsnummer existiert nicht'" :customMessage="'Antrag existiert nicht '">
            <template #default>
                <ButtonLink @click="goBack">Zurück</ButtonLink>
            </template>
        </ErrorContainer>
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
</style>

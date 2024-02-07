<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';
import { useRoute, useRouter } from 'vue-router';
import ButtonLink from "@/components/ButtonLink.vue";

const router = useRouter();
const props = defineProps(['customTitle', 'customMessage']);

const goBack = () => {
    router.go(-1);
};

</script>

<template>
    <div class="main centered">
        <ErrorContainer :customTitle="'404 Fehler: Seite nicht gefunden'"
            :customMessage="'Leider kann die gewünschte Seite nicht angezeigt werden'">
            <template #default>
                <ButtonLink @click="goBack">Zurück</ButtonLink>
            </template>
        </ErrorContainer>
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
</style>

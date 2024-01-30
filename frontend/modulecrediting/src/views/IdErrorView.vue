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
    <div class="main">
        <ErrorContainer :customTitle="'Antragsnummer existiert nicht'"
            :customMessage="'Antrag existiert nicht '" />
        <ButtonLink @click="goBack">Zurück</ButtonLink>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
    @include main();
}
</style>

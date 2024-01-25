<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';
import { useRoute, useRouter } from 'vue-router'; 
import ButtonLink from "@/components/ButtonLink.vue";

const router = useRouter();
const props = defineProps(['customTitle', 'customMessage']);

const goBack = () => {
    router.go(-2);
};

</script>

<template>
    <div class="main">
        <ErrorContainer :customTitle="'404 Fehler: Seite nicht gefunden'" 
        :customMessage="'Leider kann die gewünschte Seite nicht angezeigt werden'"/>  
        <ButtonLink @click ="goBack" >Zurück</ButtonLink>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
    @include main();
}
</style>

<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';
import { useRoute, useRouter } from 'vue-router';
import ButtonLink from "@/components/ButtonLink.vue";

const router = useRouter();
const props = defineProps(['customTitle', 'customMessage']);




</script>

<template>
    <div class="main">
        <ErrorContainer :customTitle="'403 - Forbidden'"
            :customMessage="'Du darfst nicht auf diese Adresse zugreifen'" />
    </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
    @include main();
    flex-direction: row !important;
}
</style>

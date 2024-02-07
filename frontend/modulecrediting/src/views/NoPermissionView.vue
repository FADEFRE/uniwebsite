<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const props = defineProps(['customTitle', 'customMessage']);

</script>

<template>
    <div class="main centered">
        <ErrorContainer :customTitle="'Keine Berechtigung'" 
                        :customMessage="'Du hast keine Berechtigung für den Zugriff'" />
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
</style>

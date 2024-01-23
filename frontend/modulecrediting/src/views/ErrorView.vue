<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';


const props = defineProps(['customTitle', 'customMessage']);
</script>

<template>
    <div class="main">
        <ErrorContainer :customTitle="props.customTitle" :customMessage="props.customMessage" />
    </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
    @include main();
}
</style>

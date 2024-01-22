<script setup>
import ErrorContainer from '../components/ErrorContainer.vue';


const props = defineProps(['customTitle', 'customMessage']);
</script>

<template>
    <div class="main">
    <ErrorContainer :customTitle="props.customTitle" :customMessage="props.customMessage"  />
        </div>
</template>

<style></style>

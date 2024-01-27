<script setup>
import ManagementCreateContainer from '../components/ManagementCreateContainer.vue';
</script>


<template>
    <div class="main">
        <div class="create-container-list">
            <ManagementCreateContainer :type="'course'" />
            <ManagementCreateContainer :type="'module'" />
        </div>
    </div>
</template>


<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
    @include main();
    max-width: 1200px
}

.create-container-list {
    @include verticalList(big);
    width: 100%;
}
</style>
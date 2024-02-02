<script setup>
import ManagementCreateContainer from '../components/ManagementCreateContainer.vue';
import ManagementEditCourse from '../components/ManagementEditCourse.vue';
import ManagementViewCourse from '../components/ManagementViewCourse.vue';
</script>


<template>
    <div class="main">
        <div class="create-container-list">
            <ManagementCreateContainer :type="'course'" />
            <ManagementCreateContainer :type="'module'" />
            <ManagementEditCourse />
            <ManagementViewCourse />
        </div>
    </div>
</template>


<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
    @include main();
}

.create-container-list {
    @include verticalList(big);
    width: 100%;
}
</style>
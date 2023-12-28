<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    moduleName: String,
    internalModules: Array,
});
</script>

<template>
    <div class="panel-header">
        <div class="module-name">
            {{ moduleName }}
        </div>

        <div class="arrow-icon">
                <img src="@/assets/icons/Arrow.svg" alt="Arrow Icon" />
        </div>

        <div class="internal-modules">
            <p v-for="internalModule in internalModules" :key="internalModule.id">
                {{ internalModule.moduleName }}
            </p>
        </div>
    </div>
</template>

<style scoped>
.panel-header {
    display: flex;
    align-items: center;
}
.module-name {
    margin-right: 10px;
}
</style>

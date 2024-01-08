<!-- ApplicationOverview.vue -->

<script setup>
import CreationDate from '@/assets/icons/CreationDate.svg';
import LastEditedDate from '@/assets/icons/LastEditedDate.svg';
import DecisionDate from '@/assets/icons/DecisionDate.svg';

const props = defineProps({
    creationDate: {
        type: String,
        required: true,
    },
    lastEditedDate: {
        type: String,
        required: true,
    },
    decisionDate: {
        type: String,
        required: true,
    },
    id: {
        type: String,
        required: true,
    },
    course: {
        type: String,
        // if not given use slot course
    },
    status: {
        type: String,
        required: true,
    },
});
</script>

<template>
    <div class="application-overview-container">
        <div class="dates">
            <!-- Div-Block Creation Date -->
            <div v-if="creationDate" class="date-block">
                <div class="text-wrapper-2">
                    <img :src="CreationDate" alt="Creation Date Icon" />
                    {{ creationDate }}
                </div>
            </div>

            <!-- Div-Block Last edited Date -->
            <div v-if="lastEditedDate" class="date-block">
                <div class="text-wrapper-2">
                    <img :src="LastEditedDate" alt="LastEdited Date Icon" />
                    {{ lastEditedDate }}
                </div>
            </div>

            <!-- Div-Block Decision Date -->
            <div v-if="decisionDate" class="date-block">
                <div class="text-wrapper-2">
                    <img :src="DecisionDate" alt="Decision Date Icon" />
                    {{ decisionDate }}
                </div>
            </div>
        </div>

        <!-- remaining data -->
        <div class="application-info">
            <div class="vorgangsnummer">
                <div class="vorgangsnummer-text overview-text">Vorgangsnummer: {{ id || 'Placeholder for Vorgangsnummer' }}
                </div>
            </div>
            <div class="status-text-wrapper">
                <div class="status-text-2 overview-text">Status: {{ status || 'Placeholder for Status' }}</div>
            </div>

            <!-- Slot study course -->
            <slot name="course" class="overview-text">{{ course }}</slot>
        </div>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';
.application-overview-container {
    @include applicationOverview();
}

.date-block {
    border: 1px solid #ccc;
    padding: 10px;
    margin-bottom: 5px;
}
</style>

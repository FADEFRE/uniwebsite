<!-- ApplicationOverview.vue -->

<script setup>
import CreationDate from '@/assets/icons/CreationDate.svg';
import LastEditedDate from '@/assets/icons/LastEditedDate.svg';
import DecisionDate from '@/assets/icons/DecisionDate.svg';

import { computed } from 'vue';

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

const statusStyle = computed(() => {
    if(props.status === "NEU") return "status-container greenBackground";
    if(props.status === "STUDIENBÜRO" || props.status === "PRÜFUNGSAUSSCHUSS") return "status-container yellowBackground";
    if(props.status === "ABGESCHLOSSEN") return "status-container redBackground";
})
</script>

<template>
    <div class="application-overview-container">
        <div class="dates">
            <!-- Div-Block Creation Date -->
            <div v-if="creationDate" class="date-block">
                <img :src="CreationDate" alt="Creation Date Icon" />
                <p>{{ creationDate }}</p>
            </div>

            <!-- Div-Block Last edited Date -->
            <div v-if="lastEditedDate" class="date-block">
                <img :src="LastEditedDate" alt="LastEdited Date Icon" />
                <p>{{ creationDate }}</p>
            </div>

            <!-- Div-Block Decision Date -->
            <div v-if="decisionDate" class="date-block">
                <img :src="DecisionDate" alt="Decision Date Icon" />
                <p>{{ creationDate }}</p>
            </div>
        </div>

        <!-- remaining data -->
        <div class="application-info">
            <div class="vorgangsnummer-container">
                <div class="vorgangsnummer-text overview-text">Vorgangsnummer: {{ id || 'Placeholder for Vorgangsnummer' }}
                </div>
            </div>
            <div :class="statusStyle">
                <div class="status-text overview-text">Status: {{ status || 'Placeholder for Status' }}</div>
            </div>

            <!-- Slot study course -->
            <div class="course-container">
                <slot name="course">
                    <div class="overview-text">{{ course }}</div>
                </slot>
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.application-overview-container {
    @include applicationOverview();
}


.course-container {
    @include smallHighlightBox();
    background-color: $dark-gray;
    color: $white;
}
.vorgangsnummer-container {
    @include smallHighlightBox();
    background-color: $gray;
}
.status-container {
    @include smallHighlightBox();
    color: $white;
}
.greenBackground {
    background-color: $green;
}
.orangeBackground {
    background-color: $red;
}
.redBackground {
    background-color: $red;
}

.dates {
    display: flex;
    align-items: center;
    gap: 0.9375rem;
}

.date-block {
    display: flex;
    align-items: center;
    gap: 0.625rem;
}
</style>

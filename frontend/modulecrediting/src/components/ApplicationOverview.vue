<!-- ApplicationOverview.vue -->

<script setup>
import CreationDate from '@/assets/icons/CreationDate.svg';
import LastEditedDate from '@/assets/icons/LastEditedDate.svg';
import DecisionDate from '@/assets/icons/DecisionDate.svg';

import { computed } from 'vue';
import router from "@/router";

const props = defineProps({
    creationDate: {
        type: String,
        required: true,
    },
    lastEditedDate: {
        type: String,
    },
    decisionDate: {
        type: String,
    },
    id: {
        type: String,
    },
    course: {
        type: String,
        // if not given use slot course
    },
    status: {
        type: String,
        required: true,
    },
    forward: {
        type: String,
    }
});

const statusStyle = computed(() => {
    if (props.status === "NEU") return "status-container greenBackground";
    if (props.status === "STUDIENBÜRO" || props.status === "PRÜFUNGSAUSSCHUSS") return "status-container orangeBackground";
    if (props.status === "ABGESCHLOSSEN") return "status-container redBackground";
})

const triggerForward = () => {
    if (props.forward && props.id) {
        router.push({ name: props.forward, params: { id: props.id } })
    }
}
</script>

<template>
    <div class="application-overview-container" @click="triggerForward">
        <div class="dates">
            <!-- Div-Block Creation Date -->
            <div v-if="creationDate" class="date-block">
                <img :src="CreationDate" alt="Creation Date Icon" />
                <p>{{ creationDate }}</p>
            </div>

            <!-- Div-Block Last edited Date -->
            <div v-if="lastEditedDate" class="date-block">
                <img :src="LastEditedDate" alt="LastEdited Date Icon" />
                <p>{{ lastEditedDate }}</p>
            </div>

            <!-- Div-Block Decision Date -->
            <div v-if="decisionDate" class="date-block">
                <img :src="DecisionDate" alt="Decision Date Icon" />
                <p>{{ decisionDate }}</p>
            </div>
        </div>

        <!-- remaining data -->
        <div class="application-info">
            <div v-if="id" class="vorgangsnummer-container">
                <div class="vorgangsnummer-text overview-text">Vorgangsnummer: {{ id || 'Placeholder for Vorgangsnummer' }}
                </div>
            </div>

            <!-- Slot study course -->
            <div>
                <slot>
                    <div class="course-container">
                        <div class="overview-text">{{ course }}</div>
                    </div>
                </slot>
            </div>

            <div :class="statusStyle">
                <div class="status-text overview-text">Status: {{ status || 'Placeholder for Status' }}</div>
            </div>


        </div>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';



.application-overview-container {
    background-color: $white;
    display: flex;
    padding: 0.625rem 0.625rem 0.625rem 1.25rem;
    justify-content: space-between;
    align-items: center;
    align-content: center;
    row-gap: 0.625rem;
    align-self: stretch;
    flex-wrap: wrap;
}

.application-info {
    display: flex;
    align-items: center;
    align-content: center;
    gap: 0.9375rem;
    flex-wrap: wrap;
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
    background-color: $orange;
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

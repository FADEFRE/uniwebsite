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
    },
    adminSelectionView: {
        type: Boolean,
        default: false
    }
});

const statusStyle = computed(() => {
    if (props.status === "NEU") return "status-container greenBackground";
    if (props.status === "STUDIENBÜRO" || props.status === "PRÜFUNGSAUSSCHUSS" || props.status === "IN BEARBEITUNG") return "status-container orangeBackground";
    if (props.status === "ABGESCHLOSSEN" || props.status === "FORMFEHLER") return "status-container redBackground";
})

const triggerForward = () => {
    if (props.forward && props.id) {
        router.push({ name: props.forward, params: { id: props.id } })
    }
}

const containerStyle = computed(() => {
    if (props.forward) return "application-overview-container selection-view";
    return "application-overview-container";
});
</script>

<template>
    <div :class="containerStyle" @click="triggerForward" class="application-overview-container">
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
        <div class="application-info" :class="{ 'admin-selection-view': adminSelectionView }">
            <div class="application-info-left">
                <div v-if="id" class="vorgangsnummer-container info-container">
                    <div class="vorgangsnummer-text overview-text">
                        Vorgangsnummer: {{ id || 'Placeholder for Vorgangsnummer' }}
                    </div>
                </div>

                <!-- Slot study course -->
                <div class="course-selection-container info-container">
                    <slot>
                        <div class="course-container info-container">
                            <div class="overview-text">{{ course }}</div>
                        </div>
                    </slot>
                </div>
            </div>



            <div :class="statusStyle" class="info-container">
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
    gap: 0.625rem;
    align-self: stretch;
    flex-wrap: wrap;

    @media only screen and (max-width: 1400px) {

        gap: 0.7rem;
    }
}

.admin-selection-view:hover {
    transition: 0.1s ease-in-out;
    background-color: $white-hover;
    cursor: pointer;
}

.selection-view {
    flex-direction: column;
}


.application-info {
    display: flex;
    gap: 0.9375rem;

    max-width: 100%;
    flex-wrap: wrap;

    &.admin-selection-view {
        @media only screen and (max-width: 1400px) {
            flex-direction: column;
            gap: 0.4rem;
        }
    }
}

.application-info-left {
    display: flex;
    align-items: center;
    align-content: center;
    gap: 0.9375rem;
    flex-wrap: wrap;

    @media only screen and (max-width: 550px) {
        flex-direction: column;
        width: 100%;
        gap: 0.4rem;
    }
}



.course-container {
    @include smallHighlightBox();
    background-color: $dark-gray;
    color: $white;
    min-width: 12rem;
    width: 12rem;
}

.course-selection-container {
    width: min-content;
    position: relative;
    display: flex;
    flex-direction: column;
    gap: 0.05rem;
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
    flex-wrap: wrap;
}

.date-block {
    display: flex;
    align-items: center;
    gap: 0.625rem;
}

.info-container {
    @media only screen and (max-width: 550px) {
        width: 100%;
    }
}
</style>

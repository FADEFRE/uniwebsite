<!-- ApplicationOverview.vue -->

<script setup>
import { computed } from 'vue';
import router from "@/router";
import DateIcon from '../assets/icons/DateIcon.vue';

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
    let style = "";
    if (props.status === "NEU") style += "greenBackground";
    if (props.status === "STUDIENBÜRO" || props.status === "PRÜFUNGSAUSSCHUSS" || props.status === "IN BEARBEITUNG") style += "orangeBackground";
    if (props.status === "ABGESCHLOSSEN" || props.status === "FORMFEHLER") style += "redBackground";

    if (props.adminSelectionView) style += " admin-selection-view"

    return style;
})

const triggerForward = () => {
    if (props.forward && props.id) {
        router.push({ name: props.forward, params: { id: props.id } })
    }
}

</script>

<template>
    <div @click="triggerForward" class="application-overview-container"
        :class="{ 'admin-selection-view': adminSelectionView }">
        <div class="dates">
            <!-- Div-Block Creation Date -->
            <div v-if="creationDate" class="date-block">
                <DateIcon type="creation" />
                <p>{{ creationDate }}</p>
            </div>

            <!-- Div-Block Last edited Date -->
            <div v-if="lastEditedDate" class="date-block">
                <DateIcon type="lastEdited" />
                <p>{{ lastEditedDate }}</p>
            </div>

            <!-- Div-Block Decision Date -->
            <div v-if="decisionDate" class="date-block">
                <DateIcon type="decision" />
                <p>{{ decisionDate }}</p>
            </div>
        </div>

        <!-- remaining data -->
        <div class="application-info">
            <div v-if="id" class="vorgangsnummer-container info-container"
                :class="{ 'admin-selection-view': adminSelectionView }">
                <div class="vorgangsnummer-text overview-text white">
                    Vorgangsnummer: {{ id || 'Placeholder for Vorgangsnummer' }}
                </div>
            </div>

            <!-- Slot study course -->
            <div class="course-selection-container info-container">
                <slot>
                    <div class="course-container info-container" :class="{ 'admin-selection-view': adminSelectionView }">
                        <div class="overview-text">{{ course }}</div>
                    </div>
                </slot>
            </div>



            <div :class="statusStyle" class="info-container status-container">
                <div class="status-text overview-text">Status: {{ status || 'Placeholder for Status' }}</div>
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.application-overview-container {
    background-color: $white;
    display: flex;
    padding: spacing(s);
    padding-left: spacing(m);
    justify-content: space-between;
    gap: spacing(s);
    align-self: stretch;
    flex-wrap: wrap;
    transition: 0.1s ease-in-out;

    &.admin-selection-view {
        flex-direction: column;

        &:hover, &:focus {
            background-color: $white-hover;
            cursor: pointer;
        }
    }

    @include breakpoint(xs) {
        flex-direction: column;
        gap: spacing(s);
    }
}


.application-info {
    display: flex;
    gap: spacing(s);
    max-width: 100%;
    flex-wrap: wrap;

    @include breakpoint(xs) {
        flex-direction: column;
        gap: spacing(s);
    }
}


.course-container {
    @include smallHighlightBox();
    background-color: $dark-gray;

    &.admin-selection-view {
        min-width: max-content;
        width: 12rem;
    }

    @include breakpoint(xs) {
        width: 100%;
    }
}

.course-selection-container {
    width: min-content;
    position: relative;
    display: flex;
    flex-direction: column;
}

.vorgangsnummer-container {
    @include smallHighlightBox();
    background-color: $gray;

    &.admin-selection-view {
        width: 18rem;
    }
}

.status-container {
    @include smallHighlightBox();
    height: min-content;

    &.admin-selection-view {
        width: 17rem;
    }
}

.info-container {

    color: $white;

    @include breakpoint(xs) {
        width: 100%;

        &.admin-selection-view {
            width: 100%;
        }
    }
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
    gap: spacing(m);
    flex-wrap: wrap;
}

.date-block {
    display: flex;
    align-items: center;
    gap: spacing(s);
}
</style>

<script setup>
import { computed } from 'vue';
import router from "@/router";
import i18n from '@/i18n';
import DateIcon from '@/assets/icons/DateIcon.vue';

/*
overview of an application, can be used to route to application on click

slots:
  default - used for course dropdown if course has to be selected
 */

const props = defineProps({
  /* creation date to display */
  creationDate: {
    type: String,
    required: true,
  },
  /* last edited date to display */
  lastEditedDate: {
    type: String,
  },
  /* decision date to display */
  decisionDate: {
    type: String,
  },
  /* id to display, used for routing if forward is specified */
  id: {
    type: String,
  },
  /* course to display, should be specified or course dropdown should be defined in slot */
  course: {
    type: String,
  },
  /* status to display */
  status: {
    type: String,
    required: true,
  },
  /* route name that should be routed to (with specified id) on click */
  forward: {
    type: String,
  },
  /* should be set to true if component is used in administrative selection view, modifies styling */
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

const statusTranslate = computed(() => {
  let translation = props.status;
  if (props.status === "NEU") translation = i18n.global.t('Status.New');
  if (props.status === "IN BEARBEITUNG") translation = i18n.global.t('Status.InProgress');
  if (props.status === "ABGESCHLOSSEN") translation = i18n.global.t('Status.Finished');
  if (props.status === "FORMFEHLER") translation = i18n.global.t('Status.FormalError');
  return translation;
})

const triggerForward = () => {
  if (props.forward && props.id) {
    router.push({ name: props.forward, params: { id: props.id } })
  }
}

</script>

<template>
  <div :tabindex="adminSelectionView ? 0 : -1" @keydown.enter="triggerForward" @click="triggerForward" class="application-overview-container" :class="{ 'admin-selection-view': adminSelectionView }">
    <h2 class="screen-reader-only"> {{ $t('ApplicationOverview.SRHeading') }}</h2>
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
          {{ $t('ApplicationOverview.ProcessNumber') }}: {{ id }}
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
        <div class="status-text overview-text">Status: {{ statusTranslate }}</div>
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

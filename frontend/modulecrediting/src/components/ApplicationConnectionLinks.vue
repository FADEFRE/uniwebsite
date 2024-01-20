<!--
displays link overview for one application
props:
  - connectionData, array of objects, each having id (Number),
    externalModules (array of Strings), internalModules (array of Strings) property
-->

<script setup>
import PanelHeader from "@/components/PanelHeader.vue";

const props = defineProps({
  connectionsData: {
    required: true,
    type: Array,
    validator(value) {
      return value.every(obj => obj.id && obj.externalModules)
    }
  }
})
</script>

<template>
  <div class="application-connection-links-container">

    <h2>Übersicht</h2>

    <a v-for="connection in connectionsData" :href="'#' + connection.id" class="connection-link-container">
      <p class="connection-text"> {{ connection.externalModules.join(', ') }}</p>
      <div>
        <img src="@/assets/icons/ArrowRed.svg" alt="arrow-icon" class="arrow-icon">
      </div>
    </a>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.application-connection-links-container {
  @include basicContainer();
  width: 22%;
}

.connection-link-container {
  padding: 0.25rem 0.625rem;
  width: 100%;
  background-color: transparent;

  border-top: 2px solid $dark-gray;
  border-bottom: 2px solid $dark-gray;
  gap: 0.08rem;

  &:hover{
    background-color: $gray;
    & .arrow-icon {
      transform: translateX(0.1rem) rotate(-90deg);
    }
  }

}

.connection-text {
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.arrow-icon {
  transition: 0.1s ease-in-out;
  transform: rotate(-90deg);
}
</style>
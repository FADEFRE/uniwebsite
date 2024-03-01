<!--
displays link overview for one application
props:
  - connectionData, array of objects, each having id (Number),
    externalModules (array of Strings), internalModules (array of Strings) property
-->

<script setup>
import ArrowIcon from '@/assets/icons/ArrowIcon.vue';

const props = defineProps({
  connectionsData: {
    required: true,
    type: Array,
    validator(value) {
      return value.every(obj => obj.id && obj.externalModules)
    }
  }
})

const scrollTo = (id) => {
  document.getElementById(id).scrollIntoView({ behavior: 'smooth' })
}
</script>

<template>
  <div class="application-connection-links-container">

    <h2>{{ $t('ApplicationConnectionLinks.Overview') }}</h2>

    <div class="connection-link-container">
      <div v-for="connection in connectionsData" @click="scrollTo(connection.id)" class="connection-link-item icon-hover-right">
        <p class="connection-text"> {{ connection.externalModules.join(', ') || '...' }}</p>
        <ArrowIcon direction="right" color="red" :hover="true"/>
      </div>
    </div>
    

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.application-connection-links-container {
  @include basicContainer();
  @include verticalList(m);
}

.connection-link-container {
  @include verticalList(none);
  border-top: 2px solid $dark-gray;
}

.connection-link-item {
  padding: spacing(xs) spacing(s);
  width: 100%;
  background-color: transparent;

  //border-top: 2px solid $dark-gray;
  border-bottom: 2px solid $dark-gray;

  display: flex;
  gap: spacing(xs);
  align-items: center;

  &:hover, &:focus {
    background-color: $white-hover;
  }
}

.connection-text {
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-transform: none;
}
</style>
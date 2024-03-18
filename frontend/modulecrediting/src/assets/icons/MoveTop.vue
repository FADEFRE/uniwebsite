<script setup>
import ArrowIcon from '@/assets/icons/ArrowIcon.vue';

/*
move top icon,
triggers smooth scroll to top on click
 */

const scrollTop = () => {
  document.getElementById('app').scrollIntoView({ behavior: 'smooth' })
}
</script>

<template>
  <div class="move-top-button" @click="scrollTop">
    <ArrowIcon color="white" direction="up" size="big" />
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.move-top-button {
  background-color: $dark-gray;
  width: 3rem;
  height: 3rem;

  transition: 0.1s ease-in-out;

  &:hover,
  &:focus {
    background-color: $dark-gray-hover;
    cursor: pointer;
  }

  &:focus {
    outline: 2.5px solid $dark-gray-hover;
    outline-offset: 2px;
  }

  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

<script setup>
/*
arrow icon with variable color, direction and size
 */

const props = defineProps({
  /* 'white', 'dark-gray' or 'red' */
  color: {
    type: String,
    default: 'white'
  },
  /* 'up', 'right', 'left' or 'down' */
  direction: {
    required: true,
    type: String
  },
  /* 'small', 'mid' or 'big' */
  size: {
    type: String,
    default: 'mid'
  }
})
</script>

<template>
  <svg :class="[direction, size, color]" class="arrow-icon"  viewBox="0 0 16 10" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
    <path fill-rule="evenodd" clip-rule="evenodd"
          d="M15 2.57996L12.897 0.5L8 5.34094L3.10303 0.5L1 2.57996L8 9.5L15 2.57996Z"/>
  </svg>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.arrow-icon {
  width: rem(16px);
  height: rem(10px);

  transition: 0.15s transform ease-in-out;
}
.big {
  width: rem(18px);
  height: rem(12px);
}
.small {
  width: rem(14px);
  height: rem(8px);
}

.up {
  transform: rotate(180deg);
}
.right {
  transform: rotate(-90deg);
}
.left {
  transform: rotate(90deg);
}
.down {
  transform: rotate(0deg);
}

.white {
  fill: $white;
}
.dark-gray {
  fill: $dark-gray;
}
.red {
  fill: $red;
}

.icon-hover-right:hover .arrow-icon,
.icon-hover-right:focus .arrow-icon {
  transform: translateX(spacing(xs)) rotate(-90deg);
}
.icon-hover-left:hover .arrow-icon,
.icon-hover-left:focus .arrow-icon {
  transform: translateX(-#{spacing('xs')}) rotate(90deg);
}
</style>
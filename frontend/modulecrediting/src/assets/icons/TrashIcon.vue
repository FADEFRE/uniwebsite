<script setup>
const props = defineProps({
    backgroundColor: {
        type: String,
        default: 'gray'
    },
    backgroundSize: {
        type: String,
        default: 'mid'
    }
});

const emit = defineEmits(['click'])
</script>

<template>
  <div class="trash-container" :class="[backgroundColor, backgroundSize]" tabindex="0" @keydown.enter.prevent="emit('click')" @click="emit('click')">
    <svg class="trash-icon" :class="backgroundColor === 'dark-gray' ? 'white-icon' : 'dark-gray-icon'" viewBox="0 0 17 19" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M2.5 4.97768V18H14.5V4.97768L17 4.9941L13 4.97768M0 4.9941L4 4.97768M4 4.97768L5.5 1.01369L11.5 1L13 4.97768M4 4.97768H8.5H13M6.42308 8.60815V14.6852M10.5769 8.60815V14.6852" stroke-width="2" />
    </svg>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.trash-container {
  transition: 0.1s ease-in-out;
  display: flex;

  &:hover, &:hover {
    cursor: pointer;
  }
}
.gray:hover, .gray:focus {
    background-color: $gray-hover;
}
 
.white:hover, .white:focus {
    background-color: $white-hover;
}
.gray:focus-visible,
.white:focus-visible {
    outline: 2px solid $dark-gray-hover;
    outline-offset: 2px;
}



.mid {
    @include smallHighlightBox();
}
.small {
    padding: spacing(s);
}



.trash-icon {
    width: rem(17px);
    height: rem(19px);
    transition: 0.1s ease-in-out;
}
.white-icon {
    stroke: $white-hover;
    &:hover {
        stroke: $white;
    }
}
.dark-gray-icon {
    stroke: $dark-gray;
}
</style>